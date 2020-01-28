package $group__.$modId__.utilities.helpers;

import org.apache.commons.lang3.StringUtils;

public enum Grammar {
	/* MARK empty */;


	/* SECTION static methods */

	public static String appendSuffixIfPlural(long n, String suf) { return n == 1 ? StringUtils.EMPTY : suf; }
}
