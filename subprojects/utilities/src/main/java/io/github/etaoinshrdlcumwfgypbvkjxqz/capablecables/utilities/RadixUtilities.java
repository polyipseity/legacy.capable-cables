package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

public enum RadixUtilities {
	;

	@SuppressWarnings({"OctalInteger", "RedundantSuppression"})
	public static final int
			RADIX_DECIMAL = 10,
			RADIX_HEX = 0x10,
			RADIX_OCTAL = 010,
			RADIX_BINARY = 0b10;

	public static int getRadixDecimal() {
		return RADIX_DECIMAL;
	}

	public static int getRadixHex() {
		return RADIX_HEX;
	}

	public static int getRadixOctal() {
		return RADIX_OCTAL;
	}

	public static int getRadixBinary() {
		return RADIX_BINARY;
	}
}
