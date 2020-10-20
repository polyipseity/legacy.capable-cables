package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.descriptors;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AffineTransformUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.graphics.impl.UIObjectUtilities;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class GenericShapeDescriptor extends AbstractShapeDescriptor<Shape> {
	private Shape shape;

	public GenericShapeDescriptor(Shape shape) {
		this.shape = shape;
	}

	@Override
	public Shape getShapeOutput() { return UIObjectUtilities.copyShape(getShape()); }

	protected Shape getShape() { return shape; }

	protected void setShape(Shape shape) { this.shape = shape; }

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean crop(Rectangle2D bounds)
			throws IllegalStateException {
		super.crop(bounds);
		Rectangle2D currentBounds = getShape().getBounds2D();
		Rectangle2D.intersect(currentBounds, bounds, currentBounds);
		setShape(AffineTransformUtilities.getTransformFromTo(getShape().getBounds2D(), currentBounds).createTransformedShape(getShape()));
		return true;
	}

	@Override
	public boolean adapt(Rectangle2D frame) throws IllegalStateException {
		super.adapt(frame);
		setShape(AffineTransformUtilities.getTransformFromTo(getShape().getBounds2D(), frame).createTransformedShape(getShape()));
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
