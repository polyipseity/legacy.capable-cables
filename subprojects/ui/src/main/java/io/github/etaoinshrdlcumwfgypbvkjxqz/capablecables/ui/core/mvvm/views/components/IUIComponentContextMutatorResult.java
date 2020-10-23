package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentModifier;

import java.util.List;

public interface IUIComponentContextMutatorResult {
	IUIComponent getComponent();

	List<? extends IUIComponentModifier> getModifiersView();
}
