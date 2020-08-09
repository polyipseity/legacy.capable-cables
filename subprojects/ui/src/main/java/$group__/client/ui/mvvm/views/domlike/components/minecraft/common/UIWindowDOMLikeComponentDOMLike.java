package $group__.client.ui.mvvm.views.domlike.components.minecraft.common;

import $group__.client.ui.coredeprecated.events.EventUIShapeDescriptor;
import $group__.client.ui.coredeprecated.extensions.UIExtensionCache;
import $group__.client.ui.coredeprecated.extensions.UIExtensionCache.CacheKey;
import $group__.client.ui.coredeprecated.structures.*;
import $group__.client.ui.mvvm.views.domlike.UIContainerDOMLikeComponentDOMLike;
import $group__.client.ui.mvvm.views.domlike.UIDOMLikeDOMLikeComponentDOMLike;
import $group__.client.ui.mvvm.views.domlike.components.IUIComponentDOMLike;
import $group__.client.ui.mvvm.views.domlike.components.minecraft.common.UIWindowDOMLikeComponentDOMLike.GuiDragInfoWindow.EnumGuiDragType;
import $group__.client.ui.structures.ShapeDescriptor.Rectangular;
import $group__.client.ui.utilities.DrawingUtilities;
import $group__.client.ui.utilities.UIObjectUtilities;
import $group__.client.ui.utilities.ReferenceUtilities;
import $group__.utilities.CastUtilities;
import $group__.utilities.NamespaceUtilities;
import $group__.utilities.client.GLUtilities;
import $group__.utilities.client.GLUtilities.GLStacksUtilities;
import $group__.utilities.events.EnumEventHookStage;
import $group__.utilities.specific.ThrowableUtilities.BecauseOf;
import $group__.utilities.specific.ThrowableUtilities.Try;
import $group__.utilities.structures.Registry;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ConcurrentModificationException;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static $group__.client.ui.coredeprecated.structures.UIConstraint.CONSTRAINT_NONE_VALUE;
import static net.minecraftforge.api.distmarker.Dist.CLIENT;

/*
button
 */
@OnlyIn(Dist.CLIENT)
public class UIWindowDOMLikeComponentDOMLike
		<M extends IGuiModel<?>, V extends IUIComponentDOMLike.IGuiView<?, ?>, C extends IUIComponentDOMLike.IGuiController.IContainer<?, CH>,
				CH extends IUIComponentDOMLike>
		extends UIContainerDOMLikeComponentDOMLike {
	// TODO make window scroll bars, maybe create a new component, and embed into this
	// TODO make value not hardcoded through themes
	public static final int
			WINDOW_RESHAPE_THICKNESS = 10, // COMMENT external
			WINDOW_DRAG_BAR_THICKNESS = 10, // COMMENT internal top
			WINDOW_VISIBLE_MINIMUM = 10;

	@Override
	public boolean contains(final AffineTransformStack stack, Point2D point) {
		return isBeingDragged(this) || stack.getDelegated().peek().createTransformedShape(CacheGuiWindow.RECTANGLE_CLICKABLE.get(this)).contains(point);
	}

	@Override
	public void render(final AffineTransformStack stack, Point2D mouse, float partialTicks) {
		AffineTransform transform = stack.getDelegated().peek();
		DrawingUtilities.drawShape(transform, getShapeDescriptor().getShape(), true, data.colors.background, 0);
		DrawingUtilities.drawShape(transform, getShapeDescriptor().getShape(), true, data.colors.border, 0);
		super.render(stack, mouse, partialTicks);
		if (isBeingDragged()) {
			data.getDragWindow().ifPresent(d -> {
				Rectangle2D r = (Rectangle2D) getShapeDescriptor().getShape().clone();
				d.handle(transform, r, mouse);
				UIObjectUtilities.acceptRectangular(r, (x, y) -> (w, h) -> r.setRect(x, y, w - 1, h - 1));
				if (Minecraft.getInstance().getFramebuffer().isStencilEnabled()) {
					GLStacksUtilities.push("GL_STENCIL_TEST",
							() -> GL11.glDisable(GL11.GL_STENCIL_TEST), () -> GL11.glDisable(GL11.GL_STENCIL_TEST));
					DrawingUtilities.drawRectangle(transform, r, data.colors.dragging.getRGB(), 0);
					GLStacksUtilities.pop("GL_STENCIL_TEST");
				} else {
					GLStacksUtilities.push("GL_SCISSOR_TEST",
							() -> GL11.glDisable(GL11.GL_SCISSOR_TEST), () -> GL11.glDisable(GL11.GL_SCISSOR_TEST));
					DrawingUtilities.drawRectangle(transform, r, data.colors.dragging.getRGB(), 0);
					GLStacksUtilities.pop("GL_SCISSOR_TEST");
				}
			});
		}
	}

	@Override
	public void transformChildren(final AffineTransformStack stack) {
		super.transformChildren(stack);
		stack.getDelegated().peek().translate(0, WINDOW_DRAG_BAR_THICKNESS);
	}

	@Override
	public void onMouseHovering(final AffineTransformStack stack, Point2D mouse) {
		super.onMouseHovering(stack, mouse);
		if (isBeingMouseHovered() && !isBeingDragged()) {
			if (!Try.call(() -> ReferenceUtilities.toRelativePoint(stack.getDelegated().peek(), mouse), data.logger.get())
					.filter(c -> !getShapeDescriptor().getShape().contains(c)).filter(c -> {
						EnumSet<EnumUISide> sides = EnumUISide.getSidesMouseOver(getShapeDescriptor().getShape(), c);
						if (sides.contains(EnumUISide.UP) && sides.contains(EnumUISide.LEFT)
								|| sides.contains(EnumUISide.DOWN) && sides.contains(EnumUISide.RIGHT))
							data.cursor = EnumCursor.EXTENSION_RESIZE_NW_SE_CURSOR;
						else if (sides.contains(EnumUISide.UP) && sides.contains(EnumUISide.RIGHT)
								|| sides.contains(EnumUISide.DOWN) && sides.contains(EnumUISide.LEFT))
							data.cursor = EnumCursor.EXTENSION_RESIZE_NE_SW_CURSOR;
						else if (sides.contains(EnumUISide.LEFT) || sides.contains(EnumUISide.RIGHT))
							data.cursor = EnumCursor.STANDARD_RESIZE_HORIZONTAL_CURSOR;
						else if (sides.contains(EnumUISide.UP) || sides.contains(EnumUISide.DOWN))
							data.cursor = EnumCursor.STANDARD_RESIZE_VERTICAL_CURSOR;
						else
							throw new InternalError();
						GLFW.glfwSetCursor(GLUtilities.getWindowHandle(), data.cursor.handle);
						return true;
					}).isPresent()) {
				data.cursor = null;
				GLFW.glfwSetCursor(GLUtilities.getWindowHandle(), MemoryUtil.NULL);
			}
		}
	}

	@Override
	public EnumUIMouseClickResult onMouseClicked(final AffineTransformStack stack, UIMouseButtonPressData drag, Point2D mouse, int button) {
		EnumUIMouseClickResult ret = super.onMouseClicked(stack, drag, mouse, button);
		if (ret.result || button != GLFW.GLFW_MOUSE_BUTTON_LEFT) return ret;
		if (stack.getDelegated().peek().createTransformedShape(CacheGuiWindow.RECTANGLE_DRAGGABLE.get(this)).contains(mouse)) {
			data.dragWindow = new GuiDragInfoWindow(drag, EnumGuiDragType.REPOSITION, null, null);
			return EnumUIMouseClickResult.DRAG;
		} else if (Try.call(() -> ReferenceUtilities.toRelativePoint(stack.getDelegated().peek(), mouse), data.logger.get())
				.filter(c -> !getShapeDescriptor().getShape().contains(c)).map(c -> {
					EnumSet<EnumUISide> sides = EnumUISide.getSidesMouseOver(getShapeDescriptor().getShape(), c);
					@Nullable Point2D base = null;
					if (sides.contains(EnumUISide.UP) && sides.contains(EnumUISide.LEFT))
						base = new Point2D.Double(getShapeDescriptor().getShape().getMaxX(), getShapeDescriptor().getShape().getMaxY());
					else if (sides.contains(EnumUISide.DOWN) && sides.contains(EnumUISide.RIGHT))
						base = new Point2D.Double(getShapeDescriptor().getShape().getX(), getShapeDescriptor().getShape().getY());
					else if (sides.contains(EnumUISide.UP) && sides.contains(EnumUISide.RIGHT))
						base = new Point2D.Double(getShapeDescriptor().getShape().getX(), getShapeDescriptor().getShape().getMaxY());
					else if (sides.contains(EnumUISide.DOWN) && sides.contains(EnumUISide.LEFT))
						base = new Point2D.Double(getShapeDescriptor().getShape().getMaxX(), getShapeDescriptor().getShape().getY());
					data.dragWindow = new GuiDragInfoWindow(drag, EnumGuiDragType.RESIZE, sides, base);
					return true;
				}).orElse(false))
			return EnumUIMouseClickResult.DRAG;
		return EnumUIMouseClickResult.CLICK;
	}

	@Override
	public void reshape(UIDOMLikeDOMLikeComponentDOMLike<?, ?> invoker) { reshape(invoker, s -> {}); }

	@Override
	public void reshape(UIDOMLikeDOMLikeComponentDOMLike<?, ?> invoker, Consumer<? super S> transformer) {
		transformShape(this, invoker, transformer);
		// TODO resizing logic
	}

	@Override
	public boolean onMouseDragged(final AffineTransformStack stack, UIMouseButtonPressData drag, Point2D mouse, int button) {
		if (isBeingDragged()) {
			data.getDragWindow().ifPresent(d -> {
				Rectangle2D r = getShapeDescriptor().getShape();
				d.handle(stack.getDelegated().peek(), r, mouse);
				reshape(this, s -> s.getShape().setRect(r));
				data.dragWindow = null;
			});
			return true;
		}
		return super.onMouseDragged(stack, drag, mouse, button);
	}

	@Override
	public void renderTick(final AffineTransformStack stack, Point2D mouse, float partialTicks) {
		IUIConstraint constraint = CacheGuiWindow.CONSTRAINT.get(this);
		data.constraints.add(constraint);
		super.renderTick(stack, mouse, partialTicks);
		data.constraints.remove(constraint);
	}

	@Override
	public void onFocusGet(@Nullable UIDOMLikeDOMLikeComponentDOMLike<?, ?> from) { getParent().orElseThrow(InternalError::new).moveToTop(this); }

	@Override
	public void onMouseHovered(final AffineTransformStack stack, Point2D mouse) {
		super.onMouseHovered(stack, mouse);
		data.cursor = null;
		GLFW.glfwSetCursor(GLUtilities.getWindowHandle(), MemoryUtil.NULL);
	}

	@Override
	public boolean onMouseDragging(final AffineTransformStack stack, UIMouseButtonPressData drag, Rectangle2D mouse, int button) {
		if (super.onMouseDragging(stack, drag, mouse, button))
			return true;
		if (isBeingDragged()) {
			data.getCursor().ifPresent(c -> {
				data.getDragWindow().map(d -> d.base).ifPresent(b -> {
					EnumSet<EnumUISide> sides = EnumUISide.getSidesMouseOver(new Rectangle2D.Double(b.getX(), b.getY(), 0, 0), new Point2D.Double(mouse.getMaxX(), mouse.getMaxY()));
					if (sides.contains(EnumUISide.UP) && sides.contains(EnumUISide.LEFT)
							|| sides.contains(EnumUISide.DOWN) && sides.contains(EnumUISide.RIGHT))
						data.cursor = EnumCursor.EXTENSION_RESIZE_NW_SE_CURSOR;
					else if (sides.contains(EnumUISide.UP) && sides.contains(EnumUISide.RIGHT)
							|| sides.contains(EnumUISide.DOWN) && sides.contains(EnumUISide.LEFT))
						data.cursor = EnumCursor.EXTENSION_RESIZE_NE_SW_CURSOR;
				});
				GLFW.glfwSetCursor(GLUtilities.getWindowHandle(), c.handle);
			});
			return true;
		}
		return false;
	}

	@OnlyIn(Dist.CLIENT)
	public enum CacheGuiWindow {
		;
		public static final Registry.RegistryObject<UIExtensionCache.IType<Rectangle2D, UIWindowDOMLikeComponentDOMLike<?, ?, ?, ?>>> RECTANGLE_DRAGGABLE =
				UIExtensionCache.Reg.INSTANCE.register(new ResourceLocation(
						NamespaceUtilities.getNamespacePrefixedString(".", "window", "rectangle_draggable")),
						(Function<? super ResourceLocation, ? extends UIExtensionCache.IType<Rectangle2D, UIWindowDOMLikeComponentDOMLike<?, ?, ?, ?>>>)
								k -> new UIExtensionCache.IType.Impl<Rectangle2D, UIWindowDOMLikeComponentDOMLike<?, ?, ?, ?>>(k) {
									{ Bus.FORGE.bus().get().register(this); }

									@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
									protected void onShapeDescriptorReshape(EventUIShapeDescriptor.Modify event) {
										if (event.getStage() == EnumEventHookStage.POST) {
											event.getShapeDescriptor().getView()
													.flatMap(IUIComponentDOMLike.IGuiView::getComponent)
													.filter(UIWindowDOMLikeComponentDOMLike.class::isInstance)
													.ifPresent(c -> invalidate((UIWindowDOMLikeComponentDOMLike<?, ?, ?, ?>) c));
										}
									}

									@Override
									public Optional<Rectangle2D> get(UIWindowDOMLikeComponentDOMLike<?, ?, ?, ?> component) {
										return UIExtensionCache.TYPE.getValue().get(component).flatMap(cache -> Try.call(() -> cache.getDelegated().get(getKey(),
												() -> {
													Rectangle2D bounds = component.getView().getShapeDescriptor().getShapeProcessed().getBounds2D();
													return new Rectangle2D.Double(
															bounds.getX(),
															bounds.getY(),
															bounds.getWidth(),
															WINDOW_DRAG_BAR_THICKNESS);
												}), getLogger()).flatMap(CastUtilities::castUnchecked));
									}
								});
		public static final Registry.RegistryObject<UIExtensionCache.IType<Rectangle2D, UIWindowDOMLikeComponentDOMLike<?, ?, ?, ?>>> RECTANGLE_CLICKABLE =
				UIExtensionCache.Reg.INSTANCE.register(new ResourceLocation(
								NamespaceUtilities.getNamespacePrefixedString(".", "window", "rectangle_clickable")),
						(Function<? super ResourceLocation, ? extends UIExtensionCache.IType<Rectangle2D, UIWindowDOMLikeComponentDOMLike<?, ?, ?, ?>>>)
								k -> new UIExtensionCache.IType.Impl<Rectangle2D, UIWindowDOMLikeComponentDOMLike<?, ?, ?, ?>>(k) {
									{ Bus.FORGE.bus().get().register(this); }

									@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
									protected void onShapeDescriptorReshape(EventUIShapeDescriptor.Modify event) {
										if (event.getStage() == EnumEventHookStage.POST) {
											event.getShapeDescriptor().getView()
													.flatMap(IUIComponentDOMLike.IGuiView::getComponent)
													.filter(UIWindowDOMLikeComponentDOMLike.class::isInstance)
													.ifPresent(c -> invalidate((UIWindowDOMLikeComponentDOMLike<?, ?, ?, ?>) c));
										}
									}

									@Override
									public Optional<Rectangle2D> get(UIWindowDOMLikeComponentDOMLike<?, ?, ?, ?> component) {
										return UIExtensionCache.TYPE.getValue().get(component).flatMap(cache -> Try.call(() -> cache.getDelegated().get(getKey(),
												() -> UIObjectUtilities.applyRectangular(component.getView().getShapeDescriptor().getShapeProcessed().getBounds2D(), (x, y) -> (w, h) ->
														new Rectangle2D.Double(x - WINDOW_RESHAPE_THICKNESS, y - WINDOW_RESHAPE_THICKNESS, w + (WINDOW_RESHAPE_THICKNESS << 1), h + (WINDOW_RESHAPE_THICKNESS << 1)))), getLogger()).flatMap(CastUtilities::castUnchecked));
									}
								});
		public static final Registry.RegistryObject<UIExtensionCache.IType<IUIConstraint, UIWindowDOMLikeComponentDOMLike<?, ?, ?, ?>>> CONSTRAINT =
				UIExtensionCache.Reg.INSTANCE.register(new ResourceLocation(
								NamespaceUtilities.getNamespacePrefixedString(".", "window", "constraint")),
						(Function<? super ResourceLocation, ? extends UIExtensionCache.IType<IUIConstraint, UIWindowDOMLikeComponentDOMLike<?, ?, ?, ?>>>)
								k -> new UIExtensionCache.IType.Impl<IUIConstraint, UIWindowDOMLikeComponentDOMLike<?, ?, ?, ?>>(k) {
									{ Bus.FORGE.bus().get().register(this); }

									@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
									protected void onShapeDescriptorReshape(EventUIShapeDescriptor.Modify event) {
										if (event.getStage() == EnumEventHookStage.POST) {
											event.getShapeDescriptor().getView()
													.flatMap(IUIComponentDOMLike.IGuiView::getComponent)
													.filter(UIWindowDOMLikeComponentDOMLike.class::isInstance)
													.ifPresent(c -> invalidate((UIWindowDOMLikeComponentDOMLike<?, ?, ?, ?>) c));
										}
									}

									@Override
									public Optional<IUIConstraint> get(UIWindowDOMLikeComponentDOMLike<?, ?, ?, ?> component) {
										return UIExtensionCache.TYPE.getValue().get(component).flatMap(cache -> Try.call(() -> cache.getDelegated().get(getKey(),
												() -> {
													return new UIConstraint(new Rectangle2D.Double(0, 0, WINDOW_VISIBLE_MINIMUM, WINDOW_VISIBLE_MINIMUM),
															new Rectangle2D.Double(rRoot.getMaxX() - WINDOW_VISIBLE_MINIMUM, rRoot.getMaxY() - WINDOW_VISIBLE_MINIMUM, IUIConstraint.CONSTRAINT_NULL_VALUE, IUIConstraint.CONSTRAINT_NULL_VALUE));
												}), getLogger()).flatMap(CastUtilities::castUnchecked));
										return Try.call(() -> component.data.cache.delegated.get(key, () -> {
											Rectangle2D bounds = component.getView().getShapeDescriptor().getShapeProcessed().getBounds2D();
											return new Rectangle2D.Double(
													bounds.getX(),
													bounds.getY(),
													bounds.getWidth(),
													WINDOW_DRAG_BAR_THICKNESS);
										}), getLogger()).map(CastUtilities::<Rectangle2D>castUnchecked).orElseThrow(InternalError::new);
									}
								});

		private static ResourceLocation getKey(String path) { return new ResourceLocation(UIWindowDOMLikeComponentDOMLike.class.getSimpleName().toLowerCase(Locale.ROOT), path); }
	}

	@OnlyIn(Dist.CLIENT)
	public static class View<C extends UIWindowDOMLikeComponentDOMLike<?, ?, ?, ?>, SD extends IShapeDescriptor<? extends Rectangle2D, ?>> extends UIContainerDOMLikeComponentDOMLike.View {
		public View(C component) { super(component); }

		@OnlyIn(Dist.CLIENT)
		public class ShapeDescriptor<S extends Rectangle2D, A extends IUIAnchorSet<?>> extends Rectangular<S, A> {
			public ShapeDescriptor(S shape, A anchorSet) { super(View.this, shape, anchorSet); }

			@Override
			protected <T extends IShapeDescriptor<?, ?>> boolean modify0(T self, Function<? super T, Boolean> action) throws ConcurrentModificationException {
				Optional<IUIConstraint> constraint = getComponent().flatMap(c -> CacheGuiWindow.CONSTRAINT.getValue().get(c));
				constraint.ifPresent(c -> getConstraints().add(c));
				boolean ret = super.modify0(self, action);
				constraint.ifPresent(c -> getConstraints().remove(c));
				return ret;
			}
		}
	}

	@OnlyIn(CLIENT)
	public static final class GuiDragInfoWindow {
		public final UIMouseButtonPressData decorated;
		public final EnumGuiDragType type;
		@Nullable
		public final EnumSet<EnumUISide> sides;
		@Nullable
		public final Point2D base;

		public GuiDragInfoWindow(UIMouseButtonPressData decorated, EnumGuiDragType type, @Nullable EnumSet<EnumUISide> sides, @Nullable Point2D base) {
			this.decorated = decorated;
			this.type = type;
			this.sides = sides;
			this.base = base;

			if (type == EnumGuiDragType.RESIZE) {
				if (sides == null)
					throw BecauseOf.illegalArgument("No sides specified", "type", type, "sides", null);
				if (sides.contains(EnumUISide.HORIZONTAL) || sides.contains(EnumUISide.VERTICAL))
					throw BecauseOf.illegalArgument("Invalid sides", "sides", sides);
				sides.forEach(s -> {
					if (sides.contains(s.getOpposite()))
						throw BecauseOf.illegalArgument("Illegal sides combination", "sides", sides);
				});
			} else {
				if (sides != null || base != null)
					throw BecauseOf.illegalArgument("Too much data", "type", type, "sides", sides, "base", base);
			}
		}

		public void handle(AffineTransform transform, Rectangle2D rectangle, Point2D mouse) {
			switch (type) {
				case REPOSITION:
					rectangle.setRect(rectangle.getX() + (mouse.getX() - decorated.cursor.getX()), rectangle.getY() + (mouse.getY() - decorated.cursor.getY()),
							rectangle.getWidth(), rectangle.getHeight());
					break;
				case RESIZE:
					assert sides != null;
					for (EnumUISide side : sides) {
						EnumUIAxis axis = side.getAxis();
						side.getSetter().accept(rectangle, side.getGetter().apply(rectangle) + (axis.getCoordinate(mouse) - axis.getCoordinate(decorated.cursor)));
					}
					break;
				default:
					throw new InternalError();
			}
		}

		@OnlyIn(Dist.CLIENT)
		public enum EnumGuiDragType {
			REPOSITION,
			RESIZE,
		}
	}


	@OnlyIn(Dist.CLIENT)
	public static class Data<E extends Events, C extends Data.ColorData> extends UIContainerDOMLikeComponentDOMLike.Data<E> {
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

		@OnlyIn(Dist.CLIENT)
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


}
