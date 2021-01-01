package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.adapters.ui.components.contexts;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponentView;

import java.util.Optional;

public interface IJAXBUIComponentAdapterContext
		extends IJAXBUIComponentBasedAdapterContext {
	Optional<?> getContainer();

	Optional<? extends IUIComponentView<?, ?>> getView();
}
