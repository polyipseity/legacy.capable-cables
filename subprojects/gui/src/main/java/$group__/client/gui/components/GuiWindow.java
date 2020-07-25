package $group__.client.gui.components;

import $group__.client.gui.components.roots.GuiRoot;
import $group__.client.gui.structures.*;
import $group__.client.gui.structures.GuiCache.CacheKey;
import $group__.client.gui.traits.IGuiShapeRectangle;
import $group__.client.gui.traits.handlers.IGuiReshapeHandler;
import $group__.client.gui.utilities.GLUtilities;
import $group__.client.gui.utilities.GuiUtilities.DrawingUtilities;
import $group__.client.gui.utilities.GuiUtilities.ObjectUtilities;
import $group__.client.gui.utilities.GuiUtilities.UnboxingUtilities;
import $group__.utilities.specific.ThrowableUtilities.BecauseOf;
import $group__.utilities.specific.ThrowableUtilities.ThrowableCatcher;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.EnumSet;

import static $group__.client.gui.structures.GuiConstraint.CONSTRAINT_NONE_VALUE;
import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public class GuiWindow extends GuiContainer implements IGuiReshapeHandler, IGuiShapeRectangle {
	// TODO make value not hardcoded through themes
	public static final int
			WINDOW_RESHAPE_THICKNESS = 5, // COMMENT external
			WINDOW_DRAG_BAR_THICKNESS = 5, // COMMENT internal top
			WINDOW_VISIBLE_MINIMUM = 5;
	private static final Logger LOGGER = LogManager.getLogger();
	public Color
			colorBorder,
			colorBackground,
			colorDragging;
	protected Rectangle2D
			rectangleDraggable,
			rectangleClickable;
	@Nullable
	protected Point2D resizeBase = null;
	@Nullable
	protected EnumCursor cursor = null;
	@Nullable
	protected GuiDragInfoWindow dragInfoWindow = null;


	public GuiWindow(Rectangle2D rectangle, Color colorBorder, Color colorBackground, Color colorDragging) {
		super(rectangle);
		rectangleDraggable = new Rectangle2D.Double(rectangle.getX(), rectangle.getY(),
				rectangle.getWidth(), WINDOW_DRAG_BAR_THICKNESS);
		rectangleClickable = UnboxingUtilities.applyRectangle(rectangle,
				(x, y) -> (w, h) -> new Rectangle2D.Double(x - WINDOW_RESHAPE_THICKNESS, y - WINDOW_RESHAPE_THICKNESS, w + WINDOW_RESHAPE_THICKNESS * 2, h + WINDOW_RESHAPE_THICKNESS * 2));
		this.colorBorder = colorBorder;
		this.colorBackground = colorBackground;
		this.colorDragging = colorDragging;
	}

	protected Rectangle2D getRectangle() { return (Rectangle2D) super.getShape(); }

	public Rectangle2D getRectangleView() { return (Rectangle2D) getRectangle().clone(); }

	@Override
	public void setBounds(IGuiReshapeHandler handler, GuiComponent invoker, Rectangle2D rectangle) {
		GuiRoot<?> root = CacheKey.ROOT.get(this);
		Rectangle2D rRoot = root.getRectangleView();
		GuiConstraint constraint = new GuiConstraint(new Rectangle2D.Double(0, 0, WINDOW_VISIBLE_MINIMUM, WINDOW_VISIBLE_MINIMUM), new Rectangle2D.Double(rRoot.getMaxX() - WINDOW_VISIBLE_MINIMUM, rRoot.getMaxY() - WINDOW_VISIBLE_MINIMUM, CONSTRAINT_NONE_VALUE, CONSTRAINT_NONE_VALUE));
		constraints.add(constraint);
		super.setBounds(handler, invoker, rectangle);
		constraints.remove(constraint);
	}

	@Override
	protected Shape adaptToBounds(IGuiReshapeHandler handler, GuiComponent invoker, Rectangle2D rectangle) {
		rectangleDraggable = new Rectangle2D.Double(rectangle.getX(), rectangle.getY(),
				rectangle.getWidth(), WINDOW_DRAG_BAR_THICKNESS);
		rectangleClickable = UnboxingUtilities.applyRectangle(rectangle,
				(x, y) -> (w, h) -> new Rectangle2D.Double(x - WINDOW_RESHAPE_THICKNESS, y - WINDOW_RESHAPE_THICKNESS, w + WINDOW_RESHAPE_THICKNESS * 2, h + WINDOW_RESHAPE_THICKNESS * 2));
		return rectangle;
	}

	@Override
	protected void transformTransform(AffineTransformStack stack) {
		super.transformTransform(stack);
		stack.delegated.peek().translate(0, WINDOW_DRAG_BAR_THICKNESS);
	}

	@Override
	public void render(AffineTransformStack stack, Point2D mouse, float partialTicks) {
		if (EnumGuiState.READY.isReachedBy(getState())) {
			AffineTransform transform = stack.delegated.peek();
			DrawingUtilities.fill(transform, getRectangle(), colorBorder.getRGB());
			DrawingUtilities.fill(transform,
					ObjectUtilities.getRectangleFromDiagonal(
							new Point2D.Double(getRectangle().getX(), getRectangle().getY() + WINDOW_DRAG_BAR_THICKNESS),
							new Point2D.Double(getRectangle().getMaxX(), getRectangle().getMaxY())
					), colorBackground.getRGB());
			super.render(stack, mouse, partialTicks);
			if (isBeingDragged()) {
				Rectangle2D r = getRectangleView();
				assert dragInfoWindow != null;
				dragInfoWindow.handle(transform, r, mouse);
				GLUtilities.push("GL_SCISSOR_TEST",
						() -> GL11.glDisable(GL11.GL_SCISSOR_TEST),
						() -> GL11.glDisable(GL11.GL_SCISSOR_TEST));
				DrawingUtilities.drawRectangle(transform, r, colorDragging.getRGB());
				GLUtilities.pop("GL_SCISSOR_TEST");
			}
		}
	}

	@Override
	public void reshape(GuiComponent invoker) { reshape(invoker, getRectangle()); }

	@Override
	public void reshape(GuiComponent invoker, Shape shape) { setBounds(this, invoker, shape.getBounds2D()); }

	@Override
	public void onMouseHovering(AffineTransformStack stack, Point2D mouse) {
		super.onMouseHovering(stack, mouse);
		if (isBeingHovered() && !isBeingDragged()) {
			Shape transformed = stack.delegated.peek().createTransformedShape(getShape());
			if (!transformed.contains(mouse)) {
				EnumSet<EnumGuiSide> sides = EnumGuiSide.getSidesMouseOver(transformed.getBounds2D(), mouse);
				if (sides.contains(EnumGuiSide.UP) && sides.contains(EnumGuiSide.LEFT)
						|| sides.contains(EnumGuiSide.DOWN) && sides.contains(EnumGuiSide.RIGHT))
					cursor = EnumCursor.EXTENSION_RESIZE_NW_SE_CURSOR;
				else if (sides.contains(EnumGuiSide.UP) && sides.contains(EnumGuiSide.RIGHT)
						|| sides.contains(EnumGuiSide.DOWN) && sides.contains(EnumGuiSide.LEFT))
					cursor = EnumCursor.EXTENSION_RESIZE_NE_SW_CURSOR;
				else if (sides.contains(EnumGuiSide.LEFT) || sides.contains(EnumGuiSide.RIGHT))
					cursor = EnumCursor.STANDARD_RESIZE_HORIZONTAL_CURSOR;
				else if (sides.contains(EnumGuiSide.UP) || sides.contains(EnumGuiSide.DOWN))
					cursor = EnumCursor.STANDARD_RESIZE_VERTICAL_CURSOR;
				else throw BecauseOf.unexpected();
				GLFW.glfwSetCursor(GLUtilities.getWindowHandle(), cursor.handle);
			} else {
				cursor = null;
				GLFW.glfwSetCursor(GLUtilities.getWindowHandle(), MemoryUtil.NULL);
			}
		}
	}

	@Override
	public void onMouseHovered(AffineTransformStack stack, Point2D mouse) {
		super.onMouseHovered(stack, mouse);
		cursor = null;
		GLFW.glfwSetCursor(GLUtilities.getWindowHandle(), MemoryUtil.NULL);
	}

	@Override
	public EnumGuiMouseClickResult onMouseClicked(AffineTransformStack stack, GuiDragInfo drag, Point2D mouse, int button) {
		EnumGuiMouseClickResult ret = super.onMouseClicked(stack, drag, mouse, button);
		if (ret.result || button != GLFW.GLFW_MOUSE_BUTTON_LEFT) return ret;
		if (stack.delegated.peek().createTransformedShape(rectangleDraggable).contains(mouse)) {
			dragInfoWindow = new GuiDragInfoWindow(drag, EnumGuiDragType.REPOSITION, null);
			ret = EnumGuiMouseClickResult.DRAG;
		} else {
			Shape transformed = stack.delegated.peek().createTransformedShape(getShape());
			if (!transformed.contains(mouse)) {
				EnumSet<EnumGuiSide> sides = EnumGuiSide.getSidesMouseOver(transformed.getBounds2D(), mouse);
				dragInfoWindow = new GuiDragInfoWindow(drag, EnumGuiDragType.RESIZE, sides);
				if (sides.contains(EnumGuiSide.UP) && sides.contains(EnumGuiSide.LEFT))
					resizeBase = new Point2D.Double(getRectangle().getMaxX(), getRectangle().getMaxY());
				else if (sides.contains(EnumGuiSide.DOWN) && sides.contains(EnumGuiSide.RIGHT))
					resizeBase = new Point2D.Double(getRectangle().getX(), getRectangle().getY());
				else if (sides.contains(EnumGuiSide.UP) && sides.contains(EnumGuiSide.RIGHT))
					resizeBase = new Point2D.Double(getRectangle().getX(), getRectangle().getMaxY());
				else if (sides.contains(EnumGuiSide.DOWN) && sides.contains(EnumGuiSide.LEFT))
					resizeBase = new Point2D.Double(getRectangle().getMaxX(), getRectangle().getY());
				ret = EnumGuiMouseClickResult.DRAG;
			}
		}
		if (ret.result) getParent().orElseThrow(BecauseOf::unexpected).moveToTop(this);
		return ret;
	}

	@Override
	public boolean onMouseDragging(AffineTransformStack stack, GuiDragInfo drag, Rectangle2D mouse, int button) {
		if (super.onMouseDragging(stack, drag, mouse, button))
			return true;
		if (isBeingDragged()) {
			if (cursor != null) {
				if (cursor == EnumCursor.EXTENSION_RESIZE_NW_SE_CURSOR
						|| cursor == EnumCursor.EXTENSION_RESIZE_NE_SW_CURSOR) {
					assert resizeBase != null;
					EnumSet<EnumGuiSide> sides = EnumGuiSide.getSidesMouseOver(new Rectangle2D.Double(resizeBase.getX(), resizeBase.getY(), 0, 0), new Point2D.Double(mouse.getMaxX(), mouse.getMaxY()));
					if (sides.contains(EnumGuiSide.UP) && sides.contains(EnumGuiSide.LEFT)
							|| sides.contains(EnumGuiSide.DOWN) && sides.contains(EnumGuiSide.RIGHT))
						cursor = EnumCursor.EXTENSION_RESIZE_NW_SE_CURSOR;
					else if (sides.contains(EnumGuiSide.UP) && sides.contains(EnumGuiSide.RIGHT)
							|| sides.contains(EnumGuiSide.DOWN) && sides.contains(EnumGuiSide.LEFT))
						cursor = EnumCursor.EXTENSION_RESIZE_NE_SW_CURSOR;
				}
				GLFW.glfwSetCursor(GLUtilities.getWindowHandle(), cursor.handle);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean onMouseDragged(AffineTransformStack stack, GuiDragInfo drag, Point2D mouse, int button) {
		Rectangle2D r = getRectangle();
		assert dragInfoWindow != null;
		dragInfoWindow.handle(stack.delegated.peek(), r, mouse);
		reshape(this, r);
		resizeBase = null;
		dragInfoWindow = null;
		return true;
	}

	@Override
	public boolean isMouseOver(AffineTransformStack stack, Point2D mouse) { return isBeingDragged() || stack.delegated.peek().createTransformedShape(rectangleClickable).contains(mouse); }

	@OnlyIn(CLIENT)
	public enum EnumGuiDragType {
		REPOSITION,
		RESIZE,
	}

	@OnlyIn(CLIENT)
	@Immutable
	public static final class GuiDragInfoWindow {
		public final GuiDragInfo decorated;
		public final EnumGuiDragType type;
		@Nullable
		public final EnumSet<EnumGuiSide> sides;

		public GuiDragInfoWindow(GuiDragInfo decorated, EnumGuiDragType type, @Nullable EnumSet<EnumGuiSide> sides) {
			this.decorated = decorated;
			this.type = type;
			this.sides = sides;

			if (type == EnumGuiDragType.RESIZE) {
				if (sides == null)
					throw BecauseOf.illegalArgument("type", type, "sides", null);
				if (sides.contains(EnumGuiSide.HORIZONTAL) || sides.contains(EnumGuiSide.VERTICAL))
					throw BecauseOf.illegalArgument("sides", sides);
				sides.forEach(s -> {
					if (sides.contains(s.getOpposite()))
						throw BecauseOf.illegalArgument("sides", sides);
				});
			} else {
				if (sides != null)
					throw BecauseOf.illegalArgument("type", type, "sides", sides);
			}
		}

		public void handle(AffineTransform transform, Rectangle2D rectangle, Point2D mouse) {
			Point2D start, end;
			try {
				start = ReferenceConverters.toRelativePoint(transform, decorated.start);
				end = ReferenceConverters.toRelativePoint(transform, mouse);
			} catch (NoninvertibleTransformException ex) {
				ThrowableCatcher.log(ex, LOGGER);
				return;
			}
			switch (type) {
				case REPOSITION:
					rectangle.setRect(rectangle.getX() + end.getX() - start.getX(), rectangle.getY() + end.getY() - start.getY(),
							rectangle.getWidth(), rectangle.getHeight());
					break;
				case RESIZE:
					assert sides != null;
					for (EnumGuiSide side : sides)
						side.getSetter().accept(rectangle, side.getAxis().getCoordinate(mouse));
					break;
				default:
					throw BecauseOf.unexpected();
			}
		}
	}
}
