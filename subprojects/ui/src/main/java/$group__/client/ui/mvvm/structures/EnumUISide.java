package $group__.client.ui.mvvm.structures;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.EnumSet;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public enum EnumUISide {
	UP {
		@Override
		public EnumUIAxis getAxis() { return EnumUIAxis.Y; }

		@Override
		public EnumUISide getOpposite() { return DOWN; }

		@Override
		public boolean isMouseOver(Rectangle2D rectangle, Point2D mouse) { return mouse.getY() < rectangle.getY(); }

		@Override
		public Function<Rectangle2D, Double> getGetter() { return RectangularShape::getY; }

		@Override
		public BiConsumer<Rectangle2D, Double> getSetter() { return (r, i) -> r.setFrameFromDiagonal(r.getX(), i, r.getMaxX(), r.getMaxY()); }

		@Override
		public BiFunction<Double, Double, Double> getApplyBorder() { return Double::sum; }
	},
	DOWN {
		@Override
		public EnumUIAxis getAxis() { return EnumUIAxis.Y; }

		@Override
		public EnumUISide getOpposite() { return UP; }

		@Override
		public boolean isMouseOver(Rectangle2D rectangle, Point2D mouse) { return mouse.getY() >= rectangle.getMaxY(); }

		@Override
		public Function<Rectangle2D, Double> getGetter() { return RectangularShape::getMaxY; }

		@Override
		public BiConsumer<Rectangle2D, Double> getSetter() { return (r, i) -> r.setFrameFromDiagonal(r.getX(), r.getY(), r.getMaxX(), i); }

		@Override
		public BiFunction<Double, Double, Double> getApplyBorder() { return (i, b) -> i - b; }
	},
	LEFT {
		@Override
		public EnumUIAxis getAxis() { return EnumUIAxis.X; }

		@Override
		public EnumUISide getOpposite() { return RIGHT; }

		@Override
		public boolean isMouseOver(Rectangle2D rectangle, Point2D mouse) { return mouse.getX() < rectangle.getX(); }

		@Override
		public Function<Rectangle2D, Double> getGetter() { return RectangularShape::getX; }

		@Override
		public BiConsumer<Rectangle2D, Double> getSetter() { return (r, i) -> r.setFrameFromDiagonal(i, r.getY(), r.getMaxX(), r.getMaxY()); }

		@Override
		public BiFunction<Double, Double, Double> getApplyBorder() { return Double::sum; }
	},
	RIGHT {
		@Override
		public EnumUIAxis getAxis() { return EnumUIAxis.X; }

		@Override
		public EnumUISide getOpposite() { return LEFT; }

		@Override
		public boolean isMouseOver(Rectangle2D rectangle, Point2D mouse) { return mouse.getX() >= rectangle.getMaxX(); }

		@Override
		public Function<Rectangle2D, Double> getGetter() { return RectangularShape::getMaxX; }

		@Override
		public BiConsumer<Rectangle2D, Double> getSetter() { return (r, i) -> r.setFrameFromDiagonal(r.getX(), r.getY(), i, r.getMaxY()); }

		@Override
		public BiFunction<Double, Double, Double> getApplyBorder() { return (i, b) -> i - b; }
	},
	HORIZONTAL {
		@Override
		public EnumUIAxis getAxis() { return EnumUIAxis.X; }

		@Override
		public EnumUISide getOpposite() { return VERTICAL; }

		@Override
		public Function<Rectangle2D, Double> getGetter() { return RectangularShape::getCenterX; }

		@Override
		public BiConsumer<Rectangle2D, Double> getSetter() { return (r, i) -> r.setFrameFromCenter(i, r.getCenterY(), r.getX() + i - r.getCenterX(), r.getY()); }

		@Override
		public BiFunction<Double, Double, Double> getApplyBorder() { return (i, b) -> i; }
	},
	VERTICAL {
		@Override
		public EnumUIAxis getAxis() { return EnumUIAxis.Y; }

		@Override
		public EnumUISide getOpposite() { return HORIZONTAL; }

		@Override
		public Function<Rectangle2D, Double> getGetter() { return RectangularShape::getCenterY; }

		@Override
		public BiConsumer<Rectangle2D, Double> getSetter() { return (r, i) -> r.setFrameFromCenter(i, r.getCenterY(), r.getX() + i - r.getCenterX(), r.getY()); }

		@Override
		public BiFunction<Double, Double, Double> getApplyBorder() { return (i, b) -> i; }
	};

	public static EnumSet<EnumUISide> getSidesMouseOver(Rectangle2D rectangle, Point2D mouse) {
		EnumSet<EnumUISide> sides = EnumSet.noneOf(EnumUISide.class);
		for (EnumUISide side : EnumUISide.values()) if (side.isMouseOver(rectangle, mouse)) sides.add(side);
		return sides;
	}

	public boolean isMouseOver(Rectangle2D rectangle, Point2D mouse) { return false; }

	public abstract EnumUIAxis getAxis();

	public abstract EnumUISide getOpposite();

	public abstract Function<Rectangle2D, Double> getGetter();

	public abstract BiConsumer<Rectangle2D, Double> getSetter();

	public abstract BiFunction<Double, Double, Double> getApplyBorder();
}
