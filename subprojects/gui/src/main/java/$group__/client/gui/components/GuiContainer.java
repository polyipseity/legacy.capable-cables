package $group__.client.gui.components;

import $group__.client.gui.structures.AffineTransformStack;
import $group__.client.gui.structures.EnumGuiMouseClickResult;
import $group__.client.gui.structures.EnumGuiState;
import $group__.client.gui.structures.GuiCache.CacheKey;
import $group__.client.gui.structures.GuiDragInfo;
import $group__.client.gui.traits.handlers.IGuiLifecycleHandler;
import $group__.client.gui.utilities.GLUtilities.GLStacks;
import $group__.client.gui.utilities.GuiUtilities.ObjectUtilities;
import $group__.client.gui.utilities.GuiUtilities.UnboxingUtilities;
import $group__.utilities.MiscellaneousUtilities;
import $group__.utilities.specific.Maps;
import $group__.utilities.specific.ThrowableUtilities.BecauseOf;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import static $group__.utilities.Capacities.INITIAL_CAPACITY_2;
import static $group__.utilities.specific.Optionals.unboxOptional;
import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@OnlyIn(CLIENT)
public class GuiContainer<D extends GuiContainer.Data<?>> extends GuiComponent<D> {
	protected final List<GuiComponent<?>> children = new ArrayList<>(INITIAL_CAPACITY_2);

	public GuiContainer(Shape shape, D data) { super(shape, data); }

	protected void add(@SuppressWarnings("SameParameterValue") int index, GuiComponent<?> component) {
		if (component == this) throw BecauseOf.illegalArgument("component", component);
		component.getParent().ifPresent(p -> p.remove(component));
		getChildren().add(index, component);
		component.onAdded(this, index);
		if (EnumGuiState.READY.isReachedBy(data.getState())) {
			new IGuiLifecycleHandler.Initializer(component).initialize(this);
			CacheKey.RESHAPE_HANDLER.get(this).reshape(this);
		}
	}

	public void move(int index, GuiComponent<?> component) {
		if (getChildren().remove(component)) {
			getChildren().add(index, component);
			component.onMoved(index);
		}
	}

	public void moveToTop(GuiComponent<?> component) { move(getChildren().size() - 1, component); }

	public void add(GuiComponent<?>... components) {
		boolean reshape = false;
		for (GuiComponent<?> component : components) {
			if (component == this) throw BecauseOf.illegalArgument("components", components);
			component.getParent().ifPresent(p -> p.remove(component));
			getChildren().add(component);
			component.onAdded(this, getChildren().lastIndexOf(component));
			if (EnumGuiState.READY.isReachedBy(data.getState())) {
				new IGuiLifecycleHandler.Initializer(component).initialize(this);
				reshape = true;
			}
		}
		if (reshape) CacheKey.RESHAPE_HANDLER.get(this).reshape(this);
	}

	public void remove(GuiComponent<?>... components) {
		boolean reshape = false;
		for (GuiComponent<?> component : components) {
			getChildren().remove(component);
			component.onRemoved(this);
			if (EnumGuiState.READY.isReachedBy(data.getState())) reshape = true;
		}
		if (reshape) CacheKey.RESHAPE_HANDLER.get(this).reshape(this);
	}

	protected void transformChildren(AffineTransformStack stack) {
		Rectangle2D bound = getShape().getBounds2D();
		stack.delegated.peek().translate(bound.getX(), bound.getY());
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void render(AffineTransformStack stack, Point2D mouse, float partialTicks) {
		if (EnumGuiState.READY.isReachedBy(data.getState())) {
			GLStacks.push("GL_SCISSOR_TEST",
					() -> GL11.glEnable(GL11.GL_SCISSOR_TEST),
					() -> GL11.glDisable(GL11.GL_SCISSOR_TEST));
			int[] boundsBox = new int[4]; // TODO replace glScissor with stencil buffer perhaps
			GL11.glGetIntegerv(GL11.GL_SCISSOR_BOX, boundsBox);
			getChildren().forEach(c -> runChildTransformed(stack, c, () -> {
				UnboxingUtilities.acceptRectangle(
						CoordinateConverters.toNativeRectangle(this,
								ObjectUtilities.getRectangleExpanded(stack.delegated.peek().createTransformedShape(c.getShape().getBounds2D()).getBounds2D()))
								.createIntersection(new Rectangle2D.Double(boundsBox[0], boundsBox[1], boundsBox[2], boundsBox[3])),
						(x, y) -> (w, h) -> GLStacks.push("glScissor",
								() -> GL11.glScissor(x.intValue(), y.intValue(), w.intValue(), h.intValue()),
								GLStacks.GL_SCISSOR_FALLBACK));
				c.render(stack, mouse, partialTicks);
				GLStacks.pop("glScissor");
			}));
			GLStacks.pop("GL_SCISSOR_TEST");
		}
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void constrain(AffineTransformStack stack) {
		super.constrain(stack);
		getChildren().forEach(c -> runChildTransformed(stack, c, () -> c.constrain(stack)));
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onInitialize(IGuiLifecycleHandler handler, GuiComponent<?> invoker) {
		super.onInitialize(handler, invoker);
		getChildren().forEach(component -> component.onInitialize(handler, invoker));
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onTick(IGuiLifecycleHandler handler, GuiComponent<?> invoker) {
		super.onTick(handler, invoker);
		children.forEach(component -> component.onTick(handler, invoker));
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onClose(IGuiLifecycleHandler handler, GuiComponent<?> invoker) {
		children.forEach(component -> component.onClose(handler, invoker));
		super.onClose(handler, invoker);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onDestroyed(IGuiLifecycleHandler handler, GuiComponent<?> invoker) {
		children.forEach(component -> component.onDestroyed(handler, invoker));
		super.onDestroyed(handler, invoker);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onMouseHovering(AffineTransformStack stack, Point2D mouse) {
		@Nullable GuiComponent<?> hovering = unboxOptional(getChildMouseOver(stack, mouse));
		if (hovering != data.hovering) {
			data.getHovering().ifPresent(h -> runChildTransformed(stack, h, () -> h.onMouseHovered(stack, mouse)));
			data.hovering = hovering;
			data.getHovering().ifPresent(h -> runChildTransformed(stack, h, () -> h.onMouseHover(stack, mouse)));
		}
		data.getHovering().ifPresent(h -> runChildTransformed(stack, h, () -> h.onMouseHovering(stack, mouse)));
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onMouseHovered(AffineTransformStack stack, Point2D mouse) {
		data.getHovering().ifPresent(h -> runChildTransformed(stack, h, () -> {
			h.onMouseHovered(stack, mouse);
			data.hovering = null;
		}));
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public EnumGuiMouseClickResult onMouseClicked(AffineTransformStack stack, GuiDragInfo drag, Point2D mouse, int button) {
		return getChildMouseOver(stack, mouse).map(c -> getChildTransformed(stack, c, () -> {
			GuiDragInfo dragC = new GuiDragInfo(c, mouse, button);
			EnumGuiMouseClickResult ret = c.onMouseClicked(stack, dragC, mouse, button);
			if (ret.result) data.setFocused(c);
			if (ret == EnumGuiMouseClickResult.DRAG) data.drags.put(button, dragC);
			return ret;
		})).orElse(EnumGuiMouseClickResult.PASS);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean onMouseDragging(AffineTransformStack stack, GuiDragInfo drag, Rectangle2D mouse, int button) {
		return Optional.ofNullable(data.drags.get(button)).map(d -> getChildTransformed(stack, d.dragged, () -> d.dragged.onMouseDragging(stack, d, mouse, button))).orElse(false);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean onMouseDragged(AffineTransformStack stack, GuiDragInfo drag, Point2D mouse, int button) {
		return Optional.ofNullable(data.drags.get(button)).map(d -> getChildTransformed(stack, d.dragged, () -> {
			boolean ret = d.dragged.onMouseDragged(stack, d, mouse, button);
			data.drags.remove(button);
			return ret;
		})).orElse(false);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean onMouseReleased(AffineTransformStack stack, Point2D mouse, int button) {
		return getChildMouseOver(stack, mouse).filter(c -> getChildTransformed(stack, c, () -> c.onMouseReleased(stack, mouse, button))).isPresent();
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean onMouseScrolled(AffineTransformStack stack, Point2D mouse, double scrollDelta) {
		return getChildMouseOver(stack, mouse).filter(c -> getChildTransformed(stack, c, () -> c.onMouseScrolled(stack, mouse, scrollDelta))).isPresent();
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean onKeyPressed(int key, int scanCode, int modifiers) {
		super.onKeyPressed(key, scanCode, modifiers);
		return data.getFocused().filter(f -> f.onKeyPressed(key, scanCode, modifiers)).isPresent();
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean onKeyReleased(int key, int scanCode, int modifiers) {
		boolean ret = data.getFocused().filter(f -> f.onKeyReleased(key, scanCode, modifiers)).isPresent();
		super.onKeyReleased(key, scanCode, modifiers);
		return ret;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean onCharTyped(char codePoint, int modifiers) { return data.getFocused().filter(f -> f.onCharTyped(codePoint, modifiers)).isPresent(); }

	public ImmutableList<GuiComponent<?>> getChildrenView() { return ImmutableList.copyOf(getChildren()); }

	protected List<GuiComponent<?>> getChildren() { return children; }

	@Override
	public boolean onChangeFocus(boolean next) {
		Optional<GuiComponent<?>> foc = data.getFocused();
		if (foc.filter(f -> f.onChangeFocus(next)).isPresent())
			return true;
		else {
			int i = foc.filter(f -> getChildren().contains(f))
					.map(f -> getChildren().indexOf(f) + (next ? 1 : 0))
					.orElseGet(() -> next ? 0 : getChildren().size());

			ListIterator<GuiComponent<?>> iterator = getChildren().listIterator(i);
			BooleanSupplier canAdvance = next ? iterator::hasNext : iterator::hasPrevious;
			Supplier<GuiComponent<?>> advance = next ? iterator::next : iterator::previous;

			while (canAdvance.getAsBoolean()) {
				GuiComponent<?> advanced = advance.get();
				if (advanced.onChangeFocus(next)) {
					data.setFocused(advanced);
					return true;
				}
			}

			data.setFocused(null);
			return false;
		}
	}

	@Override
	protected boolean isBeingDragged() { return data.drags.isEmpty() && super.isBeingDragged(); }

	@Override
	protected boolean isBeingHovered() { return !data.getHovering().isPresent() && super.isBeingHovered(); }

	@Override
	protected boolean isBeingFocused() { return !data.getFocused().isPresent() && super.isBeingFocused(); }

	protected Optional<GuiComponent<?>> getChildMouseOver(AffineTransformStack stack, Point2D mouse) {
		for (GuiComponent<?> c : Lists.reverse(getChildren())) {
			if (getChildTransformed(stack, c, () -> c.isMouseOver(stack, mouse)))
				return Optional.of(c);
		}
		return Optional.empty();
	}

	protected <R> R getChildTransformed(AffineTransformStack stack, GuiComponent<?> child, Supplier<R> call) {
		stack.push();
		transformChildren(stack);
		stack.push();
		child.transformThis(stack);
		R ret = call.get();
		stack.delegated.pop();
		stack.delegated.pop();
		return ret;
	}

	protected void runChildTransformed(AffineTransformStack stack, GuiComponent<?> child, Runnable call) {
		getChildTransformed(stack, child, () -> {
			call.run();
			return null;
		});
	}

	public static class Data<E extends Events> extends GuiComponent.Data<E> {
		@Nullable
		protected GuiComponent<?> focused = null, hovering = null;
		protected Map<Integer, GuiDragInfo> drags = Maps.MAP_MAKER_SINGLE_THREAD.makeMap();

		public Data(E events, Supplier<Logger> logger) {
			super(events, logger);
		}

		public Optional<GuiComponent<?>> getFocused() { return Optional.ofNullable(focused); }

		protected void setFocused(@Nullable GuiComponent<?> component) {
			getFocused().ifPresent(f -> f.onFocusLost(component));
			@Nullable GuiComponent<?> from = MiscellaneousUtilities.KNullable(focused, focused = component);
			getFocused().ifPresent(f -> f.onFocusGet(from));
		}

		public Optional<GuiComponent<?>> getHovering() { return Optional.ofNullable(hovering); }

		public ImmutableMap<Integer, GuiDragInfo> getDragsView() { return ImmutableMap.copyOf(drags); }
	}
}
