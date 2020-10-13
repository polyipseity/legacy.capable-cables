package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.components.contexts;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.NonnullBiConsumer;

import java.util.Optional;

public interface IUIDefaultComponentParserContext
		extends IUIAbstractDefaultComponentParserContext<NonnullBiConsumer<? super IUIDefaultComponentParserContext, ?>> {
	Optional<?> getContainer();

	Optional<? extends IUIViewComponent<?, ?>> getView();
}
