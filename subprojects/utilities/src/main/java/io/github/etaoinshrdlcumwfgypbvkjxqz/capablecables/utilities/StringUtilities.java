package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import java.util.regex.Pattern;

public enum StringUtilities {
	;

	// COMMENT from https://stackoverflow.com/a/2282998/9341868
	public static String replaceLast(String string, String regex, String replacement) {
		return Pattern.compile("(?<pre>.*)" + regex, Pattern.DOTALL)
				.matcher(string)
				.replaceFirst("${pre}" + replacement);
	}
}
