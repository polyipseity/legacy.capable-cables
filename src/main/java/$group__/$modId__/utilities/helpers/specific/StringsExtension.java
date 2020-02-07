package $group__.$modId__.utilities.helpers.specific;

public class StringsExtension {
	/* MARK empty */;


	/* SECTION static variables */

	public static final String STRING_EMPTY = "";

	/* SECTION static methods */


	// COMMENT from https://stackoverflow.com/a/2282998/9341868
	public static String replaceLast(String string, String regex, String replacement) { return string.replaceFirst("(?s)(.*)" + regex, "$1" + replacement); }
}
