package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures.shapes.descriptors;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.interactions.IShapeConstraint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AbstractDelegatingObject;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.function.Supplier;

public class DelegatingShapeDescriptor<S extends Shape>
		extends AbstractDelegatingObject<IShapeDescriptor<S>>
		implements IShapeDescriptor<S> {
	public DelegatingShapeDescriptor(IShapeDescriptor<S> delegated) { super(delegated); }

	@Override
	public boolean isBeingModified() { return getDelegate().isBeingModified(); }

	@Override
	public S getShapeOutput() { return getDelegate().getShapeOutput(); }

	@Override
	public List<IShapeConstraint> getConstraintsView() { return getDelegate().getConstraintsView(); }

	@Override
	public List<IShapeConstraint> getConstraintsRef() throws IllegalStateException { return getDelegate().getConstraintsRef(); }

	@Override
	public boolean modify(Supplier<? extends Boolean> action) throws ConcurrentModificationException { return getDelegate().modify(action); }

	@Override
	public boolean bound(Rectangle2D bounds) throws IllegalStateException { return getDelegate().bound(bounds); }

	@Override
	public boolean transform(AffineTransform transform) throws IllegalStateException { return getDelegate().transform(transform); }
}
