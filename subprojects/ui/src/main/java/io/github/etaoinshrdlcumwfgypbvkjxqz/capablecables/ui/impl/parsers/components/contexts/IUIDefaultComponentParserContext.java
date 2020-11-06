package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.components.contexts;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;

import java.util.Optional;
import java.util.function.BiConsumer;

public interface IUIDefaultComponentParserContext
		extends IUIAbstractDefaultComponentParserContext<BiConsumer<@Nonnull ? super IUIDefaultComponentParserContext, @Nonnull ?>> {
	Optional<?> getContainer();

	Optional<? extends IUIViewComponent<?, ?>> getView();
}
