package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.text;

import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;

public interface ITextLayout {
	IAttributedText getText();

	FontRenderContext getFontRenderContext();

	TextLayout compile();
}
