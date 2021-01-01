package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.io.def.IInputDevices;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.io.def.IOutputDevices;

public final class UIImmutableViewContext
		implements IUIViewContext {
	private final IInputDevices inputDevices;
	private final IOutputDevices outputDevices;

	private UIImmutableViewContext(IInputDevices inputDevices, IOutputDevices outputDevices) {
		this.inputDevices = inputDevices;
		this.outputDevices = outputDevices;
	}

	public static UIImmutableViewContext of(IInputDevices inputDevices, IOutputDevices outputDevices) {
		return new UIImmutableViewContext(inputDevices, outputDevices);
	}

	@Override
	public @Immutable IInputDevices getInputDevices() {
		return inputDevices;
	}

	@Override
	public @Immutable IOutputDevices getOutputDevices() {
		return outputDevices;
	}
}
