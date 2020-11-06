package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.components.contexts;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.theming.UILambdaTheme;

import java.util.Map;
import java.util.function.BiConsumer;

public final class UIImmutableDefaultComponentThemeParserContext
		extends UIAbstractImmutableAbstractDefaultComponentParserContext<BiConsumer<@Nonnull ? super IUIDefaultComponentThemeParserContext, @Nonnull ?>>
		implements IUIDefaultComponentThemeParserContext {
	private final UILambdaTheme.Builder builder;

	public UIImmutableDefaultComponentThemeParserContext(Map<? extends String, ? extends Class<?>> aliases,
	                                                     Map<? extends Class<?>, ? extends BiConsumer<@Nonnull ? super IUIDefaultComponentThemeParserContext, @Nonnull ?>> handlers,
	                                                     UILambdaTheme.Builder builder) {
		super(aliases, handlers);
		this.builder = builder;
	}

	@Override
	public UILambdaTheme.Builder getBuilder() { return builder; }
}
