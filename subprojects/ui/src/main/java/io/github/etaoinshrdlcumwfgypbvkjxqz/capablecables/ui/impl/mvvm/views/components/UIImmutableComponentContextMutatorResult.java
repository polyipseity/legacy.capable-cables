package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContextMutatorResult;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentModifier;

import java.util.List;

public final class UIImmutableComponentContextMutatorResult
		implements IUIComponentContextMutatorResult {
	private final IUIComponent component;
	private final List<IUIComponentModifier> modifiers;

	private UIImmutableComponentContextMutatorResult(IUIComponent component, Iterable<? extends IUIComponentModifier> modifiers) {
		this.component = component;
		this.modifiers = ImmutableList.copyOf(modifiers);
	}

	public static UIImmutableComponentContextMutatorResult of(IUIComponent component) {
		return new UIImmutableComponentContextMutatorResult(component, component.getModifiersView());
	}

	private static UIImmutableComponentContextMutatorResult of(IUIComponent component, Iterable<? extends IUIComponentModifier> modifiers) {
		return new UIImmutableComponentContextMutatorResult(component, modifiers);
	}

	@Override
	public IUIComponent getComponent() { return component; }

	@Override
	public List<? extends IUIComponentModifier> getModifiersView() {
		return ImmutableList.copyOf(getModifiers());
	}

	protected List<IUIComponentModifier> getModifiers() { return modifiers; }
}
