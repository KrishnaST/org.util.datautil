package org.util.datautil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public final class BERTLV {

	public static final String separator = "-----------------------------------------------------------------------------------------------------------------------";
	private final Map<String, String>	map;

	public BERTLV() {
		map = new TreeMap<>();
	}
	
	public BERTLV(boolean sort) {
		if (sort) map = new TreeMap<>();
		else map = new LinkedHashMap<>();
	}
	
	public final void put(String tag, String value) {
		map.put(tag, value);
	}

	public final void put(byte[] tag, byte[] value) {
		map.put(ByteHexUtil.byteToHex(tag), ByteHexUtil.byteToHex(value));
	}

	public final String get(String tag) {
		return map.get(tag);
	}

	public final byte[] getBytes(String tag) {
		return ByteHexUtil.hexToByte(map.get(tag));
	}

	public final BERTLV keepAll(List<String> tags) {
		final Map<String, String> newmap = new HashMap<>();
		tags.forEach(tag-> {
			if(map.get(tag)!= null) newmap.put(tag, map.get(tag));
		});
		map.clear();
		map.putAll(newmap);
		return this;
	}
	
	public final BERTLV removeAll(List<String> tags) {
		tags.forEach(tag-> map.remove(tag));
		return this;
	}

	public final String pack() {
		StringBuilder sb = new StringBuilder();
		map.forEach((k, v) -> {
			sb.append(k);
			int len = v.length() / 2;
			if (len <= 0x7F) sb.append(ByteHexUtil.byteToHex((byte) len));
			else if (len <= 0xFF) sb.append(ByteHexUtil.byteToHex(new byte[]{(byte) 0x81, (byte) len}));
			else if (len <= 0xFFFF) sb.append(ByteHexUtil.byteToHex(new byte[]{(byte) 0x82, (byte) (len / 0x100), (byte) (len % 0x100)}));
			else if (len <= 0xFFFFFF) sb.append(ByteHexUtil.byteToHex(new byte[]{(byte) 0x83, (byte) (len / 0x10000), (byte) (len % 0x10000 / 0x100), (byte) (len % 0x100)}));
			else if (len <= 0xFFFFFFFF) sb.append(ByteHexUtil.byteToHex(new byte[]{(byte) 0x84, (byte) (len / 0x10000), (byte) (len % 0x10000 / 0x100), (byte) (len % 0x100)}));
			sb.append(v);
		});
		return sb.toString();
	}

	@Override
	public final String toString() {
		final Toggle toggle = new Toggle(false);
		final StringBuilder sb = new StringBuilder(30);
		sb.append("\r\n").append(separator).append("\r\n");
		map.forEach((key, value) -> {
			if (value.length() > 51 && toggle.get()) sb.append("\r\n");
			sb.append("| ").append(String.format("%4s", key)).append(" : ").append(String.format("%-51s", value));
			if (toggle.get()) sb.append("|\r\n");
			toggle.toggle();
		});
		if (toggle.get()) sb.append("\r\n");
		sb.append(separator);
		return sb.toString();
	}

	public static final BERTLV parse(final byte[] bytes) {
		final BERTLV bertlv = new BERTLV();
		int p = 0;
		while (p < bytes.length) {
			byte[] name = null;
			byte[] value = null;
			int length = 0;
			try {
				if((bytes[p] & 0x1F) == 0x1F) {
					name = new byte[]{bytes[p], bytes[p+1]};
					p++;
				}
				else name = new byte[]{bytes[p]};
				p++;
				if (0 < bytes[p] && bytes[p] < 0x7F) {
					length = bytes[p] & 0xFF;
					p++;
				} else {
					int lenLen = bytes[p] & 0x03;
					p++;
					length = getLen(Arrays.copyOfRange(bytes, p, p + lenLen));
					p = p + lenLen;
				}
				value = Arrays.copyOfRange(bytes, p, p + length);
				p = p + length;
				bertlv.put(ByteHexUtil.byteToHex(name), ByteHexUtil.byteToHex(value));
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
		return bertlv;
	}

	public static final BERTLV parse(final String string) {
		return parse(ByteHexUtil.hexToByte(string));
	}
	
	private static final int getLen(byte... bytes) {
		int len = 0;
		int exponent = 1;
		for (int i = (bytes.length - 1); i >= 0; i--) {
			len = len + (bytes[i] & 0xFF) * exponent;
			exponent = exponent * 256;
		}
		return len;
	}
	
	public static void main(String[] args) {
		BERTLV bertlv = parse("5A0860748412300002215F24032612315F2A0203565F34010182023900950580000400009A031906249C01319F02060000000000009F03060000000000009F0610A00000052410100000000000000000019F10080105A000000000009F1A0203569F26087D69FA43A04982FE9F2701809F33036040209F34034201009F3501149F360200089F370409C1D25D");
		System.out.println(bertlv);
		bertlv.removeAll(Arrays.asList("50"));
		System.out.println(bertlv);
	}
}
