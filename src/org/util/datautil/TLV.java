package org.util.datautil;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public final class TLV {

	public static final String separator = "-----------------------------------------------------------------------------------------------------------------------";
	private static final int			size	= 3;
	private final Map<String, String>	map;

	public TLV() {
		map = new TreeMap<>();
	}

	public TLV(boolean sort) {
		if (sort) map = new TreeMap<>();
		else map = new LinkedHashMap<>();
	}

	public final TLV put(String key, String value) {
		map.put(key, value);
		return this;
	}

	public final String get(String key) {
		return map.get(key);
	}
	
	public final TLV remove(String key) {
		map.remove(key);
		return this;
	}
	
	public final String removeGet(String key) {
		return map.remove(key);
	}

	public final TLV keepAll(List<String> tags) {
		final Map<String, String> newmap = new HashMap<>();
		tags.forEach(tag-> {
			if(map.get(tag)!= null) newmap.put(tag, map.get(tag));
		});
		map.clear();
		map.putAll(newmap);
		return this;
	}
	
	public final TLV removeAll(List<String> tags) {
		tags.forEach(tag-> map.remove(tag));
		return this;
	}
	
	public static final TLV parse(String tlvString) {
		TLV tlv = new TLV();
		if(tlvString == null) return tlv;
		int i = 0;
		try {
			while (i < tlvString.length()) {
				String tagname = tlvString.substring(i, i + size);
				i = i + size;
				int taglen = Integer.parseInt(tlvString.substring(i, i + size));
				i = i + size;
				tlv.map.put(tagname, tlvString.substring(i, i + taglen));
				i = i + taglen;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tlv;
	}

	public final String build() {
		StringBuilder sb = new StringBuilder(50);
		String format = "%0" + size + "d";
		map.forEach((key, value) -> {
			if(value == null) value = "";
			sb.append(key);
			sb.append(String.format(format, value.length()));
			sb.append(value);
		});
		return sb.toString();
	}
	
	@Override
	public final String toString() {
		final Toggle toggle = new Toggle(false);
		final StringBuilder sb = new StringBuilder(30);
		sb.append("\r\n").append(separator).append("\r\n");
		map.forEach((key, value) -> {
			if(value.length() > 51 && toggle.get()) sb.append("\r\n");
			sb.append("| ").append(key).append(" : ").append(String.format("%-51s", value));
			if(toggle.get()) sb.append("|\r\n");
			toggle.toggle();
		});
		if (toggle.get()) sb.append("\r\n");
		sb.append(separator);
		return sb.toString();
	}
	
	public static void main(String[] args) {
		TLV tlv = new TLV();
		tlv.put("001", "A");
		tlv.put("002", "B");
		tlv.put("003", "C");
		tlv.put("004", "D");
		System.out.println(tlv);
		System.out.println(tlv);
		
	}
}
