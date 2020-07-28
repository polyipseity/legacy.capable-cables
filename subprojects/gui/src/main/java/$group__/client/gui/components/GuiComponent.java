package $group__.client.gui.components;

import $group__.client.gui.components.roots.GuiRoot;
import $group__.client.gui.structures.*;
import $group__.client.gui.structures.GuiCache.CacheKey;
import $group__.client.gui.traits.handlers.IGuiLifecycleHandler;
import $group__.client.gui.traits.handlers.IGuiReshapeHandler;
import $group__.client.gui.utilities.GuiUtilities;
import $group__.client.gui.utilities.Transforms.AffineTransforms;
import $group__.utilities.specific.ThrowableUtilities.BecauseOf;
import $group__.utilities.specific.ThrowableUtilities.Try;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.awt.geom.*;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static $group__.client.gui.structures.GuiConstraint.CONSTRAINT_NONE_VALUE;
import static $group__.client.gui.structures.GuiConstraint.getConstraintRectangleNone;
import static $group__.utilities.Capacities.INITIAL_CAPACITY_1;
import static $group__.utilities.Capacities.INITIAL_CAPACITY_2;
import static $group__.utilities.Casts.castUnchecked;
import static $group__.utilities.MiscellaneousUtilities.K;
import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public abstract class GuiComponent<D extends GuiComponent.Data<?>> implements IRenderableComponent, IGuiEventListenerComponent {
	private static final GuiConstraint CONSTRAINT_MINIMUM = new GuiConstraint(new Rectangle2D.Double(CONSTRAINT_NONE_VALUE, CONSTRAINT_NONE_VALUE, 1, 1), getConstraintRectangleNone());
	private static final Rectangle2D SHAPE_PLACEHOLDER = new Rectangle2D.Double(0, 0, 1, 1);
	public final D data;
	@Nullable
	protected WeakReference<GuiContainer<?>> parent = null;
	protected Shape shape; // COMMENT relative-to-parent shape

	public GuiComponent(Shape shape, D data) {
		this.shape = shape;
		this.data = data;
	}

	public static Rectangle2D getShapePlaceholder() { return (Rectangle2D) SHAPE_PLACEHOLDER.clone(); }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void renderPre(AffineTransformStack stack, Point2D mouse, float partialTicks) {
		AffineTransform transform = stack.delegated.peek();
		Rectangle2D r = transform.createTransformedShape(getShape()).getBounds2D(),
				r2 = (Rectangle2D) r.clone();
		data.constraints.forEach(c -> c.accept(r2));
		if (!r.equals(r2))
			Try.call(() -> AffineTransforms.getTransformFromTo(r, r2), data.logger.get()).ifPresent(t -> CacheKey.RESHAPE_HANDLER.get(this).reshape(this, this, t.createTransformedShape(getShape())));
	}

	public static GuiConstraint getConstraintMinimum() { return CONSTRAINT_MINIMUM.copy(); }

	@Override
	public void render(AffineTransformStack stack, Point2D mouse, float partialTicks) {}

	public void writeStencil(AffineTransformStack stack, Point2D mouse, float partialTicks, boolean increment) {
		if (increment)
			GuiUtilities.DrawingUtilities.drawShape(stack.delegated.peek(), shape, true, Color.WHITE, 0);
		else
			GuiUtilities.DrawingUtilities.drawShape(stack.delegated.peek(), shape.getBounds2D(), true, Color.BLACK, 0);
	}

	public void setBounds(IGuiReshapeHandler handler, GuiComponent<?> invoker, Rectangle2D rectangle) {
		Rectangle2D r = (Rectangle2D) rectangle.clone();
		getConstraintMinimum().accept(r);
		onReshape(handler, invoker, K(getShape(), shape = adaptToBounds(handler, invoker, r)));
	}

	protected Shape getShape() { return shape; }

	@OverridingMethodsMustInvokeSuper
	public void onReshape(IGuiReshapeHandler handler, GuiComponent<?> invoker, Shape shapePrevious) {
		data.events.fire(data.events.cReshape, new Events.CReshapeParameter(this, handler, invoker, shapePrevious));
	}

	protected Shape adaptToBounds(IGuiReshapeHandler handler, GuiComponent<?> invoker, Rectangle2D rectangle) { return AffineTransforms.getTransformFromTo(getShape().getBounds2D(), rectangle).createTransformedShape(getShape()); }

	@SuppressWarnings("EmptyMethod")
	protected void transformThis(AffineTransformStack stack) {}

	@OverridingMethodsMustInvokeSuper
	public void onAdded(GuiContainer<?> parentCurrent, int index) {
		this.parent = new WeakReference<>(parentCurrent);
		data.events.fire(data.events.cAdded, new Events.CAddedParameter(this, parentCurrent, index));
	}

	@SuppressWarnings("EmptyMethod")
	public void onMoved(@SuppressWarnings("unused") int index) {}

	@OverridingMethodsMustInvokeSuper
	public void onRemoved(GuiContainer<?> parentPrevious) {
		this.parent = null;
		data.setState(EnumGuiState.NEW);
		data.events.fire(data.events.cRemoved, new Events.CRemovedParameter(this, parentPrevious));
	}

	@OverridingMethodsMustInvokeSuper
	public void onInitialize(IGuiLifecycleHandler handler, GuiComponent<?> invoker) {
		data.setState(EnumGuiState.READY);
		if (!getNearestParentThatIs(GuiRoot.class).isPresent()
				|| !getNearestParentThatIs(IGuiLifecycleHandler.class).isPresent()
				|| !getNearestParentThatIs(IGuiReshapeHandler.class).isPresent())
			throw new IllegalStateException("Root or handlers not set!");
		data.events.fire(data.events.cInitialize, new Events.CInitializeParameter(this, handler, invoker));
	}

	@OverridingMethodsMustInvokeSuper
	public void onTick(IGuiLifecycleHandler handler, GuiComponent<?> invoker) {
		data.events.fire(data.events.cTick, new Events.CTickParameter(this, handler, invoker));
	}

	@OverridingMethodsMustInvokeSuper
	public void onClose(IGuiLifecycleHandler handler, GuiComponent<?> invoker) {
		data.setState(EnumGuiState.CLOSED);
		data.events.fire(data.events.cClose, new Events.CCloseParameter(this, handler, invoker));
	}

	@OverridingMethodsMustInvokeSuper
	public void onDestroyed(IGuiLifecycleHandler handler, GuiComponent<?> invoker) {
		data.setState(EnumGuiState.DESTROYED);
		data.events.fire(data.events.cDestroyed, new Events.CDestroyedParameter(this, handler, invoker));
	}

	public <T> Optional<T> getNearestParentThatIs(Class<T> clazz) {
		if (clazz.isAssignableFrom(getClass()))
			return castUnchecked(this);
		else
			return getParent().flatMap(p -> p.getNearestParentThatIs(clazz));
	}

	public Shape getShapeView() { return AffineTransforms.getIdentity().createTransformedShape(shape); }

	protected boolean isBeingDragged() { return getParent().filter(d -> d.data.drags.values().stream().anyMatch(dI -> dI.dragged == this)).isPresent(); }

	public Optional<GuiContainer<?>> getParent() {
		if (parent != null) {
			@Nullable GuiContainer<?> ret = parent.get();
			if (ret != null) return Optional.of(ret);
		}
		return Optional.empty();
	}

	protected boolean isBeingHovered() { return getParent().filter(p -> p.data.hovering == this).isPresent(); }

	protected boolean isBeingFocused() { return getParent().filter(p -> p.data.focused == this).isPresent(); }

	@Override
	public boolean isMouseOver(AffineTransformStack stack, Point2D mouse) { return stack.delegated.peek().createTransformedShape(getShape()).contains(mouse); }

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean onKeyPressed(int key, int scanCode, int modifiers) {
		data.keyPresses.addLast(new GuiKeyPressInfo(key, scanCode, modifiers));
		return false;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean onKeyReleased(int key, int scanCode, int modifiers) {
		data.keyPresses.removeFirstOccurrence(new GuiKeyPressInfo(key, scanCode, modifiers));
		return false;
	}

	@OnlyIn(CLIENT)
	public enum CoordinateConverters {
		;

		public static Rectangle2D toScaledRectangle(GuiComponent<?> component, Rectangle2D rectangle) {
			Rectangle2D r = (Rectangle2D) rectangle.clone();
			r.setFrame(toScaledPoint(component, new Point2D.Double(rectangle.getX(), rectangle.getMaxY())), toScaledDimension(component, new Dimension2DDouble(rectangle.getWidth(), rectangle.getHeight())));
			return r;
		}

		public static Point2D toScaledPoint(GuiComponent<?> component, Point2D point) {
			Point2D p = (Point2D) point.clone();
			p.setLocation(toScaledCoordinate(component, p.getX()), toScaledCoordinate(component, Minecraft.getInstance().getMainWindow().getFramebufferHeight() - p.getY()));
			return p;
		}

		public static Dimension2D toScaledDimension(GuiComponent<?> component, Dimension2D dimension) {
			Dimension2D dim = (Dimension2D) dimension.clone();
			dim.setSize(toScaledCoordinate(component, dim.getWidth()), toScaledCoordinate(component, dim.getHeight()));
			return dim;
		}

		public static double toScaledCoordinate(GuiComponent<?> component, double d) { return d / Minecraft.getInstance().getMainWindow().getGuiScaleFactor(); }

		public static Rectangle2D toNativeRectangle(GuiComponent<?> component, Rectangle2D rectangle) {
			Rectangle2D r = (Rectangle2D) rectangle.clone();
			r.setFrame(toNativePoint(component, new Point2D.Double(rectangle.getX(), rectangle.getMaxY())), toNativeDimension(component, new Dimension2DDouble(rectangle.getWidth(), rectangle.getHeight())));
			return r;
		}

		public static Point2D toNativePoint(GuiComponent<?> component, Point2D point) {
			Point2D p = (Point2D) point.clone();
			p.setLocation(toNativeCoordinate(component, p.getX()), toNativeCoordinate(component, CacheKey.ROOT.get(component).getRectangleView().getHeight() - p.getY()));
			return p;
		}

		public static Dimension2D toNativeDimension(GuiComponent<?> component, Dimension2D dimension) {
			Dimension2D dim = (Dimension2D) dimension.clone();
			dim.setSize(toNativeCoordinate(component, dim.getWidth()), toNativeCoordinate(component, dim.getHeight()));
			return dim;
		}

		public static double toNativeCoordinate(GuiComponent<?> component, double d) { return d * Minecraft.getInstance().getMainWindow().getGuiScaleFactor(); }
	}

	@OnlyIn(CLIENT)
	public enum ReferenceConverters {
		;

		public static Point2D toAbsolutePoint(AffineTransform transform, Point2D point) { return transform.transform(point, null); }

		public static Point2D toRelativePoint(AffineTransform transform, Point2D point) throws NoninvertibleTransformException { return transform.inverseTransform(point, null); }
	}

	@OnlyIn(CLIENT)
	public static class Data<E extends Events> {
		public final Deque<GuiConstraint> constraints = new ArrayDeque<>(INITIAL_CAPACITY_1);
		public final GuiAnchors anchors = new GuiAnchors();
		public final GuiCache cache = new GuiCache();
		public final E events;
		protected final Deque<GuiKeyPressInfo> keyPresses = new ArrayDeque<>(INITIAL_CAPACITY_2);
		public Supplier<Logger> logger;
		protected EnumGuiState state = EnumGuiState.NEW;

		public Data(E events, Supplier<Logger> logger) {
			this.events = events;
			this.logger = logger;
		}

		public ImmutableList<GuiKeyPressInfo> getKeyPresses() { return ImmutableList.copyOf(keyPresses); }

		public EnumGuiState getState() { return state; }

		protected void setState(EnumGuiState state) {
			if (!getState().getValidNextStates().contains(state))
				throw BecauseOf.illegalArgument("getState()", getState(), "state", state);
			this.state = state;
		}


	}

	@OnlyIn(CLIENT)
	public static class Events {
		public final List<Consumer<CAddedParameter>> cAdded = new ArrayList<>(INITIAL_CAPACITY_2);
		public final List<Consumer<CRemovedParameter>> cRemoved = new ArrayList<>(INITIAL_CAPACITY_2);
		public final List<Consumer<CInitializeParameter>> cInitialize = new ArrayList<>(INITIAL_CAPACITY_2);
		public final List<Consumer<CTickParameter>> cTick = new ArrayList<>(INITIAL_CAPACITY_2);
		public final List<Consumer<CCloseParameter>> cClose = new ArrayList<>(INITIAL_CAPACITY_2);
		public final List<Consumer<CDestroyedParameter>> cDestroyed = new ArrayList<>(INITIAL_CAPACITY_2);
		public final List<Consumer<CReshapeParameter>> cReshape = new ArrayList<>(INITIAL_CAPACITY_2);

		public <T> void fire(List<Consumer<T>> listeners, T parameter) { ImmutableList.copyOf(listeners).forEach(l -> l.accept(parameter)); }

		@OnlyIn(CLIENT)
		public static abstract class CParameter {
			public final GuiComponent<?> component;

			protected CParameter(GuiComponent<?> component) { this.component = component; }
		}

		@OnlyIn(CLIENT)
		public static class CAddedParameter extends CParameter {
			public final GuiContainer<?> parentCurrent;
			public final int index;

			public CAddedParameter(GuiComponent<?> component, GuiContainer<?> parentCurrent, int index) {
				super(component);
				this.parentCurrent = parentCurrent;
				this.index = index;
			}
		}

		@OnlyIn(CLIENT)
		public static class CRemovedParameter extends CParameter {
			public final GuiContainer<?> parentPrevious;

			public CRemovedParameter(GuiComponent<?> component, GuiContainer<?> parentPrevious) {
				super(component);
				this.parentPrevious = parentPrevious;
			}
		}

		@OnlyIn(CLIENT)
		public static abstract class CHasHandlerParameter<T> extends CParameter {
			public final T handler;
			public final GuiComponent<?> invoker;

			protected CHasHandlerParameter(GuiComponent<?> component, T handler, GuiComponent<?> invoker) {
				super(component);
				this.handler = handler;
				this.invoker = invoker;
			}
		}

		@OnlyIn(CLIENT)
		public static abstract class CLifecycleParameter extends CHasHandlerParameter<IGuiLifecycleHandler> {
			public CLifecycleParameter(GuiComponent<?> component, IGuiLifecycleHandler handler, GuiComponent<?> invoker) {
				super(component, handler, invoker);
			}
		}

		@OnlyIn(CLIENT)
		public static class CInitializeParameter extends CLifecycleParameter {
			public CInitializeParameter(GuiComponent<?> component, IGuiLifecycleHandler handler, GuiComponent<?> invoker) {
				super(component, handler, invoker);
			}
		}

		@OnlyIn(CLIENT)
		public static class CTickParameter extends CLifecycleParameter {
			public CTickParameter(GuiComponent<?> component, IGuiLifecycleHandler handler, GuiComponent<?> invoker) {
				super(component, handler, invoker);
			}
		}

		@OnlyIn(CLIENT)
		public static class CCloseParameter extends CLifecycleParameter {
			public CCloseParameter(GuiComponent<?> component, IGuiLifecycleHandler handler, GuiComponent<?> invoker) {
				super(component, handler, invoker);
			}
		}

		@OnlyIn(CLIENT)
		public static class CDestroyedParameter extends CLifecycleParameter {
			public CDestroyedParameter(GuiComponent<?> component, IGuiLifecycleHandler handler, GuiComponent<?> invoker) {
				super(component, handler, invoker);
			}
		}

		@OnlyIn(CLIENT)
		public static class CReshapeParameter0 extends CHasHandlerParameter<IGuiReshapeHandler> {
			public CReshapeParameter0(GuiComponent<?> component, IGuiReshapeHandler handler, GuiComponent<?> invoker) {
				super(component, handler, invoker);
			}
		}

		@OnlyIn(CLIENT)
		public static class CReshapeParameter extends CReshapeParameter0 {
			public final Shape shapePrevious;

			public CReshapeParameter(GuiComponent<?> component, IGuiReshapeHandler handler, GuiComponent<?> invoker, Shape shapePrevious) {
				super(component, handler, invoker);
				this.shapePrevious = shapePrevious;
			}
		}
	}
}
