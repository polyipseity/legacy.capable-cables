package $group__.client.gui.components;

import $group__.client.gui.components.roots.GuiRoot;
import $group__.client.gui.structures.GuiConstraint;
import $group__.client.gui.structures.GuiDragInfo;
import $group__.client.gui.traits.IGuiReRectangleHandler;
import $group__.client.gui.utilities.GuiUtilities;
import $group__.utilities.helpers.specific.ThrowableUtilities.BecauseOf;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import static $group__.client.gui.structures.GuiConstraint.CONSTRAINT_NONE_VALUE;
import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public class GuiWindow extends GuiContainer implements IGuiReRectangleHandler {
	// TODO make value not hardcoded through themes
	public static final int
			WINDOW_RE_RECTANGLE_THICKNESS = 5, // COMMENT external
			WINDOW_DRAG_BAR_THICKNESS = 5, // COMMENT internal top
			WINDOW_VISIBLE_BORDER_MINIMUM = 5;
	public Color
			colorBorder,
			colorBackground,
			colorDragging;

	public GuiWindow(Rectangle2D rectangle, Color colorBorder, Color colorBackground, Color colorDragging) {
		super(rectangle);
		this.colorBorder = colorBorder;
		this.colorBackground = colorBackground;
		this.colorDragging = colorDragging;
	}

	@Override
	protected MatrixStack transformMatrixForComponent(MatrixStack matrix, GuiComponent component) {
		MatrixStack mat = super.transformMatrixForComponent(matrix, component);
		mat.translate(0, WINDOW_DRAG_BAR_THICKNESS, 0);
		return mat;
	}

	@Override
	public void render(MatrixStack matrix, Point2D mouse, float partialTicks) {
		if (EnumState.READY.isReachedBy(getState())) {
			Matrix4f mat = matrix.getLast().getMatrix();
			GuiUtilities.fill(mat,
					new Point(0, 0),
					new Point2D.Double(getRectangle().getWidth(), Math.min(getRectangle().getHeight(), WINDOW_DRAG_BAR_THICKNESS)),
					colorBorder.getRGB());
			if (getRectangle().getHeight() > WINDOW_DRAG_BAR_THICKNESS)
				GuiUtilities.fill(mat,
						new Point(0, WINDOW_DRAG_BAR_THICKNESS),
						new Point2D.Double(getRectangle().getWidth(), getRectangle().getHeight()),
						colorBackground.getRGB());
			super.render(matrix, mouse, partialTicks);
			if (isBeingDragged()) {
				GuiDragInfo drag = getDragForThis().orElseThrow(BecauseOf::unexpected);
				GL11.glDisable(GL11.GL_SCISSOR_TEST);
				double x = mouse.getX() - drag.start.getX(),
						y = mouse.getY() - drag.start.getY();
				GuiUtilities.drawRectangle(mat,
						new Point2D.Double(x, y),
						new Point2D.Double(x + getRectangle().getWidth(), y + getRectangle().getHeight()),
						colorDragging.getRGB());
				GL11.glEnable(GL11.GL_SCISSOR_TEST);
			}
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

	@Override
	public boolean onMouseClicked(MatrixStack matrix, Point2D mouse, int button) { return super.onMouseClicked(matrix, mouse, button) || toRelativePointWithMatrix(matrix.getLast().getMatrix(), mouse).getY() < WINDOW_DRAG_BAR_THICKNESS; }

	@Override
	public boolean onMouseDragging(MatrixStack matrix, Rectangle2D mouse, int button) { return super.onMouseDragging(matrix, mouse, button) || isBeingDragged(); }

	@Override
	public boolean onMouseDragged(MatrixStack matrix, GuiDragInfo drag, Point2D mouse, int button) {
		Rectangle2D rect = getRectangleView();
		rect.setRect(rect.getX() + mouse.getX() - drag.start.getX(), rect.getY() + mouse.getY() - drag.start.getY(),
				rect.getWidth(), rect.getHeight());
		reRectangle(this, rect);
		return true;
	}
}
