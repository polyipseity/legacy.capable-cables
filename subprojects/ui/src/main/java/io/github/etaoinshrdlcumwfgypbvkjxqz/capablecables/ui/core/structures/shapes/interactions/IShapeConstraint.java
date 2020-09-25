package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.interactions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures.shapes.interactions.ShapeConstraint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.ICloneable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.ICopyable;

import java.awt.geom.RectangularShape;
import java.util.Optional;

public interface IShapeConstraint
		extends ICopyable, ICloneable {
	IShapeConstraint CONSTRAINT_NULL = new ShapeConstraint(null, null, null, null, null, null, null, null);

	default <R extends RectangularShape> R constrain(RectangularShape source, R destination) {
		final double[]
				x = new double[]{source.getX()},
				y = new double[]{source.getY()},
				width = new double[]{source.getWidth()},
				height = new double[]{source.getHeight()};
		x[0] = getMinX().map(t -> Math.max(x[0], t)).orElseGet(() -> x[0]);
		y[0] = getMinY().map(t -> Math.max(y[0], t)).orElseGet(() -> y[0]);
		x[0] = getMaxX().map(t -> Math.min(x[0], t)).orElseGet(() -> x[0]);
		y[0] = getMaxY().map(t -> Math.min(y[0], t)).orElseGet(() -> y[0]);
		width[0] = getMinWidth().map(t -> Math.max(width[0], t)).orElseGet(() -> width[0]);
		height[0] = getMinHeight().map(t -> Math.max(height[0], t)).orElseGet(() -> height[0]);
		width[0] = getMaxWidth().map(t -> Math.min(width[0], t)).orElseGet(() -> width[0]);
		height[0] = getMaxHeight().map(t -> Math.min(height[0], t)).orElseGet(() -> height[0]);
		destination.setFrame(x[0], y[0], width[0], height[0]);
		return destination;
	}

	Optional<? extends Double> getMinX();

	Optional<? extends Double> getMinY();

	Optional<? extends Double> getMaxX();

	Optional<? extends Double> getMaxY();

	Optional<? extends Double> getMinWidth();

	Optional<? extends Double> getMinHeight();

	Optional<? extends Double> getMaxWidth();

	Optional<? extends Double> getMaxHeight();

	IShapeConstraint createIntersection(IShapeConstraint constraint);

	@Override
	IShapeConstraint copy();

	@SuppressWarnings("override")
	IShapeConstraint clone() throws CloneNotSupportedException;
}
