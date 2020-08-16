package $group__.client.ui.mvvm.core.views.components.paths;

import $group__.client.ui.mvvm.core.views.components.IUIComponent;
import $group__.client.ui.mvvm.core.views.paths.IUINodePathResolver;

import java.awt.geom.Point2D;

public interface IUIComponentPathResolver<T extends IUIComponent>
		extends IUINodePathResolver<T> {
	@Override
	IUIComponentPath resolvePath(Point2D point, boolean virtual);
}
