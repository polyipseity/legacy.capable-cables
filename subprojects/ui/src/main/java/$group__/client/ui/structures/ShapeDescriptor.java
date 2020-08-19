package $group__.client.ui.structures;

import $group__.client.ui.core.IShapeDescriptor;
import $group__.client.ui.core.IUIConstraint;
import $group__.client.ui.mvvm.core.structures.IUIAnchor;
import $group__.client.ui.mvvm.core.structures.IUIAnchorSet;
import $group__.client.ui.mvvm.structures.UIAnchorSet;
import $group__.client.ui.mvvm.views.events.bus.EventUIShapeDescriptor;
import $group__.client.ui.utilities.UIObjectUtilities;
import $group__.utilities.CapacityUtilities;
import $group__.utilities.client.AffineTransformUtilities;
import $group__.utilities.events.EnumEventHookStage;
import $group__.utilities.events.EventUtilities;
import com.google.common.collect.ImmutableList;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.function.Supplier;

import static $group__.client.ui.core.IShapeDescriptor.checkIsBeingModified;

// TODO needs better design, but I cannot think of one
public abstract class ShapeDescriptor<S extends Shape> implements IShapeDescriptor<S> {
	protected final List<IUIConstraint> constraints = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_SMALL);
	protected final IUIAnchorSet<IUIAnchor> anchorSet = new AnchorSet();
	protected boolean beingModified = false;

	protected ShapeDescriptor() {}

	@Override
	public List<IUIConstraint> getConstraintsView() { return ImmutableList.copyOf(getConstraints()); }

	@Override
	public List<IUIConstraint> getConstraintsRef()
			throws IllegalStateException {
		checkIsBeingModified(this);
		return getConstraints();
	}

	@Override
	public IUIAnchorSet<IUIAnchor> getAnchorSet() { return anchorSet; }

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean modify(Supplier<? extends Boolean> action)
			throws ConcurrentModificationException {
		if (isBeingModified())
			throw new ConcurrentModificationException('\'' + getClass().getName() + "' is already being modified");
		setBeingModified(true);
		boolean ret = EventUtilities.callWithPrePostHooks(() -> modify0(action),
				new EventUIShapeDescriptor.Modify(EnumEventHookStage.PRE, this),
				new EventUIShapeDescriptor.Modify(EnumEventHookStage.POST, this));
		setBeingModified(false);
		return ret;
	}

	protected void setBeingModified(boolean beingModified) { this.beingModified = beingModified; }

	protected boolean modify0(Supplier<? extends Boolean> action) {
		boolean ret = action.get();
		constrain(this);
		return ret;
	}

	protected static void constrain(IShapeDescriptor<?> self) {
		Rectangle2D current = self.getShapeOutput().getBounds2D(), bounds = (Rectangle2D) current.clone();
		self.getConstraintsRef().forEach(c -> c.accept(bounds));
		Constants.getConstraintMinimumView().accept(bounds);
		self.bound(bounds);
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected List<IUIConstraint> getConstraints() { return constraints; }

	public static class Generic extends ShapeDescriptor<Shape> {
		protected Shape shape;

		public Generic(Shape shape) {
			this.shape = shape;
		}

		@Override
		public Shape getShapeOutput() { return getShape(); }

		protected Shape getShape() { return shape; }

		protected void setShape(Shape shape) { this.shape = shape; }

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
	public boolean isBeingModified() { return beingModified; }

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

	public static class Rectangular<S extends RectangularShape> extends ShapeDescriptor<S> {
		protected S shape;

		public Rectangular(S shape) { this.shape = shape; }

		@SuppressWarnings("unchecked")
		@Override
		public S getShapeOutput() {
			return (S) getShape().clone(); // COMMENT cloning should return itself
		}

		protected S getShape() { return shape; }

		protected void setShape(S shape) { this.shape = shape; }

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
			UIObjectUtilities.acceptRectangular(getShape(), (x, y, h, w) ->
					getShape().setFrame(x + transform.getTranslateX(), y + transform.getTranslateY(),
							w * transform.getScaleX(), h * transform.getScaleY()));
			// COMMENT shear not supported
			return true;
		}
	}

	protected class AnchorSet
			extends UIAnchorSet<IUIAnchor> {
		protected AnchorSet() { super(ShapeDescriptor.this); }
	}
}
