package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentModifier;

@FunctionalInterface
public interface IUIComponentRendererInvokerModifier {
	static <R extends IUIRenderer<?>> void handleComponentModifiers(IUIComponentRendererInvokerModifier component,
	                                                                Iterable<? extends IUIComponentModifier> modifiers,
	                                                                IUIComponentContext context) {
		IUIComponentModifier.handleComponentModifiers(component,
				modifiers,
				IUIComponentRendererInvokerModifier.class,
				modifier -> modifier.invokeRenderer(context));
	}

	void invokeRenderer(IUIComponentContext context);
}
