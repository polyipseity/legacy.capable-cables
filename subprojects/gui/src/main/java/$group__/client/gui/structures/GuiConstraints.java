package $group__.client.gui.structures;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public final class GuiConstraints implements Consumer<Rectangle2D> {
	public static final int CONSTRAINT_NONE_VALUE = -1;
	public static final GuiConstraints CONSTRAINTS_NONE = new GuiConstraints(new Dimension(CONSTRAINT_NONE_VALUE, CONSTRAINT_NONE_VALUE), new Dimension(CONSTRAINT_NONE_VALUE, CONSTRAINT_NONE_VALUE));

	public final Dimension2D min, max;

	public GuiConstraints(Dimension2D min, Dimension2D max) {
		this.min = min;
		this.max = max;
	}

	public void accept(Rectangle2D rectangle) {
		double width = rectangle.getWidth(), height = rectangle.getHeight();
		if (min.getWidth() != CONSTRAINT_NONE_VALUE) width = Math.max(width, min.getWidth());
		if (min.getHeight() != CONSTRAINT_NONE_VALUE) height = Math.max(height, min.getHeight());
		if (max.getWidth() != CONSTRAINT_NONE_VALUE) width = Math.min(width, max.getWidth());
		if (max.getHeight() != CONSTRAINT_NONE_VALUE) height = Math.min(height, max.getHeight());
		rectangle.setRect(rectangle.getX(), rectangle.getY(), width, height);
	}
}
