package $group__.client.ui.structures.shapes.descriptors;

import $group__.client.ui.utilities.UIObjectUtilities;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

public class RectangularShapeDescriptor<S extends RectangularShape>
		extends AbstractShapeDescriptor<S> {
	protected S shape;

	public RectangularShapeDescriptor(S shape) { this.shape = shape; }

	@SuppressWarnings("unchecked")
	@Override
	public S getShapeOutput() {
		return (S) getShape().clone(); // COMMENT cloning should return itself
	}

	protected S getShape() { return shape; }

	protected void setShape(S shape) { this.shape = shape; }

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean bound(Rectangle2D bounds)
			throws IllegalStateException {
		super.bound(bounds);
		getShape().setFrame(bounds);
		return true;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean transform(AffineTransform transform)
			throws IllegalStateException {
		super.transform(transform);
		UIObjectUtilities.transformRectangular(transform, getShape());
		return true;
	}
}
