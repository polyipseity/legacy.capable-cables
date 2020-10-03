package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.paths;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IUIComponentPathResolverResult;

import java.awt.geom.Point2D;

public interface IUIComponentPathResolver {
	IUIComponentPathResolverResult resolvePath(IUIComponentContext context, Point2D point);

	IUIComponentPathResolverResult resolveDirectChild(IUIComponentContext context, Point2D point);

	IUIComponentPathResolverResult getResult(IUIComponentContext context, Point2D point);
}
