package $group__.client.gui.components.common;

import $group__.client.gui.components.GuiComponent;
import $group__.client.gui.components.GuiContainer;
import $group__.client.gui.components.common.GuiWindow.GuiDragInfoWindow.EnumGuiDragType;
import $group__.client.gui.structures.*;
import $group__.client.gui.structures.GuiCache.CacheKey;
import $group__.client.gui.traits.handlers.IGuiReshapeHandler;
import $group__.client.gui.traits.shapes.IGuiShapeRectangle;
import $group__.client.gui.utilities.GLUtilities;
import $group__.client.gui.utilities.GLUtilities.GLStacksUtilities;
import $group__.client.gui.utilities.GuiUtilities;
import $group__.client.gui.utilities.GuiUtilities.DrawingUtilities;
import $group__.utilities.CastUtilities;
import $group__.utilities.specific.ThrowableUtilities.BecauseOf;
import $group__.utilities.specific.ThrowableUtilities.Try;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

import static $group__.client.gui.structures.GuiConstraint.CONSTRAINT_NONE_VALUE;
import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public class GuiWindow<D extends GuiWindow.Data<?, ?>> extends GuiContainer<D> implements IGuiReshapeHandler, IGuiShapeRectangle {
	// TODO make window scroll bars, maybe create a new component, and embed into this
	// TODO make value not hardcoded through themes
	public static final int
			WINDOW_RESHAPE_THICKNESS = 10, // COMMENT external
			WINDOW_DRAG_BAR_THICKNESS = 10, // COMMENT internal top
			WINDOW_VISIBLE_MINIMUM = 10;

	public GuiWindow(Rectangle2D rectangle, D data) { super(rectangle, data); }

	@Override
	protected Shape adaptToBounds(IGuiReshapeHandler handler, GuiComponent<?> invoker, Rectangle2D rectangle) { return rectangle; }

	@Override
	public boolean contains(AffineTransformStack stack, Point2D point) { return data.isActive() && (isBeingDragged() || stack.delegated.peek().createTransformedShape(CacheKeys.RECTANGLE_CLICKABLE.get(this)).contains(point)); }

	@Override
	protected void transformChildren(AffineTransformStack stack) {
		super.transformChildren(stack);
		stack.delegated.peek().translate(0, WINDOW_DRAG_BAR_THICKNESS);
	}

	@Override
	public void render(AffineTransformStack stack, Point2D mouse, float partialTicks) {
		AffineTransform transform = stack.delegated.peek();
		DrawingUtilities.fill(transform, getRectangle(), data.colors.background.getRGB(), 0);
		DrawingUtilities.fill(transform, CacheKeys.RECTANGLE_DRAGGABLE.get(this), data.colors.border.getRGB(), 0);
		super.render(stack, mouse, partialTicks);
		if (isBeingDragged()) {
			data.getDragWindow().ifPresent(d -> {
				Rectangle2D c = getRectangleView(), r = getRectangleView();
				d.handle(transform, r, mouse);
				reshape(this, r);
				AffineTransformStack stackR = stack.copy();
				stackR.delegated.pop();
				transformThis(stackR);
				AffineTransform transformR = stackR.delegated.pop();
				reshape(this, c);
				GuiUtilities.ObjectUtilities.acceptRectangle(r, (x, y) -> (w, h) -> r.setRect(x, y, w - 1, h - 1));
				if (Minecraft.getInstance().getFramebuffer().isStencilEnabled()) {
					GLStacksUtilities.push("GL_STENCIL_TEST",
							() -> GL11.glDisable(GL11.GL_STENCIL_TEST), () -> GL11.glDisable(GL11.GL_STENCIL_TEST));
					DrawingUtilities.drawRectangle(transformR, r, data.colors.dragging.getRGB(), 0);
					GLStacksUtilities.pop("GL_STENCIL_TEST");
				} else {
					GLStacksUtilities.push("GL_SCISSOR_TEST",
							() -> GL11.glDisable(GL11.GL_SCISSOR_TEST), () -> GL11.glDisable(GL11.GL_SCISSOR_TEST));
					DrawingUtilities.drawRectangle(transformR, r, data.colors.dragging.getRGB(), 0);
					GLStacksUtilities.pop("GL_SCISSOR_TEST");
				}
			});
		}
	}

	@Override
	protected Optional<GuiComponent<?>> getChildMouseOver(AffineTransformStack stack, Point2D mouse) { return isBeingDragged() ? Optional.empty() : super.getChildMouseOver(stack, mouse).filter(c -> stack.delegated.peek().createTransformedShape(getShape()).contains(mouse)); }

	@Override
	public Rectangle2D getRectangleView() { return (Rectangle2D) getRectangle().clone(); }

	protected Rectangle2D getRectangle() { return (Rectangle2D) super.getShape(); }

	@Override
	public void onFocusGet(@Nullable GuiComponent<?> from) { getParent().orElseThrow(BecauseOf::unexpected).moveToTop(this); }

	@Override
	public void reshape(GuiComponent<?> invoker, Shape shape) { setBounds(this, invoker, shape.getBounds2D()); }


	@Override
	public void reshape(GuiComponent<?> invoker) { reshape(invoker, getRectangle()); }

	@Override
	public void renderTick(AffineTransformStack stack, Point2D mouse, float partialTicks) {
		GuiConstraint constraint = CacheKeys.CONSTRAINT.get(this);
		data.constraints.add(constraint);
		super.renderTick(stack, mouse, partialTicks);
		data.constraints.remove(constraint);
	}

	@Override
	public void onMouseHovering(AffineTransformStack stack, Point2D mouse) {
		super.onMouseHovering(stack, mouse);
		if (isBeingHovered() && !isBeingDragged()) {
			if (!Try.call(() -> ReferenceConverters.toRelativePoint(stack.delegated.peek(), mouse), data.logger.get())
					.filter(c -> !getShape().contains(c)).filter(c -> {
						EnumSet<EnumGuiSide> sides = EnumGuiSide.getSidesMouseOver(getRectangle(), c);
						if (sides.contains(EnumGuiSide.UP) && sides.contains(EnumGuiSide.LEFT)
								|| sides.contains(EnumGuiSide.DOWN) && sides.contains(EnumGuiSide.RIGHT))
							data.cursor = EnumCursor.EXTENSION_RESIZE_NW_SE_CURSOR;
						else if (sides.contains(EnumGuiSide.UP) && sides.contains(EnumGuiSide.RIGHT)
								|| sides.contains(EnumGuiSide.DOWN) && sides.contains(EnumGuiSide.LEFT))
							data.cursor = EnumCursor.EXTENSION_RESIZE_NE_SW_CURSOR;
						else if (sides.contains(EnumGuiSide.LEFT) || sides.contains(EnumGuiSide.RIGHT))
							data.cursor = EnumCursor.STANDARD_RESIZE_HORIZONTAL_CURSOR;
						else if (sides.contains(EnumGuiSide.UP) || sides.contains(EnumGuiSide.DOWN))
							data.cursor = EnumCursor.STANDARD_RESIZE_VERTICAL_CURSOR;
						else throw BecauseOf.unexpected();
						GLFW.glfwSetCursor(GLUtilities.getWindowHandle(), data.cursor.handle);
						return true;
					}).isPresent()) {
				data.cursor = null;
				GLFW.glfwSetCursor(GLUtilities.getWindowHandle(), MemoryUtil.NULL);
			}
		}
	}

	@Override
	public void onMouseHovered(AffineTransformStack stack, Point2D mouse) {
		super.onMouseHovered(stack, mouse);
		data.cursor = null;
		GLFW.glfwSetCursor(GLUtilities.getWindowHandle(), MemoryUtil.NULL);
	}

	@Override
	public EnumGuiMouseClickResult onMouseClicked(AffineTransformStack stack, GuiDragInfo drag, Point2D mouse, int button) {
		EnumGuiMouseClickResult ret = super.onMouseClicked(stack, drag, mouse, button);
		if (ret.result || button != GLFW.GLFW_MOUSE_BUTTON_LEFT) return ret;
		if (stack.delegated.peek().createTransformedShape(CacheKeys.RECTANGLE_DRAGGABLE.get(this)).contains(mouse)) {
			data.dragWindow = new GuiDragInfoWindow(drag, EnumGuiDragType.REPOSITION, null, null);
			return EnumGuiMouseClickResult.DRAG;
		} else if (Try.call(() -> ReferenceConverters.toRelativePoint(stack.delegated.peek(), mouse), data.logger.get())
				.filter(c -> !getShape().contains(c)).map(c -> {
					EnumSet<EnumGuiSide> sides = EnumGuiSide.getSidesMouseOver(getRectangle(), c);
					@Nullable Point2D base = null;
					if (sides.contains(EnumGuiSide.UP) && sides.contains(EnumGuiSide.LEFT))
						base = new Point2D.Double(getRectangle().getMaxX(), getRectangle().getMaxY());
					else if (sides.contains(EnumGuiSide.DOWN) && sides.contains(EnumGuiSide.RIGHT))
						base = new Point2D.Double(getRectangle().getX(), getRectangle().getY());
					else if (sides.contains(EnumGuiSide.UP) && sides.contains(EnumGuiSide.RIGHT))
						base = new Point2D.Double(getRectangle().getX(), getRectangle().getMaxY());
					else if (sides.contains(EnumGuiSide.DOWN) && sides.contains(EnumGuiSide.LEFT))
						base = new Point2D.Double(getRectangle().getMaxX(), getRectangle().getY());
					data.dragWindow = new GuiDragInfoWindow(drag, EnumGuiDragType.RESIZE, sides, base);
					return true;
				}).orElse(false))
			return EnumGuiMouseClickResult.DRAG;
		return EnumGuiMouseClickResult.CLICK;
	}

	@Override
	public boolean onMouseDragging(AffineTransformStack stack, GuiDragInfo drag, Rectangle2D mouse, int button) {
		if (super.onMouseDragging(stack, drag, mouse, button))
			return true;
		if (isBeingDragged()) {
			data.getCursor().ifPresent(c -> {
				data.getDragWindow().map(d -> d.base).ifPresent(b -> {
					EnumSet<EnumGuiSide> sides = EnumGuiSide.getSidesMouseOver(new Rectangle2D.Double(b.getX(), b.getY(), 0, 0), new Point2D.Double(mouse.getMaxX(), mouse.getMaxY()));
					if (sides.contains(EnumGuiSide.UP) && sides.contains(EnumGuiSide.LEFT)
							|| sides.contains(EnumGuiSide.DOWN) && sides.contains(EnumGuiSide.RIGHT))
						data.cursor = EnumCursor.EXTENSION_RESIZE_NW_SE_CURSOR;
					else if (sides.contains(EnumGuiSide.UP) && sides.contains(EnumGuiSide.RIGHT)
							|| sides.contains(EnumGuiSide.DOWN) && sides.contains(EnumGuiSide.LEFT))
						data.cursor = EnumCursor.EXTENSION_RESIZE_NE_SW_CURSOR;
				});
				GLFW.glfwSetCursor(GLUtilities.getWindowHandle(), c.handle);
			});
			return true;
		}
		return false;
	}

	@Override
	public boolean onMouseDragged(AffineTransformStack stack, GuiDragInfo drag, Point2D mouse, int button) {
		if (isBeingDragged()) {
			data.getDragWindow().ifPresent(d -> {
				Rectangle2D r = getRectangleView();
				d.handle(stack.delegated.peek(), r, mouse);
				reshape(this, r);
				data.dragWindow = null;
			});
			return true;
		}
		return super.onMouseDragged(stack, drag, mouse, button);
	}


	@OnlyIn(CLIENT)
	public enum CacheKeys {
		;

		public static final CacheKey<GuiWindow<?>, Rectangle2D> RECTANGLE_DRAGGABLE = new CacheKey<GuiWindow<?>, Rectangle2D>(getKey("rectangle_draggable")) {
			@Override
			public void initialize(GuiWindow<?> component) {
				super.initialize(component);
				component.data.events.cReshape.add(par -> invalidate(component));
			}

			@Override
			public Rectangle2D get0(GuiWindow<?> component) {
				return Try.call(() -> component.data.cache.delegated.get(key, () ->
								new Rectangle2D.Double(component.getRectangle().getX(), component.getRectangle().getY(),
										component.getRectangle().getWidth(), WINDOW_DRAG_BAR_THICKNESS)),
						component.data.logger.get()).map(CastUtilities::<Rectangle2D>castUnchecked).orElseThrow(BecauseOf::unexpected);
			}
		};
		public static final CacheKey<GuiWindow<?>, Rectangle2D> RECTANGLE_CLICKABLE = new CacheKey<GuiWindow<?>, Rectangle2D>(getKey("rectangle_clickable")) {
			@Override
			public void initialize(GuiWindow<?> component) {
				super.initialize(component);
				component.data.events.cReshape.add(par -> invalidate(component));
			}

			@Override
			public Rectangle2D get0(GuiWindow<?> component) {
				return Try.call(() -> component.data.cache.delegated.get(key, () ->
								GuiUtilities.ObjectUtilities.applyRectangle(component.getRectangle(), (x, y) -> (w, h) ->
										new Rectangle2D.Double(x - WINDOW_RESHAPE_THICKNESS, y - WINDOW_RESHAPE_THICKNESS, w + WINDOW_RESHAPE_THICKNESS * 2, h + WINDOW_RESHAPE_THICKNESS * 2))),
						component.data.logger.get()).map(CastUtilities::<Rectangle2D>castUnchecked).orElseThrow(BecauseOf::unexpected);
			}
		};
		public static final CacheKey<GuiWindow<?>, GuiConstraint> CONSTRAINT = new CacheKey<GuiWindow<?>, GuiConstraint>(getKey("constraint")) {
			@Override
			public void initialize(GuiWindow<?> component) {
				super.initialize(component);
				ROOT.get(component).data.events.cReshape.add(par -> invalidate(component));
			}

			@Override
			public GuiConstraint get0(GuiWindow<?> component) {
				return Try.call(() -> component.data.cache.delegated.get(key, () -> {
					Rectangle2D rRoot = CacheKey.ROOT.get(component).getRectangleView();
					return new GuiConstraint(new Rectangle2D.Double(0, 0, WINDOW_VISIBLE_MINIMUM, WINDOW_VISIBLE_MINIMUM),
							new Rectangle2D.Double(rRoot.getMaxX() - WINDOW_VISIBLE_MINIMUM, rRoot.getMaxY() - WINDOW_VISIBLE_MINIMUM, CONSTRAINT_NONE_VALUE, CONSTRAINT_NONE_VALUE));
				}), component.data.logger.get()).map(CastUtilities::<GuiConstraint>castUnchecked).orElseThrow(BecauseOf::unexpected);
			}
		};

		private static ResourceLocation getKey(String path) { return new ResourceLocation(GuiWindow.class.getSimpleName().toLowerCase(Locale.ROOT), path); }
	}

	@OnlyIn(CLIENT)
	public static class Data<E extends Events, C extends Data.ColorData> extends GuiContainer.Data<E> {
		public C colors;
		@Nullable
		protected GuiDragInfoWindow dragWindow = null;
		@Nullable
		protected EnumCursor cursor = null;

		public Data(E events, Supplier<Logger> logger, C colors) {
			super(events, logger);
			this.colors = colors;
		}

		public Optional<GuiDragInfoWindow> getDragWindow() { return Optional.ofNullable(dragWindow); }

		public Optional<EnumCursor> getCursor() { return Optional.ofNullable(cursor); }

		@OnlyIn(CLIENT)
		@SuppressWarnings("UnusedReturnValue")
		public static class ColorData {
			public Color
					background = Color.BLACK,
					border = Color.WHITE,
					dragging = Color.DARK_GRAY;

			public ColorData setBackground(Color background) {
				this.background = background;
				return this;
			}

			public ColorData setBorder(Color border) {
				this.border = border;
				return this;
			}

			public ColorData setDragging(Color dragging) {
				this.dragging = dragging;
				return this;
			}
		}
	}

	@net.minecraftforge.api.distmarker.OnlyIn(CLIENT)
	public static final class GuiDragInfoWindow {
		public final GuiDragInfo decorated;
		public final EnumGuiDragType type;
		@Nullable
		public final EnumSet<EnumGuiSide> sides;
		@Nullable
		public final Point2D base;

		public GuiDragInfoWindow(GuiDragInfo decorated, EnumGuiDragType type, @Nullable EnumSet<EnumGuiSide> sides, @Nullable Point2D base) {
			this.decorated = decorated;
			this.type = type;
			this.sides = sides;
			this.base = base;

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
				if (sides != null || base != null)
					throw BecauseOf.illegalArgument("type", type, "sides", sides, "base", base);
			}
		}

		public void handle(AffineTransform transform, Rectangle2D rectangle, Point2D mouse) {
			switch (type) {
				case REPOSITION:
					rectangle.setRect(rectangle.getX() + (mouse.getX() - decorated.start.getX()), rectangle.getY() + (mouse.getY() - decorated.start.getY()),
							rectangle.getWidth(), rectangle.getHeight());
					break;
				case RESIZE:
					assert sides != null;
					for (EnumGuiSide side : sides) {
						EnumGuiAxis axis = side.getAxis();
						side.getSetter().accept(rectangle, side.getGetter().apply(rectangle) + (axis.getCoordinate(mouse) - axis.getCoordinate(decorated.start)));
					}
					break;
				default:
					throw BecauseOf.unexpected();
			}
		}

		@OnlyIn(CLIENT)
		public enum EnumGuiDragType {
			REPOSITION,
			RESIZE,
		}
	}
}
