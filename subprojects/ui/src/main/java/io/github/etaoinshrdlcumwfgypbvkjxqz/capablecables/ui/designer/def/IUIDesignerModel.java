package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.designer.def;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.construction.IUIComponentArguments;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.models.IUIModel;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.def.IRegistry;

import java.util.function.Consumer;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IUIDesignerModel
		extends IUIModel {
	IRegistry<Class<? extends IUIComponent>, ? extends Consumer<? super IUIComponentArguments>> getComponentRegistry();
}
