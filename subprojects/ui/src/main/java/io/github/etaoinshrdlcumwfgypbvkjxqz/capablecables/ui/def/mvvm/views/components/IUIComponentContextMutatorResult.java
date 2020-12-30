package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.modifiers.IUIComponentModifier;

import java.util.List;

public interface IUIComponentContextMutatorResult {
	IUIComponent getComponent();

	List<? extends IUIComponentModifier> getModifiersView();
}
