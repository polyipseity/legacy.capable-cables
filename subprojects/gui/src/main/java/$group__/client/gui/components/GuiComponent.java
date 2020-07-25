package $group__.client.gui.components;

import $group__.client.gui.components.roots.GuiRoot;
import $group__.client.gui.structures.*;
import $group__.client.gui.structures.GuiCache.CacheKey;
import $group__.client.gui.traits.handlers.IGuiLifecycleHandler;
import $group__.client.gui.traits.handlers.IGuiReshapeHandler;
import $group__.client.gui.utilities.Transforms.AffineTransforms;
import $group__.utilities.specific.ThrowableUtilities.BecauseOf;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.util.TriConsumer;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.awt.geom.*;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static $group__.client.gui.structures.GuiConstraint.CONSTRAINT_NONE_VALUE;
import static $group__.client.gui.structures.GuiConstraint.getConstraintRectangleNone;
import static $group__.utilities.Capacities.INITIAL_CAPACITY_1;
import static $group__.utilities.Capacities.INITIAL_CAPACITY_2;
import static $group__.utilities.Casts.castUnchecked;
import static $group__.utilities.MiscellaneousUtilities.K;
import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public abstract class GuiComponent implements IRenderableComponent, IGuiEventListenerComponent {
	private static final GuiConstraint CONSTRAINT_MINIMUM = new GuiConstraint(new Rectangle2D.Double(CONSTRAINT_NONE_VALUE, CONSTRAINT_NONE_VALUE, 1, 1), getConstraintRectangleNone());
	private static final Rectangle2D SHAPE_PLACEHOLDER = new Rectangle2D.Double(0, 0, 1, 1);
	public final Listeners listeners = new Listeners();
	@Nullable
	protected WeakReference<GuiContainer> parent = null;
	public final Deque<GuiConstraint> constraints = new ArrayDeque<>(INITIAL_CAPACITY_1);
	public final GuiAnchors anchors = new GuiAnchors();
	protected Shape shape; // COMMENT relative shape
	protected EnumGuiState state = EnumGuiState.NEW;
	protected final Deque<GuiKeyPressInfo> keyPresses = new ArrayDeque<>(INITIAL_CAPACITY_2);
	public final GuiCache cache = new GuiCache();
	public final CoordinateConverters coordinateConverters = new CoordinateConverters();

	public GuiComponent(Shape shape) {
		this.shape = shape;
		CacheKey.initializeAll(this);
	}

	public static GuiConstraint getConstraintMinimum() { return (GuiConstraint) CONSTRAINT_MINIMUM.clone(); }

	public static Rectangle2D getShapePlaceholder() { return (Rectangle2D) SHAPE_PLACEHOLDER.clone(); }

	public void render(AffineTransformStack stack, Point2D mouse, float partialTicks) {}

	public void setBounds(IGuiReshapeHandler handler, GuiComponent invoker, Rectangle2D rectangle) {
		Rectangle2D r = (Rectangle2D) rectangle.clone();
		constraints.push(getConstraintMinimum());
		constraints.forEach(c -> c.accept(r));
		constraints.pop();
		onReshape(handler, invoker, K(getShape(), shape = adaptToBounds(handler, invoker, r)));
	}

	protected Shape adaptToBounds(IGuiReshapeHandler handler, GuiComponent invoker, Rectangle2D rectangle) { return AffineTransforms.getTransformFromTo(getShape().getBounds2D(), rectangle).createTransformedShape(getShape()); }

	@OverridingMethodsMustInvokeSuper
	public void onAdded(GuiContainer parent, int index) {
		this.parent = new WeakReference<>(parent);
		listeners.added.forEach(l -> l.accept(parent));
	}

	@SuppressWarnings("EmptyMethod")
	public void onMoved(@SuppressWarnings("unused") int index) {}

	@OverridingMethodsMustInvokeSuper
	public void onRemoved(GuiContainer parent) {
		this.parent = null;
		setState(EnumGuiState.NEW);
		listeners.removed.forEach(l -> l.accept(parent));
	}

	@OverridingMethodsMustInvokeSuper
	public void onInitialize(IGuiLifecycleHandler handler, GuiComponent invoker) {
		setState(EnumGuiState.READY);
		if (!getNearestParentThatIs(GuiRoot.class).isPresent()
				|| !getNearestParentThatIs(IGuiLifecycleHandler.class).isPresent()
				|| !getNearestParentThatIs(IGuiReshapeHandler.class).isPresent())
			throw new IllegalStateException("Root or handlers not set!");
		listeners.initialize.forEach(l -> l.accept(handler, invoker));
	}

	@OverridingMethodsMustInvokeSuper
	public void onTick(IGuiLifecycleHandler handler, GuiComponent invoker) {
		listeners.tick.forEach(l -> l.accept(handler, invoker));
	}

	@OverridingMethodsMustInvokeSuper
	public void onClose(IGuiLifecycleHandler handler, GuiComponent invoker) {
		setState(EnumGuiState.CLOSED);
		listeners.close.forEach(l -> l.accept(handler, invoker));
	}

	@OverridingMethodsMustInvokeSuper
	public void onDestroyed(IGuiLifecycleHandler handler, GuiComponent invoker) {
		setState(EnumGuiState.DESTROYED);
		listeners.destroyed.forEach(l -> l.accept(handler, invoker));
	}

	@OverridingMethodsMustInvokeSuper
	public void onReshape(IGuiReshapeHandler handler, GuiComponent invoker, Shape old) {
		listeners.reshape.forEach(l -> l.accept(handler, invoker, old));
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean onKeyPressed(int key, int scanCode, int modifiers) {
		keyPresses.addLast(new GuiKeyPressInfo(key, scanCode, modifiers));
		return false;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean onKeyReleased(int key, int scanCode, int modifiers) {
		keyPresses.removeFirstOccurrence(new GuiKeyPressInfo(key, scanCode, modifiers));
		return false;
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
			return castUnchecked(this);
		else
			return getParent().flatMap(p -> p.getNearestParentThatIs(clazz));
	}

	public EnumGuiState getState() { return state; }

	protected void setState(EnumGuiState state) {
		if (!getState().getValidNextStates().contains(state))
			throw BecauseOf.illegalArgument("getState()", getState(), "state", state);
		this.state = state;
	}

	public Shape getShapeView() { return AffineTransforms.getIdentity().createTransformedShape(shape); }

	protected Shape getShape() { return shape; }

	protected boolean isBeingDragged() {
		return getParent().map(p -> p.drag)
				.filter(d -> d.dragged == this && (!(this instanceof GuiContainer) || ((GuiContainer) this).drag == null)).isPresent();
	}

	protected boolean isBeingHovered() { return getParent().filter(p -> p.hovering == this && (!(this instanceof GuiContainer) || ((GuiContainer) this).hovering == null)).isPresent(); }

	@Override
	public Optional<GuiDragInfo> getDragInfo() { return getParent().map(p -> p.drag); }

	@Override
	public final GuiComponent getComponent() { return this; }

	@Override
	public boolean isMouseOver(AffineTransformStack stack, Point2D mouse) { return stack.delegated.peek().createTransformedShape(getShape()).contains(mouse); }

	@OnlyIn(CLIENT)
	public static class Listeners {
		public final List<Consumer<? super GuiContainer>>
				added = new ArrayList<>(INITIAL_CAPACITY_2),
				removed = new ArrayList<>(INITIAL_CAPACITY_2);
		public final List<BiConsumer<? super IGuiLifecycleHandler, ? super GuiComponent>>
				initialize = new ArrayList<>(INITIAL_CAPACITY_2),
				tick = new ArrayList<>(INITIAL_CAPACITY_2),
				close = new ArrayList<>(INITIAL_CAPACITY_2),
				destroyed = new ArrayList<>(INITIAL_CAPACITY_2);
		public final List<TriConsumer<? super IGuiReshapeHandler, ? super GuiComponent, ? super Shape>>
				reshape = new ArrayList<>(INITIAL_CAPACITY_2);
	}

	@OnlyIn(CLIENT)
	public static class ReferenceConverters {
		public static Point2D toAbsolutePoint(AffineTransform transform, Point2D point) { return transform.transform(point, null); }

		public static Point2D toRelativePoint(AffineTransform transform, Point2D point) throws NoninvertibleTransformException { return transform.inverseTransform(point, null); }
	}

	@OnlyIn(CLIENT)
	public class CoordinateConverters {
		public double toScaledCoordinate(double d) { return d / CacheKey.SCALE_FACTOR.get(GuiComponent.this); }

		public double toNativeCoordinate(double d) { return d * CacheKey.SCALE_FACTOR.get(GuiComponent.this); }

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
