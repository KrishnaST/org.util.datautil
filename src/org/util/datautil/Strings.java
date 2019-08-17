package org.util.datautil;

public class Strings {

	public static final boolean isNullOrEmpty(final String s) {
		return s == null || s.trim().length() == 0;
	}

	public static final boolean isNull(final String s) {
		return s == null;
	}

	
}
