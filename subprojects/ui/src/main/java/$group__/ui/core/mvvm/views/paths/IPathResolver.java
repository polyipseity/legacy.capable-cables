package $group__.ui.core.mvvm.views.paths;

import java.awt.geom.Point2D;

public interface IPathResolver<T> {
	static <T> T getTargetAtPoint(IPathResolver<? extends T> self, Point2D point) { return self.resolvePath(point, true).getPathEnd(); }

	boolean addVirtualElement(T element,
	                          T virtualElement);

	boolean removeVirtualElement(T element,
	                             T virtualElement);

	boolean moveVirtualElementToTop(T element,
	                                T virtualElement);

	IPath<T> resolvePath(Point2D point, boolean virtual);
}
