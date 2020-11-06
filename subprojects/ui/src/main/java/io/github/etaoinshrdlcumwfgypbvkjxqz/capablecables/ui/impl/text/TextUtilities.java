package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.text;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AffineTransformUtilities;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.CharacterIterator;
import java.util.function.Consumer;

public enum TextUtilities {
	;

	private static final Font DEFAULT_FONT;
	private static final FontRenderContext DEFAULT_FONT_RENDER_CONTEXT;
	private static final TextLayout EMPTY_TEXT_LAYOUT;

	static {
		DEFAULT_FONT = new Font(null);
		DEFAULT_FONT_RENDER_CONTEXT = new FontRenderContext(AffineTransformUtilities.getIdentity(), RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT, RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT);
		EMPTY_TEXT_LAYOUT = new TextLayout(" ", // COMMENT the string cannot be empty
				getDefaultFont(), getDefaultFontRenderContext());
	}

	public static Font getDefaultFont() {
		return DEFAULT_FONT;
	}

	public static FontRenderContext getDefaultFontRenderContext() {
		return DEFAULT_FONT_RENDER_CONTEXT;
	}

	public static TextLayout getEmptyTextLayout() {
		return EMPTY_TEXT_LAYOUT;
	}

	public static String next(CharacterIterator characterIterator, int length) {
		char[] result = new char[length]; // COMMENT doing it directly
		for (int i = 0; i < length; ++i, characterIterator.next())
			result[i] = characterIterator.current();
		return String.valueOf(result);
	}

	public static <T extends AttributedCharacterIterator> void forEachRun(T attributedCharacterIterator, Consumer<@Nonnull ? super T> action) {
		while (attributedCharacterIterator.getRunLimit() < attributedCharacterIterator.getEndIndex() /* COMMENT equal means the last run */) {
			int runLimit = attributedCharacterIterator.getRunLimit(); // COMMENT in case the action changes the index
			action.accept(attributedCharacterIterator);
			attributedCharacterIterator.setIndex(runLimit);
		}
	}
}
