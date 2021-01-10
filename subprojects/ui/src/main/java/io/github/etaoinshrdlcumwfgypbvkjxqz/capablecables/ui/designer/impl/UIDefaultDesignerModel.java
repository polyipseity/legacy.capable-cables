package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.designer.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction.IUIComponentArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.designer.def.IUIDesignerModel;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.designer.impl.data.UIComponentRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.def.IRegistry;

import java.util.function.Consumer;

public class UIDefaultDesignerModel
		implements IUIDesignerModel {
	private final IRegistry<Class<? extends IUIComponent>, ? extends Consumer<? super IUIComponentArguments>> componentRegistry;

	protected UIDefaultDesignerModel(IRegistry<Class<? extends IUIComponent>, ? extends Consumer<? super IUIComponentArguments>> componentRegistry) {
		this.componentRegistry = componentRegistry;
	}

	public static UIDefaultDesignerModel of(IRegistry<Class<? extends IUIComponent>, ? extends Consumer<? super IUIComponentArguments>> componentRegistry) {
		return new UIDefaultDesignerModel(componentRegistry);
	}

	public static UIDefaultDesignerModel ofDefault() {
		return new UIDefaultDesignerModel(UIComponentRegistry.getInstance());
	}

	@Override
	public IRegistry<Class<? extends IUIComponent>, ? extends Consumer<? super IUIComponentArguments>> getComponentRegistry() {
		return componentRegistry;
	}
}
