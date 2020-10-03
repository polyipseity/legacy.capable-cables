package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContainer;

import java.awt.geom.AffineTransform;

@FunctionalInterface
public interface IUIComponentTransformChildrenModifier {
	void transformChildren(AffineTransform transform);

	enum StaticHolder {
		;

		public static void handleComponentModifiers(IUIComponentContainer component,
		                                            Iterable<? extends IUIComponentModifier> modifiers,
		                                            AffineTransform transform) {
			IUIComponentModifier.StaticHolder.handleComponentModifiers(component,
					modifiers,
					IUIComponentTransformChildrenModifier.class,
					modifier -> modifier.transformChildren(transform));
		}
	}
}
