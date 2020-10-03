package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.modifiers;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentCursorHandleProviderModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IUIComponentContext;

import java.util.Optional;

public class NullUIComponentCursorHandleProviderModifier
		implements IUIComponentCursorHandleProviderModifier {
	private static final NullUIComponentCursorHandleProviderModifier INSTANCE = new NullUIComponentCursorHandleProviderModifier();

	protected NullUIComponentCursorHandleProviderModifier() {}

	public static NullUIComponentCursorHandleProviderModifier getInstance() { return INSTANCE; }

	@Override
	public Optional<Long> getCursorHandle(IUIComponentContext context) { return Optional.empty(); }
}
