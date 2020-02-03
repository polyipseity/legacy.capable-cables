package $group__.$modId__.utilities.helpers;

import java.awt.*;
import java.util.regex.Matcher;

import static $group__.$modId__.utilities.helpers.Patterns.HASH_PATTERN;

public enum Colors {
	/* MARK empty */;


	/* SECTION static variables */

	@SuppressWarnings("unused")
	public static final Color
			WHITE = Color.WHITE,
			LIGHT_GRAY = Color.LIGHT_GRAY,
			GRAY = Color.GRAY,
			DARK_GRAY = Color.DARK_GRAY,
			BLACK = Color.BLACK,
			RED = Color.RED,
			PINK = Color.PINK,
			ORANGE = Color.ORANGE,
			YELLOW = Color.YELLOW,
			GREEN = Color.GREEN,
			MAGENTA = Color.MAGENTA,
			CYAN = Color.CYAN,
			BLUE = Color.BLUE,
			COLORLESS = newColor("#00000000");


	/* SECTION static methods */

	public static Color newColor(String s) {
		return new Color(Integer.parseInt(HASH_PATTERN.matcher(s).replaceAll(Matcher.quoteReplacement(StringsExtension.EMPTY)), 16), true);
	}
}
