package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.paths;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.IUIViewCoordinator;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponentContext;

import java.awt.geom.Point2D;

public interface IUIComponentPathResolver
		extends IUIViewCoordinator {
	IUIComponentPathResolverResult resolvePath(IUIComponentContext componentContext, Point2D point);

	IUIComponentPathResolverResult resolveDirectChild(IUIComponentContext componentContext, Point2D point);

	IUIComponentPathResolverResult getResult(IUIComponentContext componentContext, Point2D point);
}
