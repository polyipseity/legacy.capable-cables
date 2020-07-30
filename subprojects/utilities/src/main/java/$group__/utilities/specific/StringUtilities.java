package $group__.utilities.specific;

public class StringUtilities {
	/* MARK empty */;


	// COMMENT from https://stackoverflow.com/a/2282998/9341868
	public static String replaceLast(String string, String regex, String replacement) {
		return string.replaceFirst(
				"(?s)(.*)" + regex, "$1" + replacement);
	}
}
