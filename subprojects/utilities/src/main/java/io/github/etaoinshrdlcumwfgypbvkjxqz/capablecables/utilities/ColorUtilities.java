package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import java.awt.*;

public enum ColorUtilities {
	;

	private static final Color COLORLESS = new Color(0, 0, 0, 0);

	public static Color getColorless() { return COLORLESS; }

	public static Color ofRGBA(int rgba) {
		return new Color(rgba, true);
	}
}
