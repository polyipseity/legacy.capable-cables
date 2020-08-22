package $group__.client.ui.structures.shapes.descriptors;

import $group__.client.ui.utilities.UIObjectUtilities;
import $group__.utilities.client.AffineTransformUtilities;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class GenericShapeDescriptor extends AbstractShapeDescriptor<Shape> {
	protected Shape shape;

	public GenericShapeDescriptor(Shape shape) {
		this.shape = shape;
	}

	@Override
	public Shape getShapeOutput() { return UIObjectUtilities.copyShape(getShape()); }

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
