package $group__.ui.structures.shapes.descriptors.builders;

import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptorBuilder;
import $group__.ui.structures.shapes.descriptors.RectangularShapeDescriptor;
import $group__.ui.utilities.UIObjectUtilities;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

public abstract class RectangularShapeDescriptorBuilder<S extends RectangularShape>
		extends AbstractShapeDescriptorBuilder<S> {
	protected RectangularShapeDescriptorBuilder(Class<S> genericClass) { super(genericClass); }

	@SuppressWarnings("Convert2MethodRef")
	@Override
	public IShapeDescriptor<S> build() {
		S s = createRectangularShape();
		UIObjectUtilities.acceptRectangular(getBounds(), (x, y, w, h) -> s.setFrame(x, y, w, h)); // COMMENT javac bug, no method ref here
		UIObjectUtilities.transformRectangular(getTransform(), s);
		IShapeDescriptor<S> ret = new RectangularShapeDescriptor<>(s);
		IShapeDescriptorBuilder.addUIObjects(ret, getConstraints());
		return ret;
	}

	protected abstract S createRectangularShape();

	public static class Rectangle2DSD
			extends RectangularShapeDescriptorBuilder<Rectangle2D> {
		public Rectangle2DSD() { super(Rectangle2D.class); }

		@Override
		protected Rectangle2D createRectangularShape() { return new Rectangle2D.Double(); }
	}

	public static class Ellipse2DSD
			extends RectangularShapeDescriptorBuilder<Ellipse2D> {
		public Ellipse2DSD() { super(Ellipse2D.class); }

		@Override
		protected Ellipse2D createRectangularShape() { return new Ellipse2D.Double(); }
	}
}
