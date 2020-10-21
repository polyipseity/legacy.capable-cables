package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.paths;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;

import java.awt.geom.Point2D;

public interface IUIComponentPathResolver {
	IUIComponentPathResolverResult resolvePath(IUIComponentContext componentContext, Point2D point);

	IUIComponentPathResolverResult resolveDirectChild(IUIComponentContext componentContext, Point2D point);

	IUIComponentPathResolverResult getResult(IUIComponentContext componentContext, Point2D point);
}