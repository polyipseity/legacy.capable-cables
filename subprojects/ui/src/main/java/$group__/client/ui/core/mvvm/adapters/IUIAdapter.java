package $group__.client.ui.core.mvvm.adapters;

import $group__.client.ui.core.mvvm.IUIInfrastructure;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IUIAdapter<I extends IUIInfrastructure<?, ?, ?>> {
	I getInfrastructure();
}
