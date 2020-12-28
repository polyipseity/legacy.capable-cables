package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUISubInfrastructureContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core.IInputDevices;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IUIViewContext
		extends IUISubInfrastructureContext {
	@Immutable
	IInputDevices getInputDevices();
}
