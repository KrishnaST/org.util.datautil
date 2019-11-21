package org.util.datautil;

import java.nio.charset.StandardCharsets;

public class BCD {

	private static final String[] bcdStrings = new String[100];
	private static final byte[][] bcdASCII   = new byte[100][];

	static {
		for (int i = 0; i < bcdStrings.length; i++) {
			bcdStrings[i] = String.format("%02d", i);
			bcdASCII[i]   = bcdStrings[i].getBytes(StandardCharsets.US_ASCII);
		}
	}

	public static final String getBCDString(final int dec) {
		return bcdStrings[dec];
	}

	public static final byte[] getBCDASCIIBytes(final int dec) {
		return bcdASCII[dec];
	}

	public static void main(String[] args) {
		for (int i = 0; i < bcdStrings.length; i++) {
			System.out.println(bcdStrings[i]);
			System.out.println(bcdASCII[i]);
		}
	}
}
