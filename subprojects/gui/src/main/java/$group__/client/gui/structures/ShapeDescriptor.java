package $group__.client.gui.structures;

import $group__.client.gui.utilities.TransformUtilities.AffineTransformUtilities;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.List;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public abstract class ShapeDescriptor<S extends Shape> {
	protected S shape;

	public ShapeDescriptor(S shape) { this.shape = shape; }

	public void constrain(List<GuiConstraint> constraint) {
		Rectangle2D to = getShape().getBounds2D();
		constraint.forEach(c -> c.accept(to));
		adapt(to);
	}

	public S getShape() { return shape; }

	public abstract void adapt(Rectangle2D bounds);

	@OnlyIn(CLIENT)
	public static class Generic extends ShapeDescriptor<Shape> {
		public Generic(Shape shape) { super(shape); }

		@Override
		public void adapt(Rectangle2D bounds) { shape = AffineTransformUtilities.getTransformFromTo(getShape().getBounds2D(), bounds).createTransformedShape(getShape()); }
	}

	@OnlyIn(CLIENT)
	public static class Rectangular<S extends RectangularShape> extends ShapeDescriptor<S> {
		public Rectangular(S shape) { super(shape); }

		@Override
		public void adapt(Rectangle2D bounds) { shape.setFrame(bounds); }
	}
}
