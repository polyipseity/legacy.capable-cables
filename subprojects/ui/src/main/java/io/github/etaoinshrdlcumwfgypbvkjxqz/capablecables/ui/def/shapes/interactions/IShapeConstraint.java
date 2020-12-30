package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.shapes.interactions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.ICloneable;

import java.awt.geom.RectangularShape;
import java.util.OptionalDouble;

public interface IShapeConstraint
		extends ICloneable {
	@Override
	IShapeConstraint clone();

	default <R extends RectangularShape> R constrain(RectangularShape source, R destination) {
		final double[]
				x = new double[]{source.getX()},
				y = new double[]{source.getY()},
				width = new double[]{source.getWidth()},
				height = new double[]{source.getHeight()};
		getMinX().ifPresent(value -> x[0] = Math.max(x[0], value));
		getMinY().ifPresent(value -> y[0] = Math.max(y[0], value));
		getMaxX().ifPresent(value -> x[0] = Math.min(x[0], value));
		getMaxY().ifPresent(value -> y[0] = Math.min(y[0], value));
		getMinWidth().ifPresent(value -> width[0] = Math.max(width[0], value));
		getMinHeight().ifPresent(value -> height[0] = Math.max(height[0], value));
		getMaxWidth().ifPresent(value -> width[0] = Math.min(width[0], value));
		getMaxHeight().ifPresent(value -> height[0] = Math.min(height[0], value));
		destination.setFrame(x[0], y[0], width[0], height[0]);
		return destination;
	}

	OptionalDouble getMinX();

	OptionalDouble getMinY();

	OptionalDouble getMaxX();

	OptionalDouble getMaxY();

	OptionalDouble getMinWidth();

	OptionalDouble getMinHeight();

	OptionalDouble getMaxWidth();

	OptionalDouble getMaxHeight();

	IShapeConstraint createIntersection(IShapeConstraint constraint);
}
