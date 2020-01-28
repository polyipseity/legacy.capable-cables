package $group__.$modId__.utilities.helpers;

import org.apache.commons.lang3.StringUtils;

import java.awt.*;

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

	public static Color newColor(String s) { return new Color(Integer.parseInt(s.replace("#", StringUtils.EMPTY), 16), true); }
}
