package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities;

import com.google.common.collect.Sets;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;

import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;
import java.util.Arrays;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;

@Immutable
public enum EnumUISide {
	// COMMENT 'setFrameFromDiagonal' has a useful side effect of ensuring that width and height are always non-negative
	UP {
		@Override
		public EnumUIAxis getAxis() { return EnumUIAxis.Y; }

		@Override
		public Optional<? extends EnumUISide> getOpposite() { return Optional.of(DOWN); }

		@Override
		public boolean isMouseOver(RectangularShape rectangular, Point2D mouse) { return mouse.getY() < rectangular.getY(); }

		@Override
		public double getValue(RectangularShape shape) {
			return shape.getY();
		}

		@Override
		public void setValue(RectangularShape shape, double value) {
			shape.setFrameFromDiagonal(shape.getX(), value, shape.getMaxX(), shape.getMaxY());
		}

		@Override
		public OptionalDouble inwardsBy(double value) {
			return OptionalDouble.of(value);
		}

		@Override
		public OptionalDouble outwardsBy(double value) {
			return OptionalDouble.of(-value);
		}

		@Override
		public EnumUISideType getType() {
			return EnumUISideType.LOCATION;
		}
	},
	DOWN {
		@Override
		public EnumUIAxis getAxis() { return EnumUIAxis.Y; }

		@Override
		public Optional<? extends EnumUISide> getOpposite() { return Optional.of(UP); }

		@Override
		public boolean isMouseOver(RectangularShape rectangular, Point2D mouse) { return mouse.getY() >= rectangular.getMaxY(); }

		@Override
		public double getValue(RectangularShape shape) {
			return shape.getMaxY();
		}

		@Override
		public void setValue(RectangularShape shape, double value) {
			shape.setFrameFromDiagonal(shape.getX(), shape.getY(), shape.getMaxX(), value);
		}

		@Override
		public OptionalDouble inwardsBy(double value) {
			return OptionalDouble.of(-value);
		}

		@Override
		public OptionalDouble outwardsBy(double value) {
			return OptionalDouble.of(value);
		}

		@Override
		public EnumUISideType getType() {
			return EnumUISideType.SIZE;
		}
	},
	LEFT {
		@Override
		public EnumUIAxis getAxis() { return EnumUIAxis.X; }

		@Override
		public Optional<? extends EnumUISide> getOpposite() { return Optional.of(RIGHT); }

		@Override
		public boolean isMouseOver(RectangularShape rectangular, Point2D mouse) { return mouse.getX() < rectangular.getX(); }

		@Override
		public double getValue(RectangularShape shape) {
			return shape.getX();
		}

		@Override
		public void setValue(RectangularShape shape, double value) {
			shape.setFrameFromDiagonal(value, shape.getY(), shape.getMaxX(), shape.getMaxY());
		}

		@Override
		public OptionalDouble inwardsBy(double value) {
			return OptionalDouble.of(value);
		}

		@Override
		public OptionalDouble outwardsBy(double value) {
			return OptionalDouble.of(-value);
		}

		@Override
		public EnumUISideType getType() {
			return EnumUISideType.LOCATION;
		}
	},
	RIGHT {
		@Override
		public EnumUIAxis getAxis() { return EnumUIAxis.X; }

		@Override
		public Optional<? extends EnumUISide> getOpposite() { return Optional.of(LEFT); }

		@Override
		public boolean isMouseOver(RectangularShape rectangular, Point2D mouse) { return mouse.getX() >= rectangular.getMaxX(); }

		@Override
		public double getValue(RectangularShape shape) {
			return shape.getMaxX();
		}

		@Override
		public void setValue(RectangularShape shape, double value) {
			shape.setFrameFromDiagonal(shape.getX(), shape.getY(), value, shape.getMaxY());
		}

		@Override
		public OptionalDouble inwardsBy(double value) {
			return OptionalDouble.of(-value);
		}

		@Override
		public OptionalDouble outwardsBy(double value) {
			return OptionalDouble.of(value);
		}

		@Override
		public EnumUISideType getType() {
			return EnumUISideType.SIZE;
		}
	},
	HORIZONTAL {
		@Override
		public boolean isMouseOver(RectangularShape rectangular, Point2D mouse) {
			return false;
		}

		@Override
		public EnumUIAxis getAxis() { return EnumUIAxis.X; }

		@Override
		public Optional<? extends EnumUISide> getOpposite() { return Optional.empty(); }

		@Override
		public double getValue(RectangularShape shape) {
			return shape.getCenterX();
		}

		@Override
		public void setValue(RectangularShape shape, double value) {
			shape.setFrameFromCenter(value, shape.getCenterY(), shape.getX() + value - shape.getCenterX(), shape.getY());
		}

		@Override
		public OptionalDouble inwardsBy(double value) {
			return OptionalDouble.empty();
		}

		@Override
		public OptionalDouble outwardsBy(double value) {
			return OptionalDouble.empty();
		}

		@Override
		public EnumUISideType getType() {
			return EnumUISideType.CENTER;
		}
	},
	VERTICAL {
		@Override
		public boolean isMouseOver(RectangularShape rectangular, Point2D mouse) {
			return false;
		}

		@Override
		public EnumUIAxis getAxis() { return EnumUIAxis.Y; }

		@Override
		public Optional<? extends EnumUISide> getOpposite() { return Optional.empty(); }

		@Override
		public double getValue(RectangularShape shape) {
			return shape.getCenterY();
		}

		@Override
		public void setValue(RectangularShape shape, double value) {
			shape.setFrameFromCenter(value, shape.getCenterY(), shape.getX() + value - shape.getCenterX(), shape.getY());
		}

		@Override
		public OptionalDouble inwardsBy(double value) {
			return OptionalDouble.empty();
		}

		@Override
		public OptionalDouble outwardsBy(double value) {
			return OptionalDouble.empty();
		}

		@Override
		public EnumUISideType getType() {
			return EnumUISideType.CENTER;
		}
	};

	@SuppressWarnings("UnstableApiUsage")
	@Immutable
	public static Set<EnumUISide> getSidesMouseOver(RectangularShape rectangular, Point2D mouse) {
		return Arrays.stream(EnumUISide.values()).unordered()
				.filter(side -> side.isMouseOver(rectangular, mouse))
				.collect(Sets.toImmutableEnumSet());
	}

	private static final @Immutable Set<EnumUISide> EDGES = Sets.immutableEnumSet(UP, DOWN, LEFT, RIGHT);

	public static @Immutable Set<EnumUISide> getEdges() {
		return EDGES;
	}

	public abstract boolean isMouseOver(RectangularShape rectangular, Point2D mouse);

	public abstract EnumUIAxis getAxis();

	public abstract Optional<? extends EnumUISide> getOpposite();

	public abstract double getValue(RectangularShape shape);

	public abstract void setValue(RectangularShape shape, double value);

	public abstract OptionalDouble inwardsBy(double value);

	public abstract OptionalDouble outwardsBy(double value);

	public abstract EnumUISideType getType();
}
