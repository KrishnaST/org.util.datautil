package org.util.datautil;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public final class TLV {

	public static final String        separator = "-----------------------------------------------------------------------------------------------------------------------";
	private static final int          size      = 3;
	private final Map<String, String> map;

	public TLV() {
		map = new TreeMap<>();
	}

	public TLV(final boolean sort) {
		if (sort) map = new TreeMap<>();
		else map = new LinkedHashMap<>();
	}

	public final TLV put(final String k, final String v) {
		map.put(k, v);
		return this;
	}

	public final String get(String k) {
		return map.get(k);
	}

	public final TLV remove(String k) {
		map.remove(k);
		return this;
	}

	public final String removeGet(String k) {
		return map.remove(k);
	}

	public final TLV keepAll(final Collection<String> tags) {
		final Map<String, String> newmap = new HashMap<>();
		tags.forEach(tag -> { if (map.get(tag) != null) newmap.put(tag, map.get(tag)); });
		map.clear();
		map.putAll(newmap);
		return this;
	}

	public final TLV removeAll(final Collection<String> tags) {
		tags.forEach(tag -> map.remove(tag));
		return this;
	}

	public static final TLV parse(final String tlvString) {
		TLV tlv = new TLV();
		if (tlvString == null) return tlv;
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
		StringBuilder sb     = new StringBuilder(50);
		String        format = "%0" + size + "d";
		map.forEach((key, value) -> {
			if (value == null) value = "";
			sb.append(key);
			sb.append(String.format(format, value.length()));
			sb.append(value);
		});
		return sb.toString();
	}

	@Override
	public final String toString() {
		final Toggle        toggle = new Toggle(false);
		final StringBuilder sb     = new StringBuilder(30);
		sb.append("\r\n").append(separator).append("\r\n");
		map.forEach((key, value) -> {
			if (value.length() > 51 && toggle.get()) sb.append("\r\n");
			sb.append("| ").append(key).append(" : '").append(value).append("'").append(String.format("%-"+(51-value.length())+"s", ""));
			if (toggle.get()) sb.append("|\r\n");
			toggle.toggle();
		});
		if (toggle.get()) sb.append("\r\n");
		sb.append(separator);
		return sb.toString();
	}

	public static void main(String[] args) {
		TLV tlv = TLV.parse("00100248002003MOB045020NATEKAR MILIND ANANT04900300005001785360029673992547051005test4056003MOB059011SRCB0000379062015379203100000101");
		System.out.println(tlv);
	}
}
