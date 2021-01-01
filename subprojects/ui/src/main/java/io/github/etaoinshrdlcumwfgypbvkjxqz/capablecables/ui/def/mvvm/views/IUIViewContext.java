package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.IUISubInfrastructureContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.io.def.IInputDevices;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IUIViewContext
		extends IUISubInfrastructureContext {
	@Immutable
	IInputDevices getInputDevices();
}
