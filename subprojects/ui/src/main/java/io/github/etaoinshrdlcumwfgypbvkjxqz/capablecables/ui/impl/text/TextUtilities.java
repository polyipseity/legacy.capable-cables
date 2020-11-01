package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.text;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AffineTransformUtilities;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;

public enum TextUtilities {
	;

	private static final Font DEFAULT_FONT;
	private static final TextLayout EMPTY_TEXT_LAYOUT;

	static {
		DEFAULT_FONT = new Font(null);
		EMPTY_TEXT_LAYOUT = new TextLayout(" ", getDefaultFont(), new FontRenderContext(AffineTransformUtilities.getIdentity(), RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT, RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT));
	}

	public static Font getDefaultFont() {
		return DEFAULT_FONT;
	}

	public static TextLayout getEmptyTextLayout() {
		return EMPTY_TEXT_LAYOUT;
	}
}
