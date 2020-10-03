package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.ICopyable;

import java.util.List;

public interface IUIComponentContextMutatorResult
		extends ICopyable {
	IUIComponent getComponent();

	List<? extends IUIComponentModifier> getModifiersView();

	@Override
	IUIComponentContextMutatorResult copy();
}
