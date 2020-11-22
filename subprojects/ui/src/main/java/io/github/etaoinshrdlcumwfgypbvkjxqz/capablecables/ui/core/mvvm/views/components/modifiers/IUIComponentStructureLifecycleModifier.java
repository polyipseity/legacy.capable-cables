package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.AlwaysNull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUIStructureLifecycleContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.lifecycles.IUIStructureLifecycle;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;

import java.util.function.Consumer;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IUIComponentStructureLifecycleModifier
		extends IUIStructureLifecycle<IUIStructureLifecycleContext, @AlwaysNull @Nullable Void> {
	static void handleComponentModifiers(IUIComponent component,
	                                     Iterable<? extends IUIComponentModifier> modifiers,
	                                     Consumer<@Nonnull ? super IUIComponentStructureLifecycleModifier> action) {
		IUIComponentModifier.handleComponentModifiers(component,
				modifiers,
				IUIComponentStructureLifecycleModifier.class,
				action);
	}
}
