package $group__.utilities;

import java.util.regex.Pattern;

public class StringUtilities {
	/* MARK empty */;


	// COMMENT from https://stackoverflow.com/a/2282998/9341868
	public static String replaceLast(String string, String regex, String replacement) {
		return Pattern.compile("(.*)" + regex, Pattern.DOTALL)
				.matcher(string)
				.replaceFirst("$1" + replacement);
	}
}
