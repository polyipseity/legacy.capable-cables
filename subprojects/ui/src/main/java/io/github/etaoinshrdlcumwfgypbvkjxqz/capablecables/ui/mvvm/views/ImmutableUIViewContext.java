package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.inputs.IInputDevices;

public final class ImmutableUIViewContext
		implements IUIViewContext {
	private final IInputDevices inputDevices;

	public ImmutableUIViewContext(IInputDevices inputDevices) {
		this.inputDevices = inputDevices;
	}

	@Override
	public @Immutable IInputDevices getInputDevices() { return inputDevices; }
}
