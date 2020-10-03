package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IUIComponentContext;

import java.util.function.BiConsumer;

public interface IUIComponentLifecycleModifier {
	void initialize(IUIComponentContext context);

	void removed(IUIComponentContext context);

	enum StaticHolder {
		;

		public static void handleComponentModifiers(IUIComponent component,
		                                            Iterable<? extends IUIComponentModifier> modifiers,
		                                            IUIComponentContext context,
		                                            BiConsumer<? super IUIComponentLifecycleModifier, ? super IUIComponentContext> action) {
			IUIComponentModifier.StaticHolder.handleComponentModifiers(component,
					modifiers,
					IUIComponentLifecycleModifier.class,
					modifier -> action.accept(modifier, context));
		}
	}
}
