package $group__.client.gui.structures;

import $group__.client.gui.components.GuiComponent;
import $group__.client.gui.traits.IGuiReRectangleHandler;
import $group__.utilities.helpers.Recursions;
import $group__.utilities.helpers.specific.Maps;
import $group__.utilities.helpers.specific.ThrowableUtilities.BecauseOf;
import $group__.utilities.helpers.specific.ThrowableUtilities.Try;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.TriConsumer;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public final class GuiAnchors implements Cloneable {
	private static final Logger LOGGER = LogManager.getLogger();

	private final ConcurrentMap<AnchorSide, Anchor> anchors = Maps.MAP_MAKER_SINGLE_THREAD.makeMap();
	public double border;

	public GuiAnchors(double border) { this.border = border; }

	public GuiAnchors() { this(0); }

	public ImmutableMap<AnchorSide, Anchor> getAnchorsView() { return ImmutableMap.copyOf(anchors); }

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
			anchor.onAdded();
		}
	}

	public void remove(AnchorSide... sides) {
		for (AnchorSide side : sides) {
			@Nullable Anchor anchor = anchors.remove(side);
			if (anchor != null) anchor.onRemoved();
		}
	}

	public boolean isEmpty() { return anchors.isEmpty(); }

	public void clear() { remove(AnchorSide.values()); }

	public Anchor[] getAnchorsToMatch(GuiComponent from, GuiComponent to) {
		return new Anchor[]{
				new Anchor(from, to, AnchorSide.UP, AnchorSide.UP),
				new Anchor(from, to, AnchorSide.DOWN, AnchorSide.DOWN),
				new Anchor(from, to, AnchorSide.LEFT, AnchorSide.LEFT),
				new Anchor(from, to, AnchorSide.RIGHT, AnchorSide.RIGHT)
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

	@SuppressWarnings("Convert2MethodRef")
	@Override
	public Object clone() {
		Try.run(() -> super.clone(), LOGGER);
		GuiAnchors ret = new GuiAnchors(border);
		anchors.forEach((side, anchor) -> ret.add((Anchor) anchor.clone()));
		return ret;
	}

	@Immutable
	public final class Anchor implements Cloneable, TriConsumer<IGuiReRectangleHandler, GuiComponent, Rectangle2D> {
		public final WeakReference<GuiComponent> from;
		public final WeakReference<GuiComponent> to;
		public final AnchorSide fromSide;
		public final AnchorSide toSide;
		public final double border;

		public Anchor(GuiComponent from, GuiComponent to, AnchorSide fromSide, AnchorSide toSide) { this(from, to, fromSide, toSide, 0); }

		public Anchor(GuiComponent from, GuiComponent to, AnchorSide fromSide, AnchorSide toSide, double border) {
			this.from = new WeakReference<>(from);
			this.to = new WeakReference<>(to);
			this.fromSide = fromSide;
			this.toSide = toSide;
			this.border = border;

			checkNoCycles();
		}

		public boolean reRectangle(IGuiReRectangleHandler handler, GuiComponent invoker, Rectangle2D old) {
			@Nullable GuiComponent
					fromCopy = from.get(),
					toCopy = to.get();
			if (fromCopy == null || toCopy == null) return false;
			Rectangle2D rectangle = fromCopy.getRectangleView();
			fromSide.getSetter().accept(rectangle, fromSide.getApplyBorder().apply(toSide.getGetter().apply(toCopy.getRectangleView()), GuiAnchors.this.border + border));
			fromCopy.setRectangle(handler, invoker, rectangle);
			return true;
		}

		@Override
		public void accept(IGuiReRectangleHandler handler, GuiComponent invoker, Rectangle2D old) {
			if (!reRectangle(handler, invoker, old)) {
				@Nullable GuiComponent toCopy = to.get();
				if (toCopy != null)
					toCopy.listeners.reRectangle.remove(this);
				remove(fromSide);
			}
		}

		private void onAdded() {
			@Nullable GuiComponent toCopy = to.get();
			if (toCopy != null)
				toCopy.listeners.reRectangle.add(this);
		}

		private void onRemoved() {
			@Nullable GuiComponent toCopy = to.get();
			if (toCopy != null)
				toCopy.listeners.reRectangle.remove(this);
		}

		private void checkNoCycles() {
			Recursions.<Anchor, Void>recurseAsDepthFirstLoop(
					this,
					anchor -> null,
					anchor -> {
						@Nullable GuiComponent toCopy = to.get();
						return toCopy == null ? ImmutableList.of() : toCopy.anchors.getAnchorsView().values();
					},
					anchor -> {
						throw new IllegalStateException("Anchor cycle from '" + anchor.to.get() + " to " + anchor.from.get());
					},
					null
			);
		}

		@SuppressWarnings("Convert2MethodRef")
		@Override
		public Object clone() { return Try.call(() -> super.clone(), LOGGER); }
	}
}
