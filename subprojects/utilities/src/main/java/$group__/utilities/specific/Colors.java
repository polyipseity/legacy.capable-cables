package $group__.utilities.specific;

import java.awt.*;
import java.util.regex.Matcher;

import static $group__.utilities.RadixUtilities.RADIX_HEX;
import static $group__.utilities.specific.Patterns.HASH_PATTERN;

public enum Colors {
	;

	public static final int S_RGB_COMPONENT_MAX = 255,
			S_RGB_ALPHA_LSB_BIT = 24,
			S_RGB_RED_LSB_BIT = 16,
			S_RGB_GREEN_LSB_BIT = 8,
			S_RGB_BLUE_LSB_BIT = 0;

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

	public static Color newColor(String s) {
		return new Color(Integer.parseInt(HASH_PATTERN.matcher(s).replaceAll(Matcher.quoteReplacement("")), RADIX_HEX), true);
	}
}
