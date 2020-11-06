package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.text;

import com.google.common.base.Suppliers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.text.IAttributedText;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.text.ITextLayout;

import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.util.function.Supplier;

public final class ImmutableTextLayout
		implements ITextLayout {
	private final IAttributedText text;
	private final FontRenderContext fontRenderContext;
	private final Supplier<@Nonnull TextLayout> compiler;

	private ImmutableTextLayout(IAttributedText text, FontRenderContext fontRenderContext) {
		this.text = text;
		this.fontRenderContext = fontRenderContext;
		this.compiler = Suppliers.memoize(() -> new TextLayout(getText().compile().getIterator(), getFontRenderContext()));
	}

	@Override
	public IAttributedText getText() {
		return text;
	}

	@Override
	public FontRenderContext getFontRenderContext() {
		return fontRenderContext;
	}

	@Override
	public TextLayout compile() {
		return getCompiler().get();
	}

	protected Supplier<@Nonnull ? extends TextLayout> getCompiler() {
		return compiler;
	}

	public static ImmutableTextLayout of(IAttributedText text, FontRenderContext fontRenderContext) {
		return new ImmutableTextLayout(text, fontRenderContext);
	}
}
