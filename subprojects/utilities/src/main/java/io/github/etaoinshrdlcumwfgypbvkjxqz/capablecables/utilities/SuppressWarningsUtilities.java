package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import java.util.function.Supplier;

public enum SuppressWarningsUtilities {
	;

	// CODE suppressThisEscapedWarning(() -> this)
	public static <T> T suppressThisEscapedWarning(Supplier<? extends T> thisSupplier) {
		return thisSupplier.get();
	}

	@SuppressWarnings("AutoBoxing")
	public static Boolean suppressBoxing(boolean value) {
		return value;
	}

	@SuppressWarnings("AutoBoxing")
	public static Byte suppressBoxing(byte value) {
		return value;
	}

	@SuppressWarnings("AutoBoxing")
	public static Short suppressBoxing(short value) {
		return value;
	}

	@SuppressWarnings("AutoBoxing")
	public static Integer suppressBoxing(int value) {
		return value;
	}

	@SuppressWarnings("AutoBoxing")
	public static Long suppressBoxing(long value) {
		return value;
	}

	@SuppressWarnings("AutoBoxing")
	public static Float suppressBoxing(float value) {
		return value;
	}

	@SuppressWarnings("AutoBoxing")
	public static Double suppressBoxing(double value) {
		return value;
	}

	@SuppressWarnings("AutoUnboxing")
	public static boolean suppressUnboxing(Boolean value) {
		return value;
	}

	@SuppressWarnings("AutoUnboxing")
	public static byte suppressUnboxing(Byte value) {
		return value;
	}

	@SuppressWarnings("AutoUnboxing")
	public static short suppressUnboxing(Short value) {
		return value;
	}

	@SuppressWarnings("AutoUnboxing")
	public static int suppressUnboxing(Integer value) {
		return value;
	}

	@SuppressWarnings("AutoUnboxing")
	public static long suppressUnboxing(Long value) {
		return value;
	}

	@SuppressWarnings("AutoUnboxing")
	public static float suppressUnboxing(Float value) {
		return value;
	}

	@SuppressWarnings("AutoUnboxing")
	public static double suppressUnboxing(Double value) {
		return value;
	}
}
