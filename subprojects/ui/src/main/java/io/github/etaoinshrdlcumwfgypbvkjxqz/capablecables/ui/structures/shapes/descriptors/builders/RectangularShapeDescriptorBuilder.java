package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures.shapes.descriptors.builders;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.descriptors.IShapeDescriptorBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures.shapes.descriptors.RectangularShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.ImmutableRectangle2D;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ui.UIObjectUtilities;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

public abstract class RectangularShapeDescriptorBuilder<S extends RectangularShape>
		extends AbstractShapeDescriptorBuilder<S> {
	protected RectangularShapeDescriptorBuilder(Class<S> genericClass) { super(genericClass); }

	@SuppressWarnings("Convert2MethodRef")
	@Override
	public IShapeDescriptor<S> build() {
		S s = createRectangularShape(); // COMMENT must be mutable
		UIObjectUtilities.acceptRectangular(getBounds(), (x, y, w, h) -> s.setFrame(x, y, w, h)); // COMMENT javac bug, no method ref here
		s.setFrame(UIObjectUtilities.transformRectangular(getTransform(), s, ImmutableRectangle2D::of));
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
