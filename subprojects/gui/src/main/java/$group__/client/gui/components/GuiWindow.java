package $group__.client.gui.components;

import $group__.client.gui.structures.GuiConstraint;
import $group__.client.gui.traits.IGuiReRectangleHandler;
import $group__.utilities.helpers.specific.ThrowableUtilities.BecauseOf;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import static $group__.client.gui.structures.GuiConstraint.CONSTRAINT_NONE_VALUE;
import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public class GuiWindow extends GuiContainer implements IGuiReRectangleHandler {
	public static final int WINDOW_VISIBLE_BORDER_MINIMUM = 5;

	public GuiWindow(Rectangle2D rectangle) { super(rectangle); }

	@Override
	public void render(MatrixStack matrix, Point2D mouse, float partialTicks) {
		super.render(matrix, mouse, partialTicks);
	}

	@Override
	public void setRectangle(IGuiReRectangleHandler handler, GuiComponent invoker, Rectangle2D rectangle) {
		Rectangle2D rectangleRoot = getRoot().orElseThrow(BecauseOf::unexpected).getRectangleView();
		GuiConstraint constraint = new GuiConstraint(GuiConstraint.getConstraintRectangleNone(), new Rectangle2D.Double(rectangleRoot.getMaxX() - WINDOW_VISIBLE_BORDER_MINIMUM, rectangleRoot.getMaxY() - WINDOW_VISIBLE_BORDER_MINIMUM, CONSTRAINT_NONE_VALUE, CONSTRAINT_NONE_VALUE));
		constraints.add(constraint);
		super.setRectangle(handler, invoker, rectangle);
		constraints.remove(constraint);
	}

	@Override
	public void reRectangle(GuiComponent invoker) { reRectangle(invoker, getRectangle()); }

	@Override
	public void reRectangle(GuiComponent invoker, Rectangle2D rectangle) { setRectangle(this, invoker, rectangle); }
}
