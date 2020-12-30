package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.descriptors;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.shapes.interactions.IShapeConstraint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AbstractDelegatingObject;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.function.BooleanSupplier;

public abstract class AbstractDelegatingShapeDescriptor<S extends Shape, D extends IShapeDescriptor<?>>
		extends AbstractDelegatingObject<D>
		implements IShapeDescriptor<S> {
	public AbstractDelegatingShapeDescriptor(D delegated) {
		super(delegated);
	}

	@Override
	public boolean isBeingModified() { return getDelegate().isBeingModified(); }

	@Override
	public boolean isDynamic() {
		return getDelegate().isDynamic();
	}

	@Override
	public List<IShapeConstraint> getConstraintsView() { return getDelegate().getConstraintsView(); }

	@Override
	public List<IShapeConstraint> getConstraintsRef() throws IllegalStateException { return getDelegate().getConstraintsRef(); }

	@Override
	public boolean modify(BooleanSupplier action) throws ConcurrentModificationException { return getDelegate().modify(action); }

	@Override
	public boolean crop(Rectangle2D bounds) throws IllegalStateException { return getDelegate().crop(bounds); }

	@Override
	public boolean adapt(Rectangle2D frame) throws IllegalStateException { return getDelegate().adapt(frame); }

	@Override
	public boolean transform(AffineTransform transform) throws IllegalStateException { return getDelegate().transform(transform); }
}
