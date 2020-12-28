package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.descriptors;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.graphics.impl.UIObjectUtilities;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

public class RectangularShapeDescriptor<S extends RectangularShape>
		extends AbstractShapeDescriptor<S> {
	private S shape;

	public RectangularShapeDescriptor(S shape) { this.shape = shape; }

	@Override
	public boolean isDynamic() {
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public S getShapeOutput() {
		return (S) getShape().clone(); // COMMENT cloning should return itself
	}

	protected S getShape() { return shape; }

	protected void setShape(S shape) { this.shape = shape; }

	@Override
	public boolean crop0(Rectangle2D bounds) {
		Rectangle2D currentBounds = getShape().getBounds2D();
		Rectangle2D.intersect(currentBounds, bounds, currentBounds);
		getShape().setFrame(currentBounds);
		return true;
	}

	@Override
	public boolean transform0(AffineTransform transform) {
		UIObjectUtilities.transformRectangularShape(transform, getShape(), getShape());
		return true;
	}

	@Override
	public boolean adapt0(Rectangle2D frame) {
		getShape().setFrame(frame);
		return true;
	}
}
