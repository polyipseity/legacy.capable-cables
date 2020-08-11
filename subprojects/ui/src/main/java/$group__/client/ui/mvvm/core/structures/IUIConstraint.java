package $group__.client.ui.mvvm.core.structures;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Rectangle2D;
import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public interface IUIConstraint extends Consumer<Rectangle2D> {
	int CONSTRAINT_NULL_VALUE = -1;

	@Override
	default void accept(Rectangle2D rectangle) { constrain(rectangle); }

	default void constrain(Rectangle2D rectangle) {
		Rectangle2D min = getRectangleMinView(), max = getRectangleMaxView();
		double x = rectangle.getX(),
				y = rectangle.getY(),
				width = rectangle.getWidth(),
				height = rectangle.getHeight();
		if (!isNull(min.getX()))
			x = Math.max(x, min.getX());
		if (!isNull(max.getX()))
			x = Math.min(x, max.getX());
		if (!isNull(min.getY()))
			y = Math.max(y, min.getY());
		if (!isNull(max.getY()))
			y = Math.min(y, max.getY());
		if (!isNull(min.getWidth()))
			width = Math.max(width, min.getWidth());
		if (!isNull(max.getWidth()))
			width = Math.min(width, max.getWidth());
		if (!isNull(min.getHeight()))
			height = Math.max(height, min.getHeight());
		if (!isNull(max.getHeight()))
			height = Math.min(height, max.getHeight());
		rectangle.setRect(x, y, width, height);
	}

	Rectangle2D getRectangleMinView();

	Rectangle2D getRectangleMaxView();

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	static boolean isNull(double v) { return v == CONSTRAINT_NULL_VALUE; }

	@OnlyIn(Dist.CLIENT)
	enum Constants {
		;

		private static final Rectangle2D CONSTRAINT_NULL_RECTANGLE = new Rectangle2D.Double(CONSTRAINT_NULL_VALUE, CONSTRAINT_NULL_VALUE, CONSTRAINT_NULL_VALUE, CONSTRAINT_NULL_VALUE);

		public static Rectangle2D getConstraintNullRectangleView() { return (Rectangle2D) CONSTRAINT_NULL_RECTANGLE.clone(); }
	}
}
