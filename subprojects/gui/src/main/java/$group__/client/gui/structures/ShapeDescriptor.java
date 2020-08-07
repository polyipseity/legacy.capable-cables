package $group__.client.gui.structures;

import $group__.client.gui.core.IGuiView;
import $group__.client.gui.core.events.EventGuiShapeDescriptor;
import $group__.client.gui.core.structures.IGuiAnchorSet;
import $group__.client.gui.core.structures.IGuiConstraint;
import $group__.client.gui.core.structures.IShapeDescriptor;
import $group__.client.gui.utilities.GuiObjectUtilities;
import $group__.utilities.CapacityUtilities;
import $group__.utilities.client.TransformUtilities.AffineTransformUtilities;
import $group__.utilities.events.EnumEventHookStage;
import $group__.utilities.events.EventUtilities;
import $group__.utilities.specific.ThrowableUtilities.BecauseOf;
import com.google.common.collect.ImmutableList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static $group__.client.gui.core.structures.IShapeDescriptor.checkIsBeingModified;

@OnlyIn(Dist.CLIENT)
public abstract class ShapeDescriptor<S extends Shape, A extends IGuiAnchorSet<?>> implements IShapeDescriptor<S, A> {
	protected final A anchorSet;
	protected S shape;
	protected final List<IGuiConstraint> constraints = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_SMALL);
	protected final WeakReference<IGuiView<?, ?>> view;
	protected boolean beingModified = false;

	public ShapeDescriptor(IGuiView<?, ?> view, S shape, A anchorSet) {
		this.view = new WeakReference<>(view);
		this.shape = shape;
		this.anchorSet = anchorSet;
	}

	@OnlyIn(Dist.CLIENT)
	public static class Generic<A extends IGuiAnchorSet<?>> extends ShapeDescriptor<Shape, A> {
		public Generic(IGuiView<?, ?> view, Shape shape, A anchorSet) { super(view, shape, anchorSet); }

		@Override
		@OverridingMethodsMustInvokeSuper
		public boolean bound(Rectangle2D bounds)
				throws IllegalStateException {
			super.bound(bounds);
			setShape(AffineTransformUtilities.getTransformFromTo(getShape().getBounds2D(), bounds).createTransformedShape(getShape()));
			return true;
		}

		@Override
		@OverridingMethodsMustInvokeSuper
		public boolean transform(AffineTransform transform)
				throws IllegalStateException {
			super.transform(transform);
			setShape(transform.createTransformedShape(getShape()));
			return true;
		}
	}

	@Override
	public Optional<IGuiView<?, ?>> getView() { return Optional.ofNullable(view.get()); }

	@OnlyIn(Dist.CLIENT)
	public static class Rectangular<S extends RectangularShape, A extends IGuiAnchorSet<?>> extends ShapeDescriptor<S, A> {
		public Rectangular(IGuiView<?, ?> view, S shape, A anchorSet) { super(view, shape, anchorSet); }

		@Override
		@OverridingMethodsMustInvokeSuper
		public boolean bound(Rectangle2D bounds)
				throws IllegalStateException {
			super.bound(bounds);
			getShape().setFrame(bounds);
			return true;
		}

		@Override
		@OverridingMethodsMustInvokeSuper
		public boolean transform(AffineTransform transform)
				throws IllegalStateException {
			super.transform(transform);
			GuiObjectUtilities.acceptRectangular(getShape(), (x, y) -> (h, w) ->
					getShape().setFrame(x + transform.getTranslateX(), y + transform.getTranslateY(),
							w * transform.getScaleX(), h * transform.getScaleY()));
			// COMMENT shear not supported
			return true;
		}
	}

	@Override
	public ImmutableList<IGuiConstraint> getConstraintsView() { return ImmutableList.copyOf(getConstraints()); }

	@Override
	public List<IGuiConstraint> getConstraintsRef()
			throws IllegalStateException {
		checkIsBeingModified(this);
		return getConstraints();
	}

	@Override
	public A getAnchorSet() { return anchorSet; }

	@Override
	public Shape getShapeProcessed() { return GuiObjectUtilities.copyShape(getShape()); }

	protected void setBeingModified(boolean beingModified) { this.beingModified = beingModified; }

	@Override
	@OverridingMethodsMustInvokeSuper
	public <T extends IShapeDescriptor<?, ?>> boolean modify(T self, Function<? super T, Boolean> action)
			throws ConcurrentModificationException {
		if (self != this)
			throw BecauseOf.illegalArgument("'self' does not equal to 'this'", "this", this, "self", self);
		if (isBeingModified())
			throw new ConcurrentModificationException('\'' + getClass().getName() + "' is already being modified");
		setBeingModified(true);
		boolean ret = EventUtilities.callWithPrePostHooks(() -> modify0(self, action),
				new EventGuiShapeDescriptor.Modify(EnumEventHookStage.PRE, this),
				new EventGuiShapeDescriptor.Modify(EnumEventHookStage.POST, this));
		setBeingModified(false);
		return ret;
	}

	protected <T extends IShapeDescriptor<?, ?>> boolean modify0(T self, Function<? super T, Boolean> action) {
		boolean ret = action.apply(self);
		Rectangle2D current = getShape().getBounds2D(), bounds = (Rectangle2D) current.clone();
		getConstraints().forEach(c -> c.accept(bounds));
		Constants.getConstraintMinimumView().accept(bounds);
		bound(bounds);
		return ret;
	}

	@Override
	public boolean isBeingModified() { return beingModified; }

	@Override
	public S getShapeRef()
			throws IllegalStateException {
		checkIsBeingModified(this);
		return getShape();
	}

	protected S getShape() { return shape; }

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean setShape(S shape)
			throws IllegalStateException {
		checkIsBeingModified(this);
		this.shape = shape;
		return true;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean bound(Rectangle2D bounds)
			throws IllegalStateException {
		checkIsBeingModified(this);
		return false;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean transform(AffineTransform transform)
			throws IllegalStateException {
		checkIsBeingModified(this);
		return false;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected List<IGuiConstraint> getConstraints() { return constraints; }


}
