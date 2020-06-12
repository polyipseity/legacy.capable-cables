package $group__.utilities.helpers.specific;

@SuppressWarnings("SpellCheckingInspection")
public enum Comparables {
	/* MARK empty */;


	/* SECTION static methods */

	public static <T> boolean lessThan(Comparable<T> a, T b) { return a.compareTo(b) < 0; }

	public static <T> boolean equalTo(Comparable<T> a, T b) { return a.compareTo(b) == 0; }

	public static <T> boolean greaterThan(Comparable<T> a, T b) { return a.compareTo(b) > 0; }

	public static <T> boolean lessThanOrEqualTo(Comparable<T> a, T b) { return a.compareTo(b) <= 0; }

	public static <T> boolean greaterThanOrEqualTo(Comparable<T> a, T b) { return a.compareTo(b) >= 0; }
}
