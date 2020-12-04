package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.graphics.impl.UIObjectUtilities;

import javax.annotation.concurrent.Immutable;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressUnboxing;

@Immutable
public enum EnumUIAxis {
	X {
		@Override
		public EnumUIAxis getOpposite() {
			return Y;
		}

		@Override
		public double getCoordinate(Point2D point) { return point.getX(); }

		@Override
		public void setCoordinate(Point2D point, double value) {
			point.setLocation(value, point.getY());
		}

		@Override
		public double getSize(Dimension2D dimension) {
			return dimension.getWidth();
		}

		@Override
		public void setSize(Dimension2D dimension, double value) {
			UIObjectUtilities.acceptDimension(dimension, (width, height) ->
					dimension.setSize(value, suppressUnboxing(height)));
		}

		@Override
		public double getSize(RectangularShape rectangularShape) {
			return rectangularShape.getWidth();
		}

		@Override
		public void setSize(RectangularShape rectangularShape, double value) {
			UIObjectUtilities.acceptRectangularShape(rectangularShape, (x, y, width, height) ->
					rectangularShape.setFrame(suppressUnboxing(x), suppressUnboxing(y),
							value, suppressUnboxing(height)));
		}
	},
	Y {
		@Override
		public EnumUIAxis getOpposite() {
			return X;
		}

		@Override
		public double getCoordinate(Point2D point) { return point.getY(); }

		@Override
		public void setCoordinate(Point2D point, double value) {
			point.setLocation(point.getX(), value);
		}

		@Override
		public double getSize(Dimension2D dimension) {
			return dimension.getHeight();
		}

		@Override
		public void setSize(Dimension2D dimension, double value) {
			UIObjectUtilities.acceptDimension(dimension, (width, height) ->
					dimension.setSize(suppressUnboxing(width), value));
		}

		@Override
		public double getSize(RectangularShape rectangularShape) {
			return rectangularShape.getHeight();
		}

		@Override
		public void setSize(RectangularShape rectangularShape, double value) {
			UIObjectUtilities.acceptRectangularShape(rectangularShape, (x, y, width, height) ->
					rectangularShape.setFrame(suppressUnboxing(x), suppressUnboxing(y),
							suppressUnboxing(width), value));
		}
	};

	public abstract EnumUIAxis getOpposite();

	public abstract double getCoordinate(Point2D point);

	public abstract void setCoordinate(Point2D point, double value);

	public abstract double getSize(Dimension2D dimension);

	public abstract void setSize(Dimension2D dimension, double value);

	public abstract double getSize(RectangularShape rectangularShape);

	public abstract void setSize(RectangularShape rectangularShape, double value);
}
