package $group__.utilities;

public enum Constants {
	/* MARK empty */;


	public static final String
			MOD_ID = "${modId}",
			GROUP = "${group}",
			PACKAGE = GROUP + '.' + MOD_ID;

	@SuppressWarnings({"OctalInteger", "RedundantSuppression"})
	public static final int
			RADIX_DECIMAL = 10,
			RADIX_HEX = 0x10,
			RADIX_OCTAL = 010,
			RADIX_BINARY = 0b10;
}
