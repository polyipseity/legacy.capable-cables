package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.lifecycles.IUIActiveLifecycle;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;

import java.util.function.Consumer;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IUIComponentActiveLifecycleModifier
		extends IUIActiveLifecycle<IUIComponentContext, IUIComponentContext> {
	static void handleComponentModifiers(IUIComponent component,
	                                     Iterable<? extends IUIComponentModifier> modifiers,
	                                     Consumer<@Nonnull ? super IUIComponentActiveLifecycleModifier> action) {
		IUIComponentModifier.handleComponentModifiers(component,
				modifiers,
				IUIComponentActiveLifecycleModifier.class,
				action);
	}
}
