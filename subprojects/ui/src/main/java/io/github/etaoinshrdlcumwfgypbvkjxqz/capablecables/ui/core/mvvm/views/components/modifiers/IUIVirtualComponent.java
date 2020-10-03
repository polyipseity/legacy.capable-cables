package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;

import java.awt.geom.Point2D;

// TODO works differently, cannot have children
public interface IUIVirtualComponent
		extends IUIComponent, IUIComponentModifier {
	@SuppressWarnings("UnstableApiUsage")
	static ImmutableList<IUIVirtualComponent> findVirtualComponents(IUIComponentContext context, IUIComponent component, Point2D point) {
		return component.getModifiersView()
				.stream().sequential()
				.filter(IUIVirtualComponent.class::isInstance)
				.map(IUIVirtualComponent.class::cast)
				.filter(modifier -> modifier.containsPoint(context, point))
				.collect(ImmutableList.toImmutableList());
	}
}
