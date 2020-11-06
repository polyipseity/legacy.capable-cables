package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;

import java.util.function.BiConsumer;

public interface IUIComponentLifecycleModifier {
	static void handleComponentModifiers(IUIComponent component,
	                                     Iterable<? extends IUIComponentModifier> modifiers,
	                                     IUIComponentContext context,
	                                     BiConsumer<@Nonnull ? super IUIComponentLifecycleModifier, @Nonnull ? super IUIComponentContext> action) {
		IUIComponentModifier.handleComponentModifiers(component,
				modifiers,
				IUIComponentLifecycleModifier.class,
				modifier -> action.accept(modifier, context));
	}

	void initialize(IUIComponentContext context);

	void removed(IUIComponentContext context);
}
