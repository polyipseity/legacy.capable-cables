package $group__.client.gui.structures;

import $group__.client.gui.components.GuiComponent;
import $group__.utilities.helpers.specific.Maps;
import $group__.utilities.helpers.specific.ThrowableUtilities.BecauseOf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public final class GuiAnchors implements Consumer<Rectangle2D> {
	protected final ConcurrentMap<AnchorSide, Anchor> anchors = Maps.MAP_MAKER_SINGLE_THREAD.makeMap();
	public double border;

	public GuiAnchors(double border) { this.border = border; }

	public GuiAnchors() { this(0); }

	@Override
	public void accept(Rectangle2D from) { anchors.values().removeIf(anchor -> !anchor.apply(from)); }

	public void add(Anchor... anchors) {
		for (Anchor anchor : anchors) {
			switch (anchor.fromSide) {
				case UP:
				case DOWN:
					remove(AnchorSide.VERTICAL);
					break;
				case VERTICAL:
					remove(AnchorSide.UP);
					remove(AnchorSide.DOWN);
					break;
				case LEFT:
				case RIGHT:
					remove(AnchorSide.HORIZONTAL);
					break;
				case HORIZONTAL:
					remove(AnchorSide.LEFT);
					remove(AnchorSide.RIGHT);
					break;
				default:
					throw BecauseOf.unexpected();
			}
			this.anchors.put(anchor.fromSide, anchor);
		}
	}

	public void remove(AnchorSide... sides) { for (AnchorSide side : sides) anchors.remove(side); }

	public Anchor[] getAnchorsToMatch(GuiComponent to) {
		return new Anchor[]{
				new Anchor(to, AnchorSide.UP, AnchorSide.UP),
				new Anchor(to, AnchorSide.DOWN, AnchorSide.DOWN),
				new Anchor(to, AnchorSide.LEFT, AnchorSide.LEFT),
				new Anchor(to, AnchorSide.RIGHT, AnchorSide.RIGHT)
		};
	}

	public enum AnchorSide {
		UP {
			@Override
			public AnchorSide getOpposite() { return DOWN; }

			@Override
			public Function<Rectangle2D, Double> getGetter() { return RectangularShape::getY; }

			@Override
			public BiConsumer<Rectangle2D, Double> getSetter() { return (r, i) -> r.setFrameFromDiagonal(r.getX(), i, r.getMaxX(), r.getMaxY()); }

			@Override
			public BiFunction<Double, Double, Double> getApplyBorder() { return Double::sum; }
		},
		DOWN {
			@Override
			public AnchorSide getOpposite() { return UP; }

			@Override
			public Function<Rectangle2D, Double> getGetter() { return RectangularShape::getMaxY; }

			@Override
			public BiConsumer<Rectangle2D, Double> getSetter() { return (r, i) -> r.setFrameFromDiagonal(r.getX(), r.getY(), r.getMaxX(), i); }

			@Override
			public BiFunction<Double, Double, Double> getApplyBorder() { return (i, b) -> i - b; }
		},
		LEFT {
			@Override
			public AnchorSide getOpposite() { return RIGHT; }

			@Override
			public Function<Rectangle2D, Double> getGetter() { return RectangularShape::getX; }

			@Override
			public BiConsumer<Rectangle2D, Double> getSetter() { return (r, i) -> r.setFrameFromDiagonal(i, r.getY(), r.getMaxX(), r.getMaxY()); }

			@Override
			public BiFunction<Double, Double, Double> getApplyBorder() { return Double::sum; }
		},
		RIGHT {
			@Override
			public AnchorSide getOpposite() { return LEFT; }

			@Override
			public Function<Rectangle2D, Double> getGetter() { return RectangularShape::getMaxX; }

			@Override
			public BiConsumer<Rectangle2D, Double> getSetter() { return (r, i) -> r.setFrameFromDiagonal(r.getX(), r.getY(), i, r.getMaxY()); }

			@Override
			public BiFunction<Double, Double, Double> getApplyBorder() { return (i, b) -> i - b; }
		},
		HORIZONTAL {
			@Override
			public AnchorSide getOpposite() { return VERTICAL; }

			@Override
			public Function<Rectangle2D, Double> getGetter() { return RectangularShape::getCenterX; }

			@Override
			public BiConsumer<Rectangle2D, Double> getSetter() { return (r, i) -> r.setFrameFromCenter(i, r.getCenterY(), r.getX() + i - r.getCenterX(), r.getY()); }

			@Override
			public BiFunction<Double, Double, Double> getApplyBorder() { return (i, b) -> i; }
		},
		VERTICAL {
			@Override
			public AnchorSide getOpposite() { return HORIZONTAL; }

			@Override
			public Function<Rectangle2D, Double> getGetter() { return RectangularShape::getCenterY; }

			@Override
			public BiConsumer<Rectangle2D, Double> getSetter() { return (r, i) -> r.setFrameFromCenter(i, r.getCenterY(), r.getX() + i - r.getCenterX(), r.getY()); }

			@Override
			public BiFunction<Double, Double, Double> getApplyBorder() { return (i, b) -> i; }
		};

		public abstract AnchorSide getOpposite();

		public abstract Function<Rectangle2D, Double> getGetter();

		public abstract BiConsumer<Rectangle2D, Double> getSetter();

		public abstract BiFunction<Double, Double, Double> getApplyBorder();
	}

	@Immutable
	public final class Anchor implements Function<Rectangle2D, Boolean> {
		public final WeakReference<GuiComponent> to;
		public final AnchorSide fromSide;
		public final AnchorSide toSide;
		public final double border;

		public Anchor(GuiComponent to, AnchorSide fromSide, AnchorSide toSide) { this(to, fromSide, toSide, 0); }

		public Anchor(GuiComponent to, AnchorSide fromSide, AnchorSide toSide, double border) {
			this.to = new WeakReference<>(to);
			this.fromSide = fromSide;
			this.toSide = toSide;
			this.border = border;
		}

		@Override
		public Boolean apply(Rectangle2D from) {
			@Nullable GuiComponent toCopy = to.get();
			if (toCopy == null) return false;
			fromSide.getSetter().accept(from, fromSide.getApplyBorder().apply(toSide.getGetter().apply(toCopy.getRectangleView()), GuiAnchors.this.border + border));
			return true;
		}
	}
}
