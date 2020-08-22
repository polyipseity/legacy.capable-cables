package $group__.client.ui.structures.shapes.descriptors;

import $group__.client.ui.core.structures.shapes.IShapeDescriptor;
import $group__.client.ui.core.structures.shapes.IUIAnchor;
import $group__.client.ui.core.structures.shapes.IUIAnchorSet;
import $group__.client.ui.core.structures.shapes.IUIConstraint;
import $group__.client.ui.structures.shapes.interactions.UIAnchorSet;
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

import static $group__.client.ui.core.structures.shapes.IShapeDescriptor.checkIsBeingModified;

// TODO needs better design, but I cannot think of one
public abstract class AbstractShapeDescriptor<S extends Shape>
		implements IShapeDescriptor<S> {
	protected final List<IUIConstraint> constraints = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_SMALL);
	protected final IUIAnchorSet<IUIAnchor> anchors = new AnchorSet();
	protected boolean beingModified = false;

	protected AbstractShapeDescriptor() {}

	@Override
	public List<IUIConstraint> getConstraintsView() { return ImmutableList.copyOf(getConstraints()); }

	@Override
	public List<IUIConstraint> getConstraintsRef()
			throws IllegalStateException {
		checkIsBeingModified(this);
		return getConstraints();
	}

	@Override
	public IUIAnchorSet<IUIAnchor> getAnchorsRef() {
		checkIsBeingModified(this);
		return getAnchors();
	}

	protected IUIAnchorSet<IUIAnchor> getAnchors() { return anchors; }

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
			getAnchors().anchor(true);
			Rectangle2D bounds = getShapeOutput().getBounds2D();
			IShapeDescriptor.constrain(bounds, getConstraints());
			bound(bounds);
		}
		return ret;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected List<IUIConstraint> getConstraints() { return constraints; }

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

	protected class AnchorSet
			extends UIAnchorSet<IUIAnchor> {
		protected AnchorSet() { super(AbstractShapeDescriptor.this); }
	}
}
