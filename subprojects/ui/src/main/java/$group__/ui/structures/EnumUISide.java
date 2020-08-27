package $group__.ui.structures;

import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public enum EnumUISide {
	UP {
		@Override
		public EnumUIAxis getAxis() { return EnumUIAxis.Y; }

		@Override
		public Optional<EnumUISide> getOpposite() { return Optional.of(DOWN); }

		@Override
		public boolean isMouseOver(RectangularShape rectangular, Point2D mouse) { return mouse.getY() < rectangular.getY(); }

		@Override
		public Function<RectangularShape, Double> getGetter() { return RectangularShape::getY; }

		@Override
		public BiConsumer<RectangularShape, Double> getSetter() { return (r, i) -> r.setFrameFromDiagonal(r.getX(), i, r.getMaxX(), r.getMaxY()); }

		@Override
		public BiFunction<Double, Double, Double> getApplyBorder() { return Double::sum; }
	},
	DOWN {
		@Override
		public EnumUIAxis getAxis() { return EnumUIAxis.Y; }

		@Override
		public Optional<EnumUISide> getOpposite() { return Optional.of(UP); }

		@Override
		public boolean isMouseOver(RectangularShape rectangular, Point2D mouse) { return mouse.getY() >= rectangular.getMaxY(); }

		@Override
		public Function<RectangularShape, Double> getGetter() { return RectangularShape::getMaxY; }

		@Override
		public BiConsumer<RectangularShape, Double> getSetter() { return (r, i) -> r.setFrameFromDiagonal(r.getX(), r.getY(), r.getMaxX(), i); }

		@Override
		public BiFunction<Double, Double, Double> getApplyBorder() { return (i, b) -> i - b; }
	},
	LEFT {
		@Override
		public EnumUIAxis getAxis() { return EnumUIAxis.X; }

		@Override
		public Optional<EnumUISide> getOpposite() { return Optional.of(RIGHT); }

		@Override
		public boolean isMouseOver(RectangularShape rectangular, Point2D mouse) { return mouse.getX() < rectangular.getX(); }

		@Override
		public Function<RectangularShape, Double> getGetter() { return RectangularShape::getX; }

		@Override
		public BiConsumer<RectangularShape, Double> getSetter() { return (r, i) -> r.setFrameFromDiagonal(i, r.getY(), r.getMaxX(), r.getMaxY()); }

		@Override
		public BiFunction<Double, Double, Double> getApplyBorder() { return Double::sum; }
	},
	RIGHT {
		@Override
		public EnumUIAxis getAxis() { return EnumUIAxis.X; }

		@Override
		public Optional<EnumUISide> getOpposite() { return Optional.of(LEFT); }

		@Override
		public boolean isMouseOver(RectangularShape rectangular, Point2D mouse) { return mouse.getX() >= rectangular.getMaxX(); }

		@Override
		public Function<RectangularShape, Double> getGetter() { return RectangularShape::getMaxX; }

		@Override
		public BiConsumer<RectangularShape, Double> getSetter() { return (r, i) -> r.setFrameFromDiagonal(r.getX(), r.getY(), i, r.getMaxY()); }

		@Override
		public BiFunction<Double, Double, Double> getApplyBorder() { return (i, b) -> i - b; }
	},
	HORIZONTAL {
		@Override
		public EnumUIAxis getAxis() { return EnumUIAxis.X; }

		@Override
		public Optional<EnumUISide> getOpposite() { return Optional.empty(); }

		@Override
		public Function<RectangularShape, Double> getGetter() { return RectangularShape::getCenterX; }

		@Override
		public BiConsumer<RectangularShape, Double> getSetter() { return (r, i) -> r.setFrameFromCenter(i, r.getCenterY(), r.getX() + i - r.getCenterX(), r.getY()); }

		@Override
		public BiFunction<Double, Double, Double> getApplyBorder() { return (i, b) -> i; }
	},
	VERTICAL {
		@Override
		public EnumUIAxis getAxis() { return EnumUIAxis.Y; }

		@Override
		public Optional<EnumUISide> getOpposite() { return Optional.empty(); }

		@Override
		public Function<RectangularShape, Double> getGetter() { return RectangularShape::getCenterY; }

		@Override
		public BiConsumer<RectangularShape, Double> getSetter() { return (r, i) -> r.setFrameFromCenter(i, r.getCenterY(), r.getX() + i - r.getCenterX(), r.getY()); }

		@Override
		public BiFunction<Double, Double, Double> getApplyBorder() { return (i, b) -> i; }
	};

	public static Set<EnumUISide> getSidesMouseOver(RectangularShape rectangular, Point2D mouse) {
		Set<EnumUISide> sides = EnumSet.noneOf(EnumUISide.class);
		for (EnumUISide side : EnumUISide.values())
			if (side.isMouseOver(rectangular, mouse))
				sides.add(side);
		return sides;
	}

	public boolean isMouseOver(RectangularShape rectangular, Point2D mouse) { return false; }

	public abstract EnumUIAxis getAxis();

	public abstract Optional<EnumUISide> getOpposite();

	public abstract Function<RectangularShape, Double> getGetter();

	public abstract BiConsumer<RectangularShape, Double> getSetter();

	public abstract BiFunction<Double, Double, Double> getApplyBorder();
}
