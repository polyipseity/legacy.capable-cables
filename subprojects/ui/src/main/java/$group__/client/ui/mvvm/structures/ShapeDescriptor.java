package $group__.client.ui.mvvm.structures;

import $group__.client.ui.mvvm.core.structures.IShapeDescriptor;
import $group__.client.ui.mvvm.core.structures.IUIAnchorSet;
import $group__.client.ui.mvvm.core.structures.IUIConstraint;
import $group__.client.ui.mvvm.views.events.bus.EventUIShapeDescriptor;
import $group__.client.ui.utilities.UIObjectUtilities;
import $group__.utilities.CapacityUtilities;
import $group__.utilities.client.minecraft.TransformUtilities.AffineTransformUtilities;
import $group__.utilities.events.EnumEventHookStage;
import $group__.utilities.events.EventUtilities;
import $group__.utilities.specific.ThrowableUtilities.BecauseOf;
import com.google.common.collect.ImmutableList;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.function.Function;

import static $group__.client.ui.mvvm.core.structures.IShapeDescriptor.checkIsBeingModified;

public abstract class ShapeDescriptor<S extends Shape, A extends IUIAnchorSet<?>> implements IShapeDescriptor<S, A> {
	protected final A anchorSet;
	protected S shape;
	protected final List<IUIConstraint> constraints = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_SMALL);
	protected boolean beingModified = false;

	public ShapeDescriptor(S shape, A anchorSet) {
		this.shape = shape;
		this.anchorSet = anchorSet;
	}

	@Override
	public Shape getShapeProcessed() { return UIObjectUtilities.copyShape(getShape()); }

	@Override
	public List<IUIConstraint> getConstraintsView() { return ImmutableList.copyOf(getConstraints()); }

	@Override
	public List<IUIConstraint> getConstraintsRef()
			throws IllegalStateException {
		checkIsBeingModified(this);
		return getConstraints();
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public <T extends IShapeDescriptor<?, ?>> boolean modify(T self, Function<? super T, Boolean> action)
			throws ConcurrentModificationException {
		if (!self.equals(this))
			throw BecauseOf.illegalArgument("'self' does not equal to 'this'", "this", this, "self", self);
		if (isBeingModified())
			throw new ConcurrentModificationException('\'' + getClass().getName() + "' is already being modified");
		setBeingModified(true);
		boolean ret = EventUtilities.callWithPrePostHooks(() -> modify0(self, action),
				new EventUIShapeDescriptor.Modify(EnumEventHookStage.PRE, this),
				new EventUIShapeDescriptor.Modify(EnumEventHookStage.POST, this));
		setBeingModified(false);
		return ret;
	}

	@Override
	public A getAnchorSet() { return anchorSet; }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected List<IUIConstraint> getConstraints() { return constraints; }

	protected void setBeingModified(boolean beingModified) { this.beingModified = beingModified; }

	public static class Generic<A extends IUIAnchorSet<?>> extends ShapeDescriptor<Shape, A> {
		public Generic(Shape shape, A anchorSet) { super(shape, anchorSet); }

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

	public static class Rectangular<S extends RectangularShape, A extends IUIAnchorSet<?>> extends ShapeDescriptor<S, A> {
		public Rectangular(S shape, A anchorSet) { super(shape, anchorSet); }

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
			UIObjectUtilities.acceptRectangular(getShape(), (x, y) -> (h, w) ->
					getShape().setFrame(x + transform.getTranslateX(), y + transform.getTranslateY(),
							w * transform.getScaleX(), h * transform.getScaleY()));
			// COMMENT shear not supported
			return true;
		}
	}


}
