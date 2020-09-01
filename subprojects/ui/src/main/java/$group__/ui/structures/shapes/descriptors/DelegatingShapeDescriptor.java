package $group__.ui.structures.shapes.descriptors;

import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.ui.core.structures.shapes.interactions.IShapeConstraint;
import $group__.utilities.interfaces.IDelegating;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.function.Supplier;

public class DelegatingShapeDescriptor<S extends Shape>
		extends IDelegating.Impl<IShapeDescriptor<S>>
		implements IShapeDescriptor<S> {
	public DelegatingShapeDescriptor(IShapeDescriptor<S> delegated) { super(delegated); }

	@Override
	public boolean isBeingModified() { return getDelegated().isBeingModified(); }

	@Override
	public S getShapeOutput() { return getDelegated().getShapeOutput(); }

	@Override
	public List<IShapeConstraint> getConstraintsView() { return getDelegated().getConstraintsView(); }

	@Override
	public List<IShapeConstraint> getConstraintsRef() throws IllegalStateException { return getDelegated().getConstraintsRef(); }

	@Override
	public boolean modify(Supplier<? extends Boolean> action) throws ConcurrentModificationException { return getDelegated().modify(action); }

	@Override
	public boolean bound(Rectangle2D bounds) throws IllegalStateException { return getDelegated().bound(bounds); }

	@Override
	public boolean transform(AffineTransform transform) throws IllegalStateException { return getDelegated().transform(transform); }
}
