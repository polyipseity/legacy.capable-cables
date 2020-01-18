package $group__.$modId__.utilities.helpers;

public enum Grammar {
	/* MARK empty */ ;


	/* SECTION static methods */

	public static String appendSuffixIfPlural(long n, String suf) { return n == 1 ? "" : suf; }
}
