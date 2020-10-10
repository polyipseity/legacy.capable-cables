package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

public enum ComparableUtilities {
	;

	public static <T> boolean lessThan(Comparable<T> a, T b) { return isLessThan(a.compareTo(b)); }

	public static boolean isLessThan(int result) { return result < 0; }

	public static <T> boolean equalTo(Comparable<T> a, T b) { return isEqualTo(a.compareTo(b)); }

	public static boolean isEqualTo(int result) { return result == 0; }

	public static <T> boolean unequalTo(Comparable<T> a, T b) { return isUnequalTo(a.compareTo(b)); }

	public static boolean isUnequalTo(int result) { return result != 0; }

	public static <T> boolean greaterThan(Comparable<T> a, T b) { return isGreaterThan(a.compareTo(b)); }

	public static boolean isGreaterThan(int result) { return result > 0; }

	public static <T> boolean lessThanOrEqualTo(Comparable<T> a, T b) { return isLessThanOrEqualTo(a.compareTo(b)); }

	public static boolean isLessThanOrEqualTo(int result) { return result <= 0; }

	public static <T> boolean greaterThanOrEqualTo(Comparable<T> a, T b) { return isGreaterThanOrEqualTo(a.compareTo(b)); }

	public static boolean isGreaterThanOrEqualTo(int result) { return result >= 0; }

	public static boolean lessThan(double a, double b) { return isLessThan(Double.compare(a, b)); }

	public static boolean equalTo(double a, double b) { return isEqualTo(Double.compare(a, b)); }

	public static boolean unequalTo(double a, double b) { return isUnequalTo(Double.compare(a, b)); }

	public static boolean greaterThan(double a, double b) { return isGreaterThan(Double.compare(a, b)); }

	public static boolean lessThanOrEqualTo(double a, double b) { return isLessThanOrEqualTo(Double.compare(a, b)); }

	public static boolean greaterThanOrEqualTo(double a, double b) { return isGreaterThanOrEqualTo(Double.compare(a, b)); }

	public static boolean lessThan(float a, float b) { return isLessThan(Float.compare(a, b)); }

	public static boolean equalTo(float a, float b) { return isEqualTo(Float.compare(a, b)); }

	public static boolean unequalTo(float a, float b) { return isUnequalTo(Float.compare(a, b)); }

	public static boolean greaterThan(float a, float b) { return isGreaterThan(Float.compare(a, b)); }

	public static boolean lessThanOrEqualTo(float a, float b) { return isLessThanOrEqualTo(Float.compare(a, b)); }

	public static boolean greaterThanOrEqualTo(float a, float b) { return isGreaterThanOrEqualTo(Float.compare(a, b)); }
}
