package $group__.client.gui.structures;

import $group__.client.gui.components.GuiComponent;
import $group__.client.gui.traits.handlers.IGuiReshapeHandler;
import $group__.utilities.Recursions;
import $group__.utilities.specific.Maps;
import $group__.utilities.specific.ThrowableUtilities.BecauseOf;
import $group__.utilities.specific.ThrowableUtilities.Try;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.TriConsumer;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentMap;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public final class GuiAnchors implements Cloneable {
	private static final Logger LOGGER = LogManager.getLogger();

	private final ConcurrentMap<EnumGuiSide, Anchor> anchors = Maps.MAP_MAKER_SINGLE_THREAD.makeMap();
	public double border;

	public GuiAnchors(double border) { this.border = border; }

	public GuiAnchors() { this(0); }

	public ImmutableMap<EnumGuiSide, Anchor> getAnchorsView() { return ImmutableMap.copyOf(anchors); }

	public void add(Anchor... anchors) {
		for (Anchor anchor : anchors) {
			switch (anchor.fromSide) {
				case UP:
				case DOWN:
					remove(EnumGuiSide.VERTICAL);
					break;
				case VERTICAL:
					remove(EnumGuiSide.UP);
					remove(EnumGuiSide.DOWN);
					break;
				case LEFT:
				case RIGHT:
					remove(EnumGuiSide.HORIZONTAL);
					break;
				case HORIZONTAL:
					remove(EnumGuiSide.LEFT);
					remove(EnumGuiSide.RIGHT);
					break;
				default:
					throw BecauseOf.unexpected();
			}
			this.anchors.put(anchor.fromSide, anchor);
			anchor.onAdded();
		}
	}

	public void remove(EnumGuiSide... sides) {
		for (EnumGuiSide side : sides) {
			@Nullable Anchor anchor = anchors.remove(side);
			if (anchor != null) anchor.onRemoved();
		}
	}

	public boolean isEmpty() { return anchors.isEmpty(); }

	public void clear() { remove(EnumGuiSide.values()); }

	public Anchor[] getAnchorsToMatch(GuiComponent from, GuiComponent to) {
		return new Anchor[]{
				new Anchor(from, to, EnumGuiSide.UP, EnumGuiSide.UP),
				new Anchor(from, to, EnumGuiSide.DOWN, EnumGuiSide.DOWN),
				new Anchor(from, to, EnumGuiSide.LEFT, EnumGuiSide.LEFT),
				new Anchor(from, to, EnumGuiSide.RIGHT, EnumGuiSide.RIGHT)
		};
	}

	@SuppressWarnings("Convert2MethodRef")
	@Override
	public Object clone() {
		Try.run(() -> super.clone(), LOGGER);
		GuiAnchors ret = new GuiAnchors(border);
		anchors.forEach((side, anchor) -> ret.add((Anchor) anchor.clone()));
		return ret;
	}

	@OnlyIn(CLIENT)
	@Immutable
	public final class Anchor implements Cloneable, TriConsumer<IGuiReshapeHandler, GuiComponent, Shape> {
		public final WeakReference<GuiComponent> from;
		public final WeakReference<GuiComponent> to;
		public final EnumGuiSide fromSide;
		public final EnumGuiSide toSide;
		public final double border;

		public Anchor(GuiComponent from, GuiComponent to, EnumGuiSide fromSide, EnumGuiSide toSide) { this(from, to, fromSide, toSide, 0); }

		public Anchor(GuiComponent from, GuiComponent to, EnumGuiSide fromSide, EnumGuiSide toSide, double border) {
			this.from = new WeakReference<>(from);
			this.to = new WeakReference<>(to);
			this.fromSide = fromSide;
			this.toSide = toSide;
			this.border = border;

			checkNoCycles();
		}

		public boolean reRectangle(IGuiReshapeHandler handler, GuiComponent invoker) {
			@Nullable GuiComponent
					fromCopy = from.get(),
					toCopy = to.get();
			if (fromCopy == null || toCopy == null) return false;
			Rectangle2D rectangle = fromCopy.getShapeView().getBounds2D();
			fromSide.getSetter().accept(rectangle, fromSide.getApplyBorder().apply(toSide.getGetter().apply(toCopy.getShapeView().getBounds2D()), GuiAnchors.this.border + border));
			fromCopy.setBounds(handler, invoker, rectangle);
			return true;
		}

		@Override
		public void accept(IGuiReshapeHandler handler, GuiComponent invoker, Shape old) {
			if (!reRectangle(handler, invoker)) {
				@Nullable GuiComponent toCopy = to.get();
				if (toCopy != null)
					toCopy.listeners.reshape.remove(this);
				remove(fromSide);
			}
		}

		private void onAdded() {
			@Nullable GuiComponent toCopy = to.get();
			if (toCopy != null)
				toCopy.listeners.reshape.add(this);
		}

		private void onRemoved() {
			@Nullable GuiComponent toCopy = to.get();
			if (toCopy != null)
				toCopy.listeners.reshape.remove(this);
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
