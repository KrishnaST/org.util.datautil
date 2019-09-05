package org.util.datautil;


public class ThalesKey {

	public static final String toThalesKey(final String key) {
		if(key == null) return null;
		final int len = key.trim().length();
		if(len == 32) return "U"+key;
		if(len == 48) return "T"+key;
		return key;
	}
	
	public static final String toNormalKey(final String key) {
		if(key == null) return null;
		final int len = key.trim().length();
		if(len == 33) return key.trim().substring(1);
		if(len == 49) return key.trim().substring(1);
		return key;
	}
}
