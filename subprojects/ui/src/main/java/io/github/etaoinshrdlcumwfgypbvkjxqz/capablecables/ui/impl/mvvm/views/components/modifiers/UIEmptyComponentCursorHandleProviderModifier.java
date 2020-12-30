package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.modifiers;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.modifiers.IUIComponentCursorHandleProviderModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.def.ICursor;

import java.util.Optional;

@Immutable
public final class UIEmptyComponentCursorHandleProviderModifier
		implements IUIComponentCursorHandleProviderModifier {
	@Override
	public Optional<? extends ICursor> getCursorHandle(IUIComponentContext context) {
		return Optional.empty();
	}
}
