package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.ui.components.contexts;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;

import java.util.Optional;

public interface IJAXBUIComponentAdapterContext
		extends IJAXBUIComponentBasedAdapterContext {
	Optional<?> getContainer();

	Optional<? extends IUIViewComponent<?, ?>> getView();
}
