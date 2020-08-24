package $group__.ui.core.mvvm.adapters;

import $group__.ui.core.mvvm.IUIInfrastructure;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IUIAdapter<I extends IUIInfrastructure<?, ?, ?>> {
	I getInfrastructure();
}
