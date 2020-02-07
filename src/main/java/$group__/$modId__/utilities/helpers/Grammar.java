package $group__.$modId__.utilities.helpers;

import $group__.$modId__.utilities.helpers.specific.StringsExtension;

public enum Grammar {
	/* MARK empty */;


	/* SECTION static methods */

	public static String appendSuffixIfPlural(long n, String suf) { return n == 1 ? StringsExtension.STRING_EMPTY : suf; }
}
