package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.descriptors.builders;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors.IShapeDescriptorBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.descriptors.RectangularShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.graphics.impl.UIObjectUtilities;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressUnboxing;

public abstract class RectangularShapeDescriptorBuilder<S extends RectangularShape>
		extends AbstractShapeDescriptorBuilder<S> {
	protected RectangularShapeDescriptorBuilder(Class<S> genericClass) { super(genericClass); }

	@Override
	public IShapeDescriptor<S> build() {
		S s = createRectangularShape();
		UIObjectUtilities.acceptRectangularShape(getBounds(), (x, y, width, height) ->
				s.setFrame(suppressUnboxing(x), suppressUnboxing(y),
						suppressUnboxing(width), suppressUnboxing(height))); // TODO javac bug, no method ref here
		UIObjectUtilities.transformRectangularShape(getTransform(), s, s);
		IShapeDescriptor<S> ret = new RectangularShapeDescriptor<>(s);
		IShapeDescriptorBuilder.addUIObjects(ret, getConstraints().iterator());
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
