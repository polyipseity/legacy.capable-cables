package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.components.contexts;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.theming.UILambdaTheme;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.NonnullBiConsumer;

import java.util.Map;

public final class UIImmutableDefaultComponentThemeParserContext
		extends UIAbstractImmutableAbstractDefaultComponentParserContext<NonnullBiConsumer<? super IUIDefaultComponentThemeParserContext, ?>>
		implements IUIDefaultComponentThemeParserContext {
	private final UILambdaTheme.Builder builder;

	public UIImmutableDefaultComponentThemeParserContext(Map<? extends String, ? extends Class<?>> aliases,
	                                                     Map<? extends Class<?>, ? extends NonnullBiConsumer<? super IUIDefaultComponentThemeParserContext, ?>> handlers,
	                                                     UILambdaTheme.Builder builder) {
		super(aliases, handlers);
		this.builder = builder;
	}

	@Override
	public UILambdaTheme.Builder getBuilder() { return builder; }
}
