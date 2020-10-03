package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.inputs.IInputDevices;

public class DefaultUIViewContext
		implements IUIViewContext {
	private final IInputDevices inputDevices;

	public DefaultUIViewContext(IInputDevices inputDevices) {
		this.inputDevices = inputDevices;
	}

	@Override
	public IInputDevices getInputDevices() { return inputDevices; }

	@Override
	public DefaultUIViewContext copy() { return new DefaultUIViewContext(getInputDevices()); }
}
