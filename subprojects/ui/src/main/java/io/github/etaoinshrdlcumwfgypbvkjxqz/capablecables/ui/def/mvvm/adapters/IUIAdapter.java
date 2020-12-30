package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.adapters;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.IUIInfrastructure;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IUIAdapter<I extends IUIInfrastructure<?, ?, ?>> {
	I getInfrastructure();
}
