package org.util.datautil;

public class Strings {

	public static final String trimSpecial(final String s) {
		if(s == null) return "**null**";
		return s.strip();
	}
	
	public static final String trim(final String s) {
		if(s == null) return "";
		return s.strip();
	}
	
	public static final boolean isNullOrEmpty(final String s) {
		return s == null || s.trim().length() == 0;
	}

	public static final boolean isNull(final String s) {
		return s == null;
	}

	public static final String lessOrEqualLength(final String s, final int len) {
		if(s == null) return "";
		else if(s.length() > len) return s.substring(0, len);
		else return s;
	}

	
	public static final String makeOfLength(final String s, final int len) {
		if(s == null) return makeOfLength("null", len);
		else if(s.length() == len) return s;
		else if(s.length() > len) return s.substring(0, len);
		else {
			final StringBuilder sb = new StringBuilder();
			sb.append(s);
			while (sb.length() != len) sb.append(" ");
			return s;
		}
	}
	
	public static String padRight(String string, char padChar, int len) {
		if (string == null || string.length() >= len) return string;
		StringBuilder sb = new StringBuilder(len);
		sb.append(string);
		while (sb.length() != len) sb.append(padChar);
		return sb.toString();
	}
	
	public static String padRight(StringBuilder sb, char padChar, int len) {
		if (sb == null || sb.length() >= len) return sb.toString();
		while (sb.length() != len) sb.append(padChar);
		return sb.toString();
	}

	public static String padLeft(String string, char padChar, int len) {
		if (string == null || string.length() >= len) return string;
		StringBuilder sb = new StringBuilder(len);
		while (sb.length() != (len - string.length())) sb.append(padChar);
		sb.append(string);
		return sb.toString();
	}

}
