package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.modifiers;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponentContext;

@FunctionalInterface
public interface IUIComponentUpdateModifier {
	static void handleComponentModifiers(IUIComponent component,
	                                     Iterable<? extends IUIComponentModifier> modifiers,
	                                     IUIComponentContext context) {
		IUIComponentModifier.handleComponentModifiers(component,
				modifiers,
				IUIComponentUpdateModifier.class,
				modifier -> modifier.update(context));
	}

	void update(IUIComponentContext context);
}
