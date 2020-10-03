package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContextMutatorResult;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentModifier;

import java.util.List;

public final class ImmutableUIComponentContextMutatorResult
		implements IUIComponentContextMutatorResult {
	private final IUIComponent component;
	private final List<IUIComponentModifier> modifiers;

	private ImmutableUIComponentContextMutatorResult(IUIComponent component, Iterable<? extends IUIComponentModifier> modifiers) {
		this.component = component;
		this.modifiers = ImmutableList.copyOf(modifiers);
	}

	public static ImmutableUIComponentContextMutatorResult of(IUIComponent component) {
		return new ImmutableUIComponentContextMutatorResult(component, component.getModifiersView());
	}

	private static ImmutableUIComponentContextMutatorResult of(IUIComponent component, Iterable<? extends IUIComponentModifier> modifiers) {
		return new ImmutableUIComponentContextMutatorResult(component, modifiers);
	}


	@Override
	public IUIComponent getComponent() { return component; }

	@Override
	public List<? extends IUIComponentModifier> getModifiersView() {
		return ImmutableList.copyOf(getModifiers());
	}

	@Override
	public ImmutableUIComponentContextMutatorResult copy() {
		return of(getComponent(), getModifiers());
	}

	protected List<IUIComponentModifier> getModifiers() { return modifiers; }
}
