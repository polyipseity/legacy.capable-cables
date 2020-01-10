package etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers;

import java.awt.*;

public enum Colors {
	;
	public static final Color
			COLORLESS = newColor("#00000000"),
			WHITE = Color.WHITE;


	public static Color newColor(String s) { return new Color(Integer.parseInt(s.replace("#", ""), 16), true); }

	public static Color copyColor(Color c) { return new Color(c.getColorSpace(), c.getColorComponents(null), c.getAlpha() / 255F); }
}
