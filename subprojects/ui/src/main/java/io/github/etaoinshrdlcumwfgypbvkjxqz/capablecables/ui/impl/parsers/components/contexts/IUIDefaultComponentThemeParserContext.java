package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.components.contexts;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.theming.UILambdaTheme;

import java.util.function.BiConsumer;

public interface IUIDefaultComponentThemeParserContext
		extends IUIAbstractDefaultComponentParserContext<BiConsumer<@Nonnull ? super IUIDefaultComponentThemeParserContext, @Nonnull ?>> {
	UILambdaTheme.Builder getBuilder();
}
