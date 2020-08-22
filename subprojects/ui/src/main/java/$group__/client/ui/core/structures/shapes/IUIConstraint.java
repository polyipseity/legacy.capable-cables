package $group__.client.ui.core.structures.shapes;

import $group__.client.ui.structures.shapes.interactions.UIConstraint;
import $group__.utilities.interfaces.ICloneable;
import $group__.utilities.interfaces.ICopyable;

import java.awt.geom.Rectangle2D;
import java.util.Optional;

public interface IUIConstraint
		extends ICopyable, ICloneable {
	IUIConstraint CONSTRAINT_NULL = new UIConstraint(null, null, null, null, null, null, null, null);

	default void constrain(Rectangle2D rectangle) {
		final double[]
				x = new double[]{rectangle.getX()},
				y = new double[]{rectangle.getY()},
				width = new double[]{rectangle.getWidth()},
				height = new double[]{rectangle.getHeight()};
		x[0] = getMinX().map(t -> Math.max(x[0], t)).orElseGet(() -> x[0]);
		y[0] = getMinY().map(t -> Math.max(y[0], t)).orElseGet(() -> y[0]);
		x[0] = getMaxX().map(t -> Math.min(x[0], t)).orElseGet(() -> x[0]);
		y[0] = getMaxY().map(t -> Math.min(y[0], t)).orElseGet(() -> y[0]);
		width[0] = getMinWidth().map(t -> Math.max(width[0], t)).orElseGet(() -> width[0]);
		height[0] = getMinHeight().map(t -> Math.max(height[0], t)).orElseGet(() -> height[0]);
		width[0] = getMaxWidth().map(t -> Math.min(width[0], t)).orElseGet(() -> width[0]);
		height[0] = getMaxHeight().map(t -> Math.min(height[0], t)).orElseGet(() -> height[0]);
		rectangle.setRect(x[0], y[0], width[0], height[0]);
	}

	Optional<Double> getMinX();

	Optional<Double> getMinY();

	Optional<Double> getMaxX();

	Optional<Double> getMaxY();

	Optional<Double> getMinWidth();

	Optional<Double> getMinHeight();

	Optional<Double> getMaxWidth();

	Optional<Double> getMaxHeight();

	IUIConstraint createIntersection(IUIConstraint constraint);

	@Override
	IUIConstraint copy();

	@SuppressWarnings("override")
	IUIConstraint clone() throws CloneNotSupportedException;
}
