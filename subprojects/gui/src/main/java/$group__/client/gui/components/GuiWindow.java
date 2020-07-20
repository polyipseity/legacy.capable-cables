package $group__.client.gui.components;

import $group__.client.gui.components.roots.GuiRoot;
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
	// TODO make value not hardcoded through themes
	public static final int
			WINDOW_RE_RECTANGLE_WIDTH = 5, // COMMENT external
			WINDOW_DRAG_BAR_WIDTH = 5, // COMMENT internal top
			WINDOW_VISIBLE_BORDER_MINIMUM = 5;
	public Color color;

	public GuiWindow(Rectangle2D rectangle, Color color) {
		super(rectangle);
		this.color = color;
	}

	@Override
	protected MatrixStack transformMatrixForComponent(MatrixStack matrix, GuiComponent component) {
		MatrixStack mat = super.transformMatrixForComponent(matrix, component);
		mat.translate(0, WINDOW_DRAG_BAR_WIDTH, 0);
		return mat;
	}

	@Override
	public void render(MatrixStack matrix, Point2D mouse, float partialTicks) {
		if (EnumState.READY.isReachedBy(getState())) {
			GuiUtilities.fill(matrix.getLast().getMatrix(), 0, 0, (int) getRectangle().getWidth(), (int) getRectangle().getHeight(), color.getRGB());
			super.render(matrix, mouse, partialTicks);
		}
	}

	@Override
	public void setRectangle(IGuiReRectangleHandler handler, GuiComponent invoker, Rectangle2D rectangle) {
		Rectangle2D rectangleRoot = getNearestParentThatIs(GuiRoot.class).orElseThrow(BecauseOf::unexpected).getRectangleView();
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
