package $group__.client.gui.components;

import $group__.client.gui.structures.*;
import $group__.client.gui.structures.GuiCache.CacheKey;
import $group__.client.gui.traits.handlers.IGuiLifecycleHandler;
import $group__.client.gui.utilities.GLUtilities;
import $group__.client.gui.utilities.GLUtilities.GLStacksUtilities;
import $group__.client.gui.utilities.GLUtilities.GLStateUtilities;
import $group__.client.gui.utilities.GuiUtilities.ObjectUtilities;
import $group__.utilities.MiscellaneousUtilities;
import $group__.utilities.specific.MapUtilities;
import $group__.utilities.specific.ThrowableUtilities.BecauseOf;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Supplier;

import static $group__.utilities.CapacityUtilities.INITIAL_CAPACITY_SMALL;
import static $group__.utilities.CapacityUtilities.INITIAL_CAPACITY_TINY;
import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public class GuiContainer<S extends ShapeDescriptor<?>, D extends GuiContainer.Data<?>> extends GuiComponent<S, D> {
	protected final List<GuiComponent<?, ?>> children = new ArrayList<>(INITIAL_CAPACITY_SMALL);

	public GuiContainer(S shape, D data) { super(shape, data); }

	protected static boolean onChangeFocusWithThisFocusable(GuiContainer<?, ?> self, Function<Boolean, Boolean> superMethod, boolean next) {
		return next ? !self.data.getFocused().isPresent() && !self.isBeingFocused() || superMethod.apply(true)
				: superMethod.apply(false) || !self.isBeingFocused();
	}

	protected void add(@SuppressWarnings("SameParameterValue") int index, GuiComponent<?, ?> component) {
		if (component == this) throw BecauseOf.illegalArgument("component", component);
		component.getParent().ifPresent(p -> p.remove(component));
		getChildren().add(index, component);
		component.onAdded(this, index);
		if (EnumGuiState.READY.isReachedBy(data.getState())) {
			IGuiLifecycleHandler.Initializer initializer = new IGuiLifecycleHandler.Initializer();
			initializer.component = component;
			initializer.initialize(this);
			CacheKey.RESHAPE_HANDLER.get(this).reshape(this);
		}
	}

	public void remove(GuiComponent<?, ?>... components) {
		boolean reshape = false;
		for (GuiComponent<?, ?> component : components) {
			getChildren().remove(component);
			component.onRemoved(this);
			if (EnumGuiState.READY.isReachedBy(data.getState())) reshape = true;
		}
		if (reshape) CacheKey.RESHAPE_HANDLER.get(this).reshape(this);
	}

	protected List<GuiComponent<?, ?>> getChildren() { return children; }

	public void moveToTop(GuiComponent<?, ?> component) { move(getChildren().size() - 1, component); }

	public void move(int index, GuiComponent<?, ?> component) {
		if (getChildren().remove(component)) {
			getChildren().add(index, component);
			component.onMoved(index);
		}
	}

	public void add(GuiComponent<?, ?>... components) {
		boolean reshape = false;
		IGuiLifecycleHandler.Initializer initializer = new IGuiLifecycleHandler.Initializer();
		for (GuiComponent<?, ?> component : components) {
			if (component == this) throw BecauseOf.illegalArgument("components", components);
			component.getParent().ifPresent(p -> p.remove(component));
			getChildren().add(component);
			component.onAdded(this, getChildren().lastIndexOf(component));
			if (EnumGuiState.READY.isReachedBy(data.getState())) {
				initializer.component = component;
				initializer.initialize(this);
				reshape = true;
			}
		}
		if (reshape) CacheKey.RESHAPE_HANDLER.get(this).reshape(this);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void render(AffineTransformStack stack, Point2D mouse, float partialTicks) {
		runChildTransformed(stack, () -> {
			if (Minecraft.getInstance().getFramebuffer().isStencilEnabled()) {
				int stencilRef = Math.floorMod(CacheKey.Z.get(this), (int) Math.pow(2, GLStateUtilities.getInteger(GL11.GL_STENCIL_BITS)));

				GLStacksUtilities.push("GL_STENCIL_TEST",
						() -> GL11.glEnable(GL11.GL_STENCIL_TEST), () -> GL11.glDisable(GL11.GL_STENCIL_TEST));
				GLStacksUtilities.push("stencilMask",
						() -> RenderSystem.stencilMask(GLUtilities.GL_MASK_ALL_BITS), GLStacksUtilities.STENCIL_MASK_FALLBACK);

				getChildren().forEach(c -> {
					if (c.data.visible) {
						GLStacksUtilities.push("stencilFunc",
								() -> RenderSystem.stencilFunc(GL11.GL_EQUAL, stencilRef, GLUtilities.GL_MASK_ALL_BITS), GLStacksUtilities.STENCIL_FUNC_FALLBACK);
						GLStacksUtilities.push("stencilOp",
								() -> RenderSystem.stencilOp(GL11.GL_KEEP, GL14.GL_INCR_WRAP, GL14.GL_INCR_WRAP), GLStacksUtilities.STENCIL_OP_FALLBACK);
						GLStacksUtilities.push("colorMask",
								() -> RenderSystem.colorMask(false, false, false, false), GLStacksUtilities.COLOR_MASK_FALLBACK);
						c.writeStencil(stack, mouse, partialTicks, true);
						GLStacksUtilities.pop("colorMask");
						GLStacksUtilities.pop("stencilOp");
						GLStacksUtilities.pop("stencilFunc");

						GLStacksUtilities.push("stencilFunc",
								() -> RenderSystem.stencilFunc(GL11.GL_LESS, stencilRef, GLUtilities.GL_MASK_ALL_BITS), GLStacksUtilities.STENCIL_FUNC_FALLBACK);

						GLStacksUtilities.push("stencilOp",
								() -> RenderSystem.stencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP), GLStacksUtilities.STENCIL_OP_FALLBACK);
						c.render(stack, mouse, partialTicks);
						GLStacksUtilities.pop("stencilOp");

						GLStacksUtilities.push("stencilOp",
								() -> RenderSystem.stencilOp(GL11.GL_KEEP, GL11.GL_REPLACE, GL11.GL_REPLACE), GLStacksUtilities.STENCIL_OP_FALLBACK);
						GLStacksUtilities.push("colorMask",
								() -> RenderSystem.colorMask(false, false, false, false), GLStacksUtilities.COLOR_MASK_FALLBACK);
						c.writeStencil(stack, mouse, partialTicks, false);
						GLStacksUtilities.pop("colorMask");
						GLStacksUtilities.pop("stencilOp");

						GLStacksUtilities.pop("stencilFunc");
					}
				});

				GLStacksUtilities.pop("stencilMask");
				GLStacksUtilities.pop("GL_STENCIL_TEST");
			} else {
				GLStacksUtilities.push("GL_SCISSOR_TEST",
						() -> GL11.glEnable(GL11.GL_SCISSOR_TEST),
						() -> GL11.glDisable(GL11.GL_SCISSOR_TEST));
				int[] boundsBox = new int[4];
				GLStateUtilities.getIntegerValue(GL11.GL_SCISSOR_BOX, boundsBox);
				getChildren().forEach(c -> {
					if (c.data.visible) {
						ObjectUtilities.acceptRectangle(
								CoordinateConverters.toNativeRectangle(this,
										ObjectUtilities.getRectangleExpanded(stack.delegated.peek().createTransformedShape(c.getShapeDescriptor().getShape().getBounds2D()).getBounds2D()))
										.createIntersection(new Rectangle2D.Double(boundsBox[0], boundsBox[1], boundsBox[2], boundsBox[3])),
								(x, y) -> (w, h) -> GLStacksUtilities.push("glScissor",
										() -> GLStateUtilities.setIntegerValue(GL11.GL_SCISSOR_BOX, new int[]{x.intValue(), y.intValue(), w.intValue(), h.intValue()},
												(i, v) -> GL11.glScissor(v[0], v[1], v[2], v[3])), GLStacksUtilities.GL_SCISSOR_FALLBACK));
						c.render(stack, mouse, partialTicks);
						GLStacksUtilities.pop("glScissor");
					}
				});
				GLStacksUtilities.pop("GL_SCISSOR_TEST");
			}
		});
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onInitialize(IGuiLifecycleHandler handler, GuiComponent<?, ?> invoker) {
		super.onInitialize(handler, invoker);
		getChildren().forEach(component -> component.onInitialize(handler, invoker));
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onTick(IGuiLifecycleHandler handler, GuiComponent<?, ?> invoker) {
		super.onTick(handler, invoker);
		children.forEach(component -> component.onTick(handler, invoker));
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onClose(IGuiLifecycleHandler handler, GuiComponent<?, ?> invoker) {
		children.forEach(component -> component.onClose(handler, invoker));
		super.onClose(handler, invoker);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onDestroyed(IGuiLifecycleHandler handler, GuiComponent<?, ?> invoker) {
		children.forEach(component -> component.onDestroyed(handler, invoker));
		super.onDestroyed(handler, invoker);
	}

	@Override
	protected boolean isBeingDragged() { return data.drags.isEmpty() && super.isBeingDragged(); }

	@Override
	protected boolean isBeingHovered() { return !data.getHovering().isPresent() && super.isBeingHovered(); }

	@Override
	protected boolean isBeingFocused() { return !data.getFocused().isPresent() && super.isBeingFocused(); }

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean onKeyPressed(int key, int scanCode, int modifiers) {
		super.onKeyPressed(key, scanCode, modifiers);
		return data.getFocused().filter(f -> f.data.isActive() && f.onKeyPressed(key, scanCode, modifiers)).isPresent();
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean onKeyReleased(int key, int scanCode, int modifiers) {
		boolean ret = data.getFocused().filter(f -> f.data.isActive() && f.onKeyReleased(key, scanCode, modifiers)).isPresent();
		super.onKeyReleased(key, scanCode, modifiers);
		return ret;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void renderTick(AffineTransformStack stack, Point2D mouse, float partialTicks) {
		super.renderTick(stack, mouse, partialTicks);
		runChildTransformed(stack, () -> getChildren().forEach(c -> {
			if (c.data.visible)
				c.renderTick(stack, mouse, partialTicks);
		}));
	}

	protected void runChildTransformed(AffineTransformStack stack, Runnable call) {
		getChildTransformed(stack, () -> {
			call.run();
			return null;
		});
	}

	protected <R> R getChildTransformed(AffineTransformStack stack, Supplier<R> call) {
		stack.push();
		transformChildren(stack);
		R ret = call.get();
		stack.delegated.pop();
		return ret;
	}

	protected void transformChildren(AffineTransformStack stack) {
		Rectangle2D bounds = getShapeDescriptor().getShape().getBounds2D();
		stack.delegated.peek().translate(bounds.getX(), bounds.getY());
	}

	@Override
	public void onMouseMoved(AffineTransformStack stack, Point2D mouse) { getChildMouseOver(stack, mouse).ifPresent(c -> runChildTransformed(stack, () -> c.onMouseMoved(stack, mouse))); }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onMouseHovering(AffineTransformStack stack, Point2D mouse) {
		@Nullable GuiComponent<?, ?> hovering = getChildMouseOver(stack, mouse).orElse(null);
		runChildTransformed(stack, () -> {
			if (hovering != data.hovering) {
				data.getHovering().ifPresent(h -> h.onMouseHovered(stack, mouse));
				data.hovering = hovering;
				data.getHovering().ifPresent(h -> h.onMouseHover(stack, mouse));
			}
			data.getHovering().ifPresent(h -> h.onMouseHovering(stack, mouse));
		});
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onMouseHovered(AffineTransformStack stack, Point2D mouse) {
		data.getHovering().ifPresent(h -> runChildTransformed(stack, () -> {
			h.onMouseHovered(stack, mouse);
			data.hovering = null;
		}));
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public EnumGuiMouseClickResult onMouseClicked(AffineTransformStack stack, GuiDragInfo drag, Point2D mouse, int button) {
		return getChildMouseOver(stack, mouse).map(c -> getChildTransformed(stack, () -> {
			GuiDragInfo dragC = new GuiDragInfo(c, mouse, button);
			EnumGuiMouseClickResult ret = c.onMouseClicked(stack, dragC, mouse, button);
			if (ret.result) data.setFocused(c);
			if (ret == EnumGuiMouseClickResult.DRAG) data.drags.put(button, dragC);
			return ret;
		})).orElse(EnumGuiMouseClickResult.PASS);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean onMouseReleased(AffineTransformStack stack, Point2D mouse, int button) {
		return getChildMouseOver(stack, mouse).filter(c -> getChildTransformed(stack, () -> c.onMouseReleased(stack, mouse, button))).isPresent();
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean onMouseDragging(AffineTransformStack stack, GuiDragInfo drag, Rectangle2D mouse, int button) {
		return Optional.ofNullable(data.drags.get(button)).filter(d -> d.dragged.data.isActive() && getChildTransformed(stack, () -> d.dragged.onMouseDragging(stack, d, mouse, button))).isPresent();
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean onMouseDragged(AffineTransformStack stack, GuiDragInfo drag, Point2D mouse, int button) {
		return Optional.ofNullable(data.drags.get(button)).map(d -> d.dragged.data.isActive() && getChildTransformed(stack, () -> {
			boolean ret = d.dragged.onMouseDragged(stack, d, mouse, button);
			data.drags.remove(button);
			return ret;
		})).orElse(false);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean onCharTyped(char codePoint, int modifiers) { return data.getFocused().filter(f -> f.data.isActive() && f.onCharTyped(codePoint, modifiers)).isPresent(); }

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean onMouseScrolled(AffineTransformStack stack, Point2D mouse, double scrollDelta) {
		return getChildMouseOver(stack, mouse).filter(c -> getChildTransformed(stack, () -> c.onMouseScrolled(stack, mouse, scrollDelta))).isPresent();
	}

	@Override
	public boolean onChangeFocus(boolean next) {
		Optional<GuiComponent<?, ?>> foc = data.getFocused();
		if (foc.filter(f -> f.data.isActive() && f.onChangeFocus(next)).isPresent())
			return true;
		else {
			int i = foc.filter(f -> getChildren().contains(f))
					.map(f -> getChildren().indexOf(f) + (next ? 1 : 0))
					.orElseGet(() -> next ? 0 : getChildren().size());

			ListIterator<GuiComponent<?, ?>> iterator = getChildren().listIterator(i);
			BooleanSupplier canAdvance = next ? iterator::hasNext : iterator::hasPrevious;
			Supplier<GuiComponent<?, ?>> advance = next ? iterator::next : iterator::previous;

			while (canAdvance.getAsBoolean()) {
				GuiComponent<?, ?> advanced = advance.get();
				if (advanced.data.isActive() && advanced.onChangeFocus(next)) {
					data.setFocused(advanced);
					return true;
				}
			}

			data.setFocused(null);
			return false;
		}
	}

	public ImmutableList<GuiComponent<?, ?>> getChildrenView() { return ImmutableList.copyOf(getChildren()); }

	protected Optional<GuiComponent<?, ?>> getChildMouseOver(AffineTransformStack stack, Point2D mouse) {
		return getChildTransformed(stack, () -> {
			for (GuiComponent<?, ?> c : Lists.reverse(getChildren())) {
				if (c.contains(stack, mouse))
					return Optional.of(c);
			}
			return Optional.empty();
		});
	}

	public static class Data<E extends Events> extends GuiComponent.Data<E> {
		@Nullable
		protected GuiComponent<?, ?> focused = null, hovering = null;
		protected Map<Integer, GuiDragInfo> drags = MapUtilities.getMapMakerSingleThread().initialCapacity(INITIAL_CAPACITY_TINY).makeMap();

		public Data(E events, Supplier<Logger> logger) {
			super(events, logger);
		}

		public Optional<GuiComponent<?, ?>> getHovering() { return Optional.ofNullable(hovering); }

		public ImmutableMap<Integer, GuiDragInfo> getDragsView() { return ImmutableMap.copyOf(drags); }

		public Optional<GuiComponent<?, ?>> getFocused() { return Optional.ofNullable(focused); }

		protected void setFocused(@Nullable GuiComponent<?, ?> component) {
			getFocused().ifPresent(f -> f.onFocusLost(component));
			@Nullable GuiComponent<?, ?> from = MiscellaneousUtilities.kNullable(focused, focused = component);
			getFocused().ifPresent(f -> f.onFocusGet(from));
		}
	}
}
