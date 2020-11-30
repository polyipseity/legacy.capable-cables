package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.modifiers;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentCursorHandleProviderModifier;

import java.util.OptionalLong;

@Immutable
public final class UIEmptyComponentCursorHandleProviderModifier
		implements IUIComponentCursorHandleProviderModifier {
	@Override
	public OptionalLong getCursorHandle(IUIComponentContext context) {
		return OptionalLong.empty();
	}
}
