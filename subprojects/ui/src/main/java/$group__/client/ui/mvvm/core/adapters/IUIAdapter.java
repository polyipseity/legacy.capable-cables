package $group__.client.ui.mvvm.core.adapters;

import $group__.client.ui.mvvm.core.IUIInfrastructure;

@FunctionalInterface
public interface IUIAdapter<I extends IUIInfrastructure<?, ?, ?>> {
	I getInfrastructure();
}
