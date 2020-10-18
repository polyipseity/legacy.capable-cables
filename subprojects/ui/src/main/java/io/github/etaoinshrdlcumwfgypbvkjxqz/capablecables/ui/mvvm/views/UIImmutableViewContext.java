package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.inputs.IInputDevices;

public final class UIImmutableViewContext
		implements IUIViewContext {
	private final IInputDevices inputDevices;

	public UIImmutableViewContext(IInputDevices inputDevices) {
		this.inputDevices = inputDevices;
	}

	@Override
	public @Immutable IInputDevices getInputDevices() { return inputDevices; }
}
