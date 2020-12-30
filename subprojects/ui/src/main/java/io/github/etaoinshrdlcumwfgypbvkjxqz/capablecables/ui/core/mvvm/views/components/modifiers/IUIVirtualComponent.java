package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;

import java.awt.geom.Point2D;
import java.util.Spliterator;

// TODO works differently, cannot have children
public interface IUIVirtualComponent
		extends IUIComponent, IUIComponentModifier {
	static Spliterator<IUIVirtualComponent> findVirtualComponents(IUIComponentContext context, IUIComponent component, Point2D point) {
		return component.getModifiersView()
				.stream()
				.filter(IUIVirtualComponent.class::isInstance)
				.map(IUIVirtualComponent.class::cast)
				.filter(modifier -> modifier.containsPoint(context, point))
				.spliterator();
	}
}
