package $group__.client.gui.components;

import $group__.client.gui.structures.GuiConstraint;
import $group__.client.gui.traits.IGuiReRectangleHandler;
import $group__.client.gui.utilities.GuiUtilities;
import $group__.utilities.helpers.specific.ThrowableUtilities.BecauseOf;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import static $group__.client.gui.structures.GuiConstraint.CONSTRAINT_NONE_VALUE;
import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public class GuiWindow extends GuiContainer implements IGuiReRectangleHandler {
	public static final int WINDOW_VISIBLE_BORDER_MINIMUM = 5;
	public Color color;

	public GuiWindow(Rectangle2D rectangle, Color color) {
		super(rectangle);
		this.color = color;
	}

	@Override
	public void render(MatrixStack matrix, Point2D mouse, float partialTicks) {
		GuiUtilities.fill(matrix.getLast().getMatrix(), (int) getRectangle().getX(), (int) getRectangle().getY(), (int) getRectangle().getMaxX(), (int) getRectangle().getMaxY(), color.getRGB());
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
