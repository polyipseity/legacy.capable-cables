package $group__.client.gui.components;

import $group__.client.gui.components.roots.GuiRoot;
import $group__.client.gui.structures.AffineTransformStack;
import $group__.client.gui.structures.GuiConstraint;
import $group__.client.gui.structures.GuiDragInfo;
import $group__.client.gui.traits.IGuiShapeRectangle;
import $group__.client.gui.traits.handlers.IGuiReshapeHandler;
import $group__.client.gui.utilities.GuiUtilities;
import $group__.client.gui.utilities.GuiUtilities.DrawingUtilities;
import $group__.utilities.helpers.specific.ThrowableUtilities;
import $group__.utilities.helpers.specific.ThrowableUtilities.BecauseOf;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import static $group__.client.gui.structures.GuiConstraint.CONSTRAINT_NONE_VALUE;
import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public class GuiWindow extends GuiContainer implements IGuiReshapeHandler, IGuiShapeRectangle {
	// TODO make value not hardcoded through themes
	public static final int
			WINDOW_RE_RECTANGLE_THICKNESS = 5, // COMMENT external
			WINDOW_DRAG_BAR_THICKNESS = 5, // COMMENT internal top
			WINDOW_VISIBLE_MINIMUM = 5;
	private static final Logger LOGGER = LogManager.getLogger();
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

	protected Rectangle2D getRectangle() { return (Rectangle2D) super.getShape(); }

	public Rectangle2D getRectangleView() { return (Rectangle2D) getRectangle().clone(); }

	@Override
	public void setBounds(IGuiReshapeHandler handler, GuiComponent invoker, Rectangle2D rectangle) {
		Rectangle2D rectangleRoot = getNearestParentThatIs(GuiRoot.class).orElseThrow(BecauseOf::unexpected).getRectangleView();
		GuiConstraint constraint = new GuiConstraint(new Rectangle2D.Double(0, 0, WINDOW_VISIBLE_MINIMUM, WINDOW_VISIBLE_MINIMUM), new Rectangle2D.Double(rectangleRoot.getMaxX() - WINDOW_VISIBLE_MINIMUM, rectangleRoot.getMaxY() - WINDOW_VISIBLE_MINIMUM, CONSTRAINT_NONE_VALUE, CONSTRAINT_NONE_VALUE));
		constraints.add(constraint);
		super.setBounds(handler, invoker, rectangle);
		constraints.remove(constraint);
	}

	@Override
	protected Shape adaptToBounds(IGuiReshapeHandler handler, GuiComponent invoker, Rectangle2D rectangle) { return rectangle; }

	@Override
	protected void transformTransform(AffineTransformStack stack) {
		super.transformTransform(stack);
		stack.delegated.peek().translate(0, WINDOW_DRAG_BAR_THICKNESS);
	}

	@Override
	public void render(AffineTransformStack stack, Point2D mouse, float partialTicks) {
		if (EnumState.READY.isReachedBy(getState())) {
			AffineTransform transform = stack.delegated.peek();
			DrawingUtilities.fill(transform, getRectangle(), colorBorder.getRGB());
			DrawingUtilities.fill(transform,
					GuiUtilities.ObjectUtilities.fromDiagonal(
							new Point2D.Double(getRectangle().getX(), getRectangle().getY() + WINDOW_DRAG_BAR_THICKNESS),
							new Point2D.Double(getRectangle().getMaxX(), getRectangle().getMaxY())
					), colorBackground.getRGB());
			super.render(stack, mouse, partialTicks);
			if (isBeingDragged()) {
				GuiDragInfo drag = getDragForThis().orElseThrow(BecauseOf::unexpected);
				GL11.glDisable(GL11.GL_SCISSOR_TEST);
				Point2D start, end;
				try {
					start = ReferenceConverters.toRelativePoint(transform, drag.start);
					end = ReferenceConverters.toRelativePoint(transform, mouse);
				} catch (NoninvertibleTransformException ex) {
					ThrowableUtilities.ThrowableCatcher.log(ex, LOGGER);
					return;
				}
				DrawingUtilities.drawRectangle(transform,
						new Rectangle2D.Double(
								getRectangle().getX() + end.getX() - start.getX(), getRectangle().getY() + end.getY() - start.getY(),
								getRectangle().getWidth(), getRectangle().getHeight()
						), colorDragging.getRGB());
				//GL11.glEnable(GL11.GL_SCISSOR_TEST);
			}
		}
	}

	@Override
	public void reshape(GuiComponent invoker) { reshape(invoker, getRectangle()); }

	@Override
	public void reshape(GuiComponent invoker, Shape shape) { setBounds(this, invoker, shape.getBounds2D()); }

	@Override
	public boolean onMouseClicked(AffineTransformStack stack, Point2D mouse, int button) {
		return super.onMouseClicked(stack, mouse, button) ||
				stack.delegated.peek().createTransformedShape(
						new Rectangle2D.Double(getRectangle().getX(), getRectangle().getY(),
								getRectangle().getWidth(), WINDOW_DRAG_BAR_THICKNESS)).contains(mouse);
	}

	@Override
	public boolean onMouseDragging(AffineTransformStack stack, Rectangle2D mouse, int button) { return super.onMouseDragging(stack, mouse, button) || isBeingDragged(); }

	@Override
	public boolean onMouseDragged(AffineTransformStack stack, GuiDragInfo drag, Point2D mouse, int button) {
		AffineTransform transform = stack.delegated.peek();
		Point2D start, end;
		try {
			start = ReferenceConverters.toRelativePoint(transform, drag.start);
			end = ReferenceConverters.toRelativePoint(transform, mouse);
		} catch (NoninvertibleTransformException ex) {
			ThrowableUtilities.ThrowableCatcher.log(ex, LOGGER);
			return false;
		}
		Rectangle2D r = getRectangle();
		r.setRect(r.getX() + end.getX() - start.getX(), r.getY() + end.getY() - start.getY(),
				r.getWidth(), r.getHeight());
		reshape(this, r);
		return true;
	}
}
