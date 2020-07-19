package $group__.client.gui.components;

import $group__.client.gui.components.roots.GuiRoot;
import $group__.client.gui.structures.GuiAnchors;
import $group__.client.gui.structures.GuiConstraint;
import $group__.client.gui.traits.IGuiLifecycleHandler;
import $group__.client.gui.traits.IGuiReRectangleHandler;
import $group__.utilities.helpers.specific.ThrowableUtilities.BecauseOf;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.IRenderable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.util.TriConsumer;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
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

@OnlyIn(Dist.CLIENT)
public class GuiComponent implements IRenderable, IGuiEventListenerImproved {
	public final Listeners listeners = new Listeners();
	@Nullable
	protected WeakReference<GuiContainer> parent = null;
	protected final Rectangle2D rectangle = new Rectangle();
	public final GuiAnchors anchors = new GuiAnchors();
	public List<GuiConstraint> constraints = new ArrayList<>(INITIAL_CAPACITY_1);
	protected EnumState state = EnumState.NEW;

	public void render(MatrixStack matrix, Point2D mouse, float partialTicks) {}

	protected Point2D toAbsolutePoint(Point2D point) {
		Point2D pointAbs = new Point2D.Double(point.getX() + getRectangle().getX(), point.getY() + getRectangle().getY());
		if (parent != null) {
			@Nullable GuiContainer parentCopy = parent.get();
			if (parentCopy != null)
				return parentCopy.toAbsolutePoint(pointAbs);
		}
		return pointAbs;
	}

	public void setRectangle(IGuiReRectangleHandler handler, GuiComponent invoker, Rectangle2D rectangle) {
		Rectangle2D old = getRectangleView();
		getRectangle().setRect(rectangle);
		constraints.forEach(c -> c.accept(getRectangle()));
		onReRectangle(handler, invoker, old);
	}

	@OverridingMethodsMustInvokeSuper
	public void onAdded(GuiContainer parent) {
		this.parent = new WeakReference<>(parent);
		listeners.added.forEach(l -> l.accept(parent));
	}

	@OverridingMethodsMustInvokeSuper
	public void onRemoved(GuiContainer parent) {
		this.parent = null;
		setState(EnumState.NEW);
		listeners.removed.forEach(l -> l.accept(parent));
	}

	@OverridingMethodsMustInvokeSuper
	public void onInitialize(IGuiLifecycleHandler handler, GuiComponent invoker) {
		setState(EnumState.READY);
		if (!getRoot().isPresent() || !getLifecycleHandler().isPresent() || !getReRectangleHandler().isPresent())
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

	public Optional<? extends GuiRoot> getRoot() {
		if (this instanceof GuiRoot) return Optional.of((GuiRoot) this);
		else if (parent != null) return Optional.ofNullable(parent.get()).flatMap(GuiComponent::getRoot);
		return Optional.empty();
	}

	public Optional<? extends IGuiLifecycleHandler> getLifecycleHandler() {
		if (this instanceof IGuiLifecycleHandler) return Optional.of((IGuiLifecycleHandler) this);
		else if (parent != null) return Optional.ofNullable(parent.get()).flatMap(GuiComponent::getLifecycleHandler);
		return Optional.empty();
	}

	public Optional<? extends IGuiReRectangleHandler> getReRectangleHandler() {
		if (this instanceof IGuiReRectangleHandler) return Optional.of((IGuiReRectangleHandler) this);
		else if (parent != null) return Optional.ofNullable(parent.get()).flatMap(GuiComponent::getReRectangleHandler);
		return Optional.empty();
	}

	public EnumState getState() { return state; }

	protected void setState(EnumState state) {
		if (!getState().getValidNextStates().contains(state))
			throw BecauseOf.illegalArgument("state", state);
		this.state = state;
	}

	public Rectangle2D getRectangleView() { return (Rectangle2D) rectangle.clone(); }

	protected Rectangle2D getRectangle() { return rectangle; }

	public enum EnumState {
		NEW {
			@Override
			public EnumSet<EnumState> getValidNextStates() { return EnumSet.of(NEW, READY); }
		},
		READY {
			@Override
			public EnumSet<EnumState> getValidNextStates() { return EnumSet.of(READY, CLOSED); }
		},
		CLOSED {
			@Override
			public EnumSet<EnumState> getValidNextStates() { return EnumSet.of(CLOSED, DESTROYED, NEW); }
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
}
