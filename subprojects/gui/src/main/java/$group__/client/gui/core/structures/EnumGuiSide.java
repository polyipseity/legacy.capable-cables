package $group__.client.gui.core.structures;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.EnumSet;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public enum EnumGuiSide {
	UP {
		@Override
		public EnumGuiAxis getAxis() { return EnumGuiAxis.Y; }

		@Override
		public EnumGuiSide getOpposite() { return DOWN; }

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
		public EnumGuiAxis getAxis() { return EnumGuiAxis.Y; }

		@Override
		public EnumGuiSide getOpposite() { return UP; }

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
		public EnumGuiAxis getAxis() { return EnumGuiAxis.X; }

		@Override
		public EnumGuiSide getOpposite() { return RIGHT; }

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
		public EnumGuiAxis getAxis() { return EnumGuiAxis.X; }

		@Override
		public EnumGuiSide getOpposite() { return LEFT; }

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
		public EnumGuiAxis getAxis() { return EnumGuiAxis.X; }

		@Override
		public EnumGuiSide getOpposite() { return VERTICAL; }

		@Override
		public Function<Rectangle2D, Double> getGetter() { return RectangularShape::getCenterX; }

		@Override
		public BiConsumer<Rectangle2D, Double> getSetter() { return (r, i) -> r.setFrameFromCenter(i, r.getCenterY(), r.getX() + i - r.getCenterX(), r.getY()); }

		@Override
		public BiFunction<Double, Double, Double> getApplyBorder() { return (i, b) -> i; }
	},
	VERTICAL {
		@Override
		public EnumGuiAxis getAxis() { return EnumGuiAxis.Y; }

		@Override
		public EnumGuiSide getOpposite() { return HORIZONTAL; }

		@Override
		public Function<Rectangle2D, Double> getGetter() { return RectangularShape::getCenterY; }

		@Override
		public BiConsumer<Rectangle2D, Double> getSetter() { return (r, i) -> r.setFrameFromCenter(i, r.getCenterY(), r.getX() + i - r.getCenterX(), r.getY()); }

		@Override
		public BiFunction<Double, Double, Double> getApplyBorder() { return (i, b) -> i; }
	};

	public static EnumSet<EnumGuiSide> getSidesMouseOver(Rectangle2D rectangle, Point2D mouse) {
		EnumSet<EnumGuiSide> sides = EnumSet.noneOf(EnumGuiSide.class);
		for (EnumGuiSide side : EnumGuiSide.values()) if (side.isMouseOver(rectangle, mouse)) sides.add(side);
		return sides;
	}

	public boolean isMouseOver(Rectangle2D rectangle, Point2D mouse) { return false; }

	public abstract EnumGuiAxis getAxis();

	public abstract EnumGuiSide getOpposite();

	public abstract Function<Rectangle2D, Double> getGetter();

	public abstract BiConsumer<Rectangle2D, Double> getSetter();

	public abstract BiFunction<Double, Double, Double> getApplyBorder();
}
