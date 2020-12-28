package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.adapters;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUIInfrastructure;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IUIAdapter<I extends IUIInfrastructure<?, ?, ?>> {
	I getInfrastructure();
}
