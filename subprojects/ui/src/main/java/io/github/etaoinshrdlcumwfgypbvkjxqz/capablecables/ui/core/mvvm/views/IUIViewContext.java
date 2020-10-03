package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUISubInfrastructureContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.inputs.IInputDevices;

public interface IUIViewContext
		extends IUISubInfrastructureContext {
	IInputDevices getInputDevices();

	@Override
	IUIViewContext copy();
}
