package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;

import java.awt.geom.AffineTransform;

@FunctionalInterface
public interface IUIComponentTransformChildrenModifier {
	static void handleComponentModifiers(IUIComponent component,
	                                     Iterable<? extends IUIComponentModifier> modifiers,
	                                     AffineTransform transform) {
		IUIComponentModifier.handleComponentModifiers(component,
				modifiers,
				IUIComponentTransformChildrenModifier.class,
				modifier -> modifier.transformChildren(transform));
	}

	void transformChildren(AffineTransform transform);
}
