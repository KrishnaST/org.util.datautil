package org.util.datautil;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public final class BERTLV {

	public static final String        separator = "-----------------------------------------------------------------------------------------------------------------------";
	private final Map<String, String> map;

	public BERTLV() {
		map = new TreeMap<>();
	}

	public BERTLV(final boolean sort) {
		if (sort) map = new TreeMap<>();
		else map = new LinkedHashMap<>();
	}

	public final void put(final String t, final String v) {
		if (t == null || v == null) return;
		map.put(t, v);
	}

	public final void put(final byte[] t, final byte[] v) {
		if (t == null || v == null) return;
		map.put(ByteHexUtil.byteToHex(t), ByteHexUtil.byteToHex(v));
	}

	public final String get(final String t) {
		if (t == null) return null;
		return map.get(t);
	}

	public final byte[] getBytes(final String t) {
		if (t == null) return null;
		return ByteHexUtil.hexToByte(map.get(t));
	}

	public final String pack() {
		if (map.isEmpty()) return "";
		StringBuilder sb = new StringBuilder();
		map.forEach((k, v) -> {
			sb.append(k);
			int len = v.length() / 2;
			if (len <= 0x7F) sb.append(ByteHexUtil.byteToHex((byte) len));
			else if (len <= 0xFF) sb.append(ByteHexUtil.byteToHex(new byte[] { (byte) 0x81, (byte) len }));
			else if (len <= 0xFFFF) sb.append(ByteHexUtil.byteToHex(new byte[] { (byte) 0x82, (byte) (len / 0x100), (byte) (len % 0x100) }));
			else if (len <= 0xFFFFFF)
				sb.append(ByteHexUtil.byteToHex(new byte[] { (byte) 0x83, (byte) (len / 0x10000), (byte) (len % 0x10000 / 0x100), (byte) (len % 0x100) }));
			else if (len <= 0xFFFFFFFF)
				sb.append(ByteHexUtil.byteToHex(new byte[] { (byte) 0x84, (byte) (len / 0x10000), (byte) (len % 0x10000 / 0x100), (byte) (len % 0x100) }));
			sb.append(v);
		});
		return sb.toString();
	}

	@Override
	public final String toString() {
		if (map.isEmpty()) return "";
		final Toggle        toggle = new Toggle(false);
		final StringBuilder sb     = new StringBuilder(30);
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
		int          p      = 0;
		while (p < bytes.length) {
			byte[] name   = null;
			byte[] value  = null;
			int    length = 0;
			try {
				if ((bytes[p] & 0x1F) == 0x1F) {
					name = new byte[] { bytes[p], bytes[p + 1] };
					p++;
				} else name = new byte[] { bytes[p] };
				p++;
				if (0 < bytes[p] && bytes[p] < 0x7F) {
					length = bytes[p] & 0xFF;
					p++;
				} else {
					int lenLen = bytes[p] & 0x03;
					p++;
					length = getLen(Arrays.copyOfRange(bytes, p, p + lenLen));
					p      = p + lenLen;
				}
				value = Arrays.copyOfRange(bytes, p, p + length);
				p     = p + length;
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

	private static final int getLen(final byte... bytes) {
		int len      = 0;
		int exponent = 1;
		for (int i = (bytes.length - 1); i >= 0; i--) {
			len      = len + (bytes[i] & 0xFF) * exponent;
			exponent = exponent * 256;
		}
		return len;
	}

	public final BERTLV keepAll(final Collection<String> tags) {
		final Map<String, String> newmap = new HashMap<>();
		tags.forEach(tag -> { if (map.get(tag) != null) newmap.put(tag, map.get(tag)); });
		map.clear();
		map.putAll(newmap);
		return this;
	}

	public final BERTLV removeAll(final Collection<String> tags) {
		tags.forEach(tag -> map.remove(tag));
		return this;
	}

	public final BERTLV cleanup() {
		final Map<String, String> newmap = new HashMap<>();
		map.forEach((t, v) -> { if (t != null && v != null && t.length() > 0 && v.length() > 0) newmap.put(t, v); });
		map.clear();
		map.putAll(newmap);
		return this;
	}

	public final BERTLV keepAllCleanup(final Collection<String> tags) {
		final Map<String, String> newmap = new HashMap<>();
		tags.stream().map(t -> new Pair<String, String>(t, map.get(t)))
				.filter(p -> p.getLeft() != null && p.getRight() != null && p.getLeft().length() > 0 && p.getRight().length() > 0)
				.forEach(p -> newmap.put(p.getLeft(), p.getRight()));
		map.clear();
		map.putAll(newmap);
		return this;
	}

	public static void main(String[] args) {
		BERTLV oringinal = parse(
				"9F1A0203569F360200405F3401019F3704F9B169019F34034203009F3501119F1E009F33036040005F2A020356950580000480009F2701809A031908289F2608677150E5F6714AED9F090200649F4104000000699F0206000000300000820258009F0607A00000052410108407A00000052410104F07A00000052410109C01019B0268009F03060000000000009F10080105A00000000000");

		BERTLV changed = parse(
				"4F07A00000052410105F2A020356820258008407A0000005241010950580000480009A031908289C01019F02060000003000009F03060000000000009F0607A00000052410109F090200649F1A0203569F2608677150E5F6714AED9F2701809F33036040009F34034203009F3501119F360200409F3704F9B169019F410400000069");
		System.out.println(oringinal);
		System.out.println(changed);
	}
}
