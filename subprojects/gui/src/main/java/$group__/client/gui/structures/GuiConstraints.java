package $group__.client.gui.structures;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

@OnlyIn(Dist.CLIENT)
public final class GuiConstraints implements Consumer<Rectangle2D> {
	public static final int CONSTRAINT_NONE_VALUE = -1;
	public static final GuiConstraints CONSTRAINTS_NONE = new GuiConstraints(
			new Rectangle(CONSTRAINT_NONE_VALUE, CONSTRAINT_NONE_VALUE, CONSTRAINT_NONE_VALUE, CONSTRAINT_NONE_VALUE),
			new Rectangle(CONSTRAINT_NONE_VALUE, CONSTRAINT_NONE_VALUE, CONSTRAINT_NONE_VALUE, CONSTRAINT_NONE_VALUE));

	public final Rectangle2D min, max;
	@Nullable
	private Rectangle2D minLastCorrected, maxLastCorrected;

	public GuiConstraints(Rectangle2D min, Rectangle2D max) {
		this.min = min;
		this.max = max;
	}

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	public static boolean isNoneValue(double value) { return value == CONSTRAINT_NONE_VALUE; }

	private static void correct(double min, double max, DoubleConsumer minSetter, DoubleConsumer maxSetter) {
		if (!isNoneValue(min) && !isNoneValue(max) && min > max) {
			minSetter.accept(max);
			maxSetter.accept(min);
		}
	}

	public void accept(Rectangle2D rectangle) {
		double x = rectangle.getX(),
				y = rectangle.getY(),
				width = rectangle.getWidth(),
				height = rectangle.getHeight();
		if (!min.equals(minLastCorrected) || !max.equals(maxLastCorrected)) correct();
		if (!isNoneValue(min.getX())) x = Math.max(x, min.getX());
		if (!isNoneValue(max.getX())) x = Math.min(x, max.getX());
		if (!isNoneValue(min.getY())) y = Math.max(y, min.getY());
		if (!isNoneValue(max.getY())) y = Math.min(y, max.getY());
		if (!isNoneValue(min.getWidth())) width = Math.max(width, min.getWidth());
		if (!isNoneValue(max.getWidth())) width = Math.min(width, max.getWidth());
		if (!isNoneValue(min.getHeight())) height = Math.max(height, min.getHeight());
		if (!isNoneValue(max.getHeight())) height = Math.min(height, max.getHeight());
		rectangle.setRect(x, y, width, height);
	}

	private void correct() {
		correct(min.getX(), max.getX(),
				d -> min.setRect(d, min.getY(), min.getWidth(), min.getHeight()),
				d -> max.setRect(d, max.getY(), max.getWidth(), max.getHeight()));
		correct(min.getY(), max.getY(),
				d -> min.setRect(min.getX(), d, min.getWidth(), min.getHeight()),
				d -> max.setRect(max.getX(), d, max.getWidth(), max.getHeight()));
		correct(min.getY(), max.getY(),
				d -> min.setRect(min.getX(), min.getY(), d, min.getHeight()),
				d -> max.setRect(max.getX(), max.getY(), d, max.getHeight()));
		correct(min.getY(), max.getY(),
				d -> min.setRect(min.getX(), min.getY(), min.getWidth(), d),
				d -> max.setRect(max.getX(), max.getY(), max.getWidth(), d));
		minLastCorrected = (Rectangle2D) min.clone();
		maxLastCorrected = (Rectangle2D) max.clone();
	}
}
