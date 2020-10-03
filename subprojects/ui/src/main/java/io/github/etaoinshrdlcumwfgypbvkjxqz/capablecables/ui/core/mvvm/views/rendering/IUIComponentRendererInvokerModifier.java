package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IUIComponentContext;

@FunctionalInterface
public interface IUIComponentRendererInvokerModifier {
	void invokeRenderer(IUIComponentContext context);

	enum StaticHolder {
		;

		public static <R extends IUIRenderer<?>> void handleComponentModifiers(IUIComponentRendererInvokerModifier component,
		                                                                       Iterable<? extends IUIComponentModifier> modifiers,
		                                                                       IUIComponentContext context) {
			IUIComponentModifier.StaticHolder.handleComponentModifiers(component,
					modifiers,
					IUIComponentRendererInvokerModifier.class,
					modifier -> modifier.invokeRenderer(context));
		}
	}
}
