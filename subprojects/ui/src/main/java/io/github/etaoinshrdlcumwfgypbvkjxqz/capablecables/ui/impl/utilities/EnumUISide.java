package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities;

import com.google.common.collect.Sets;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;

import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.function.DoubleBinaryOperator;
import java.util.function.ObjDoubleConsumer;
import java.util.function.ToDoubleFunction;

@Immutable
public enum EnumUISide {
	UP {
		@Override
		public EnumUIAxis getAxis() { return EnumUIAxis.Y; }

		@Override
		public Optional<? extends EnumUISide> getOpposite() { return Optional.of(DOWN); }

		@Override
		public boolean isMouseOver(RectangularShape rectangular, Point2D mouse) { return mouse.getY() < rectangular.getY(); }

		@Override
		public ToDoubleFunction<RectangularShape> getGetter() { return RectangularShape::getY; }

		@Override
		public ObjDoubleConsumer<RectangularShape> getSetter() { return (r, i) -> r.setFrameFromDiagonal(r.getX(), i, r.getMaxX(), r.getMaxY()); }

		@Override
		public DoubleBinaryOperator getApplyBorder() { return Double::sum; }
	},
	DOWN {
		@Override
		public EnumUIAxis getAxis() { return EnumUIAxis.Y; }

		@Override
		public Optional<? extends EnumUISide> getOpposite() { return Optional.of(UP); }

		@Override
		public boolean isMouseOver(RectangularShape rectangular, Point2D mouse) { return mouse.getY() >= rectangular.getMaxY(); }

		@Override
		public ToDoubleFunction<RectangularShape> getGetter() { return RectangularShape::getMaxY; }

		@Override
		public ObjDoubleConsumer<RectangularShape> getSetter() { return (r, i) -> r.setFrameFromDiagonal(r.getX(), r.getY(), r.getMaxX(), i); }

		@Override
		public DoubleBinaryOperator getApplyBorder() { return (i, b) -> i - b; }
	},
	LEFT {
		@Override
		public EnumUIAxis getAxis() { return EnumUIAxis.X; }

		@Override
		public Optional<? extends EnumUISide> getOpposite() { return Optional.of(RIGHT); }

		@Override
		public boolean isMouseOver(RectangularShape rectangular, Point2D mouse) { return mouse.getX() < rectangular.getX(); }

		@Override
		public ToDoubleFunction<RectangularShape> getGetter() { return RectangularShape::getX; }

		@Override
		public ObjDoubleConsumer<RectangularShape> getSetter() { return (r, i) -> r.setFrameFromDiagonal(i, r.getY(), r.getMaxX(), r.getMaxY()); }

		@Override
		public DoubleBinaryOperator getApplyBorder() { return Double::sum; }
	},
	RIGHT {
		@Override
		public EnumUIAxis getAxis() { return EnumUIAxis.X; }

		@Override
		public Optional<? extends EnumUISide> getOpposite() { return Optional.of(LEFT); }

		@Override
		public boolean isMouseOver(RectangularShape rectangular, Point2D mouse) { return mouse.getX() >= rectangular.getMaxX(); }

		@Override
		public ToDoubleFunction<RectangularShape> getGetter() { return RectangularShape::getMaxX; }

		@Override
		public ObjDoubleConsumer<RectangularShape> getSetter() { return (r, i) -> r.setFrameFromDiagonal(r.getX(), r.getY(), i, r.getMaxY()); }

		@Override
		public DoubleBinaryOperator getApplyBorder() { return (i, b) -> i - b; }
	},
	HORIZONTAL {
		@Override
		public EnumUIAxis getAxis() { return EnumUIAxis.X; }

		@Override
		public Optional<? extends EnumUISide> getOpposite() { return Optional.empty(); }

		@Override
		public ToDoubleFunction<RectangularShape> getGetter() { return RectangularShape::getCenterX; }

		@Override
		public ObjDoubleConsumer<RectangularShape> getSetter() { return (r, i) -> r.setFrameFromCenter(i, r.getCenterY(), r.getX() + i - r.getCenterX(), r.getY()); }

		@Override
		public DoubleBinaryOperator getApplyBorder() { return (i, b) -> i; }
	},
	VERTICAL {
		@Override
		public EnumUIAxis getAxis() { return EnumUIAxis.Y; }

		@Override
		public Optional<? extends EnumUISide> getOpposite() { return Optional.empty(); }

		@Override
		public ToDoubleFunction<RectangularShape> getGetter() { return RectangularShape::getCenterY; }

		@Override
		public ObjDoubleConsumer<RectangularShape> getSetter() { return (r, i) -> r.setFrameFromCenter(i, r.getCenterY(), r.getX() + i - r.getCenterX(), r.getY()); }

		@Override
		public DoubleBinaryOperator getApplyBorder() { return (i, b) -> i; }
	};

	@SuppressWarnings("UnstableApiUsage")
	@Immutable
	public static Set<EnumUISide> getSidesMouseOver(RectangularShape rectangular, Point2D mouse) {
		return Arrays.stream(EnumUISide.values()).unordered()
				.filter(side -> side.isMouseOver(rectangular, mouse))
				.collect(Sets.toImmutableEnumSet());
	}

	public boolean isMouseOver(RectangularShape rectangular, Point2D mouse) { return false; }

	public abstract EnumUIAxis getAxis();

	public abstract Optional<? extends EnumUISide> getOpposite();

	public abstract ToDoubleFunction<RectangularShape> getGetter();

	public abstract ObjDoubleConsumer<RectangularShape> getSetter();

	public abstract DoubleBinaryOperator getApplyBorder();
}
