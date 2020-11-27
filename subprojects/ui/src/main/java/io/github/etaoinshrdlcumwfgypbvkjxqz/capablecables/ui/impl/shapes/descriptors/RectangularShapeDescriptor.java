package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.descriptors;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.graphics.impl.UIObjectUtilities;

import javax.annotation.OverridingMethodsMustInvokeSuper;
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
	@OverridingMethodsMustInvokeSuper
	public boolean crop(Rectangle2D bounds)
			throws IllegalStateException {
		super.crop(bounds);
		Rectangle2D currentBounds = getShape().getBounds2D();
		Rectangle2D.intersect(currentBounds, bounds, currentBounds);
		getShape().setFrame(currentBounds);
		return true;
	}

	@Override
	public boolean adapt(Rectangle2D frame)
			throws IllegalStateException {
		super.adapt(frame);
		getShape().setFrame(frame);
		return true;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean transform(AffineTransform transform)
			throws IllegalStateException {
		super.transform(transform);
		UIObjectUtilities.transformRectangularShape(transform, getShape(), getShape());
		return true;
	}

	@Override
	public boolean adapt0(Rectangle2D frame) {
		getShape().setFrame(frame);
		return true;
	}
}
