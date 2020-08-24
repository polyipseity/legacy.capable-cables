package $group__.ui.structures.shapes.descriptors;

import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.ui.core.structures.shapes.interactions.IShapeConstraint;
import $group__.utilities.CapacityUtilities;
import com.google.common.collect.ImmutableList;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.function.Supplier;

import static $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor.checkIsBeingModified;

// TODO needs better design, but I cannot think of one
public abstract class AbstractShapeDescriptor<S extends Shape>
		implements IShapeDescriptor<S> {
	protected final List<IShapeConstraint> constraints = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_SMALL);
	protected boolean beingModified = false;

	protected AbstractShapeDescriptor() {}

	@Override
	public List<IShapeConstraint> getConstraintsView() { return ImmutableList.copyOf(getConstraints()); }

	@Override
	public List<IShapeConstraint> getConstraintsRef()
			throws IllegalStateException {
		checkIsBeingModified(this);
		return getConstraints();
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean modify(Supplier<? extends Boolean> action)
			throws ConcurrentModificationException {
		if (isBeingModified())
			throw new ConcurrentModificationException('\'' + getClass().getName() + "' is already being modified");
		setBeingModified(true);
		boolean ret = modify0(action);
		setBeingModified(false);
		return ret;
	}

	protected void setBeingModified(boolean beingModified) { this.beingModified = beingModified; }

	protected boolean modify0(Supplier<? extends Boolean> action) {
		boolean ret = action.get();
		if (ret) {
			Rectangle2D bounds = getShapeOutput().getBounds2D();
			IShapeDescriptor.constrain(bounds, getConstraints());
			bound(bounds);
		}
		return ret;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected List<IShapeConstraint> getConstraints() { return constraints; }

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
}
