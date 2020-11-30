package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.primitives;

public enum BooleanUtilities {
	;

	public enum PaddedBool {
		;

		public static final byte FALSE = 0;
		public static final byte TRUE = 1;

		public static int orBool(int a, int b) {
			return a | b;
		}

		public static long orBool(long a, long b) {
			return a | b;
		}

		public static int andBool(int a, int b) {
			return a & b;
		}

		public static long andBool(long a, long b) {
			return a & b;
		}

		public static byte padBool(boolean value) {
			return value ? tBool() : fBool();
		}

		public static byte tBool() {
			return getTrue();
		}

		public static byte fBool() {
			return getFalse();
		}

		private static byte getTrue() {
			return TRUE;
		}

		private static byte getFalse() {
			return FALSE;
		}

		public static boolean stripBool(long value) {
			return value != fBool();
		}

		public static boolean stripBool(int value) {
			return value != fBool();
		}

		public static boolean stripBool(short value) {
			return value != fBool();
		}

		public static boolean stripBool(byte value) {
			return value != fBool();
		}
	}
}
