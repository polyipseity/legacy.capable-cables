package $group__.client.gui.components;

import $group__.client.gui.components.roots.GuiRoot;
import $group__.client.gui.structures.GuiAnchors;
import $group__.client.gui.structures.GuiConstraint;
import $group__.client.gui.traits.IGuiLifecycleHandler;
import $group__.client.gui.traits.IGuiReRectangleHandler;
import $group__.client.gui.utilities.Matrices;
import $group__.utilities.helpers.Casts;
import $group__.utilities.helpers.specific.ThrowableUtilities.BecauseOf;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.util.TriConsumer;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static $group__.utilities.helpers.Capacities.INITIAL_CAPACITY_1;
import static $group__.utilities.helpers.Capacities.INITIAL_CAPACITY_2;
import static $group__.utilities.helpers.specific.ComparableUtilities.greaterThanOrEqualTo;
import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public class GuiComponent implements IRenderable, IGuiEventListenerComponent {
	public final Listeners listeners = new Listeners();
	@Nullable
	protected WeakReference<GuiContainer> parent = null;
	protected final Rectangle2D rectangle;
	public final GuiAnchors anchors = new GuiAnchors();
	public List<GuiConstraint> constraints = new ArrayList<>(INITIAL_CAPACITY_1);
	protected EnumState state = EnumState.NEW;
	public final CoordinateConverters coordinateConverters = new CoordinateConverters();

	public GuiComponent(Rectangle2D rectangle) { this.rectangle = rectangle; }

	public void render(MatrixStack matrix, Point2D mouse, float partialTicks) {}

	public void setRectangle(IGuiReRectangleHandler handler, GuiComponent invoker, Rectangle2D rectangle) {
		Rectangle2D old = getRectangleView();
		getRectangle().setRect(rectangle);
		constraints.forEach(c -> c.accept(getRectangle()));
		onReRectangle(handler, invoker, old);
	}

	@OverridingMethodsMustInvokeSuper
	public void onAdded(GuiContainer parent, int index) {
		this.parent = new WeakReference<>(parent);
		listeners.added.forEach(l -> l.accept(parent));
	}

	public void onMoved(@SuppressWarnings("unused") int index) {}

	@OverridingMethodsMustInvokeSuper
	public void onRemoved(GuiContainer parent) {
		this.parent = null;
		setState(EnumState.NEW);
		listeners.removed.forEach(l -> l.accept(parent));
	}

	@OverridingMethodsMustInvokeSuper
	public void onInitialize(IGuiLifecycleHandler handler, GuiComponent invoker) {
		setState(EnumState.READY);
		if (!getNearestParentThatIs(GuiRoot.class).isPresent() ||
				!getNearestParentThatIs(IGuiLifecycleHandler.class).isPresent() ||
				!getNearestParentThatIs(IGuiReRectangleHandler.class).isPresent())
			throw new IllegalStateException("Root or handlers not set!");
		listeners.initialize.forEach(l -> l.accept(handler, invoker));
	}

	@OverridingMethodsMustInvokeSuper
	public void onTick(IGuiLifecycleHandler handler, GuiComponent invoker) {
		listeners.tick.forEach(l -> l.accept(handler, invoker));
	}

	@OverridingMethodsMustInvokeSuper
	public void onClose(IGuiLifecycleHandler handler, GuiComponent invoker) {
		setState(EnumState.CLOSED);
		listeners.close.forEach(l -> l.accept(handler, invoker));
	}

	@OverridingMethodsMustInvokeSuper
	public void onDestroyed(IGuiLifecycleHandler handler, GuiComponent invoker) {
		setState(EnumState.DESTROYED);
		listeners.destroyed.forEach(l -> l.accept(handler, invoker));
	}

	@OverridingMethodsMustInvokeSuper
	public void onReRectangle(IGuiReRectangleHandler handler, GuiComponent invoker, Rectangle2D old) {
		listeners.reRectangle.forEach(l -> l.accept(handler, invoker, old));
	}

	public Optional<GuiContainer> getParent() {
		if (parent != null) {
			@Nullable GuiContainer ret = parent.get();
			if (ret != null) return Optional.of(ret);
		}
		return Optional.empty();
	}

	public <T> Optional<T> getNearestParentThatIs(Class<T> clazz) {
		if (clazz.isAssignableFrom(getClass()))
			return Casts.castUnchecked(this);
		else
			return getParent().flatMap(p -> p.getNearestParentThatIs(clazz));
	}

	public EnumState getState() { return state; }

	protected void setState(EnumState state) {
		if (!getState().getValidNextStates().contains(state))
			throw BecauseOf.illegalArgument("getState()", getState(), "state", state);
		this.state = state;
	}

	public Rectangle2D getRectangleView() { return (Rectangle2D) rectangle.clone(); }

	protected Rectangle2D getRectangle() { return rectangle; }

	@Override
	public final GuiComponent getComponent() { return this; }

	@Override
	public boolean isMouseOver(MatrixStack matrix, Point2D mouse) {
		Point2D p = ReferenceConverters.toRelativePoint(matrix.getLast().getMatrix(), mouse);
		return p.getX() >= 0 && p.getX() < getRectangle().getWidth() && p.getY() >= 0 && p.getY() < getRectangle().getHeight();
	}

	public enum EnumState {
		NEW {
			@Override
			public EnumSet<EnumState> getValidNextStates() { return EnumSet.of(NEW, READY, DESTROYED); }
		},
		READY {
			@Override
			public EnumSet<EnumState> getValidNextStates() { return EnumSet.of(READY, CLOSED, DESTROYED); }
		},
		CLOSED {
			@Override
			public EnumSet<EnumState> getValidNextStates() { return EnumSet.of(CLOSED, NEW, DESTROYED); }
		},
		DESTROYED {
			@Override
			public EnumSet<EnumState> getValidNextStates() { return EnumSet.of(DESTROYED); }
		};

		public abstract EnumSet<EnumState> getValidNextStates();

		public boolean isReachedBy(EnumState state) { return greaterThanOrEqualTo(state, this); }
	}

	public static class Listeners {
		public final List<Consumer<? super GuiContainer>>
				added = new ArrayList<>(INITIAL_CAPACITY_2),
				removed = new ArrayList<>(INITIAL_CAPACITY_2);
		public final List<BiConsumer<? super IGuiLifecycleHandler, ? super GuiComponent>>
				initialize = new ArrayList<>(INITIAL_CAPACITY_2),
				tick = new ArrayList<>(INITIAL_CAPACITY_2),
				close = new ArrayList<>(INITIAL_CAPACITY_2),
				destroyed = new ArrayList<>(INITIAL_CAPACITY_2);
		public final List<TriConsumer<? super IGuiReRectangleHandler, ? super GuiComponent, ? super Rectangle2D>>
				reRectangle = new ArrayList<>(INITIAL_CAPACITY_2);
	}

	@Override
	@Deprecated
	public final void render(int mouseX, int mouseY, float partialTicks) { render(new MatrixStack(), new Point(mouseX, mouseY), partialTicks); }

	@OnlyIn(CLIENT)
	public static class ReferenceConverters {
		public static Point2D toAbsolutePoint(Matrix4f matrix, Point2D point) {
			Point2D p = (Point2D) point.clone();
			Matrices.transformPoint(p, matrix);
			return p;
		}

		public static Point2D toRelativePoint(Matrix4f matrix, Point2D point) {
			Point2D p = (Point2D) point.clone();
			Matrix4f mat = matrix.copy();
			if (mat.invert())
				Matrices.transformPoint(p, mat);
			return p;
		}
	}

	@OnlyIn(CLIENT)
	public class CoordinateConverters {
		public double toScaledCoordinate(double d) { return d / getNearestParentThatIs(GuiRoot.class).orElseThrow(BecauseOf::unexpected).getScreen().getMinecraft().getMainWindow().getGuiScaleFactor(); }

		public double toNativeCoordinate(double d) { return d * getNearestParentThatIs(GuiRoot.class).orElseThrow(BecauseOf::unexpected).getScreen().getMinecraft().getMainWindow().getGuiScaleFactor(); }

		public Point2D toScaledPoint(Point2D point) {
			Point2D p = (Point2D) point.clone();
			p.setLocation(toScaledCoordinate(p.getX()), toScaledCoordinate(p.getY()));
			return p;
		}

		public Point2D toNativePoint(Point2D point) {
			Point2D p = (Point2D) point.clone();
			p.setLocation(toNativeCoordinate(p.getX()), toNativeCoordinate(p.getY()));
			return p;
		}

		public Dimension2D toNativeDimension(Dimension2D dimension) {
			Dimension2D dim = (Dimension2D) dimension.clone();
			dim.setSize(toNativeCoordinate(dim.getWidth()), toNativeCoordinate(dim.getHeight()));
			return dim;
		}

		public Dimension2D toScaledDimension(Dimension2D dimension) {
			Dimension2D dim = (Dimension2D) dimension.clone();
			dim.setSize(toScaledCoordinate(dim.getWidth()), toScaledCoordinate(dim.getHeight()));
			return dim;
		}

		public Rectangle2D toScaledRectangle(Rectangle2D rectangle) {
			Rectangle2D r = (Rectangle2D) rectangle.clone();
			r.setFrame(toScaledPoint(new Point2D.Double(rectangle.getX(), rectangle.getY())), toScaledDimension(new Dimension((int) rectangle.getWidth(), (int) rectangle.getHeight())));
			return r;
		}

		public Rectangle2D toNativeRectangle(Rectangle2D rectangle) {
			Rectangle2D r = (Rectangle2D) rectangle.clone();
			r.setFrame(toNativePoint(new Point2D.Double(rectangle.getX(), rectangle.getY())), toNativeDimension(new Dimension((int) rectangle.getWidth(), (int) rectangle.getHeight())));
			return r;
		}
	}
}
