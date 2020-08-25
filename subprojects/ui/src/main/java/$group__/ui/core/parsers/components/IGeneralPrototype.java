package $group__.ui.core.parsers.components;

import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.components.IUIComponentManager;

import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IGeneralPrototype {
	void construct(List<Consumer<? super IUIComponentManager<?>>> queue, IUIComponent container)
			throws Throwable;
}
