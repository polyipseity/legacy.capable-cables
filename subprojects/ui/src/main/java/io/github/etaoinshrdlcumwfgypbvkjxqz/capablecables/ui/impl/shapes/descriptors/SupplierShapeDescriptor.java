package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.descriptors;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.function.Supplier;

public class SupplierShapeDescriptor<S extends Shape>
		extends AbstractShapeDescriptor<S> {
	private final Supplier<S> shapeSupplier;

	public SupplierShapeDescriptor(Supplier<S> shapeSupplier) {
		this.shapeSupplier = shapeSupplier;
	}

	@Override
	protected boolean crop0(Rectangle2D bounds) {
		// COMMENT NOOP
		return false;
	}

	@Override
	protected boolean transform0(AffineTransform transform) {
		// COMMENT NOOP
		return false;
	}

	@Override
	protected boolean adapt0(Rectangle2D frame) {
		// COMMENT NOOP
		return false;
	}

	@Override
	public boolean isDynamic() {
		return true;
	}

	@Override
	public S getShapeOutput() {
		return getShapeSupplier().get();
	}

	protected Supplier<S> getShapeSupplier() {
		return shapeSupplier;
	}
}
