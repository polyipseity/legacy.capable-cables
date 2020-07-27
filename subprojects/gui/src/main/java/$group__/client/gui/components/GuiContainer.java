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
public class GuiContainer extends GuiComponent {
	protected final List<GuiComponent> children = new ArrayList<>(INITIAL_CAPACITY_2);
	@Nullable
	protected GuiComponent focused = null, hovering = null;
	protected Map<Integer, GuiDragInfo> drags = Maps.MAP_MAKER_SINGLE_THREAD.makeMap();

	public GuiContainer(Shape shape, Logger logger) { super(shape, logger); }

	protected void add(@SuppressWarnings("SameParameterValue") int index, GuiComponent component) {
		if (component == this) throw BecauseOf.illegalArgument("component", component);
		if (component.parent != null) {
			@Nullable GuiContainer parentOld = component.parent.get();
			if (parentOld != null) parentOld.remove(component);
		}
		getChildren().add(index, component);
		component.onAdded(this, index);
		if (EnumGuiState.READY.isReachedBy(getState())) {
			new IGuiLifecycleHandler.Initializer(component).initialize(this);
			CacheKey.RESHAPE_HANDLER.get(this).reshape(this);
		}
	}

	public void move(int index, GuiComponent component) {
		if (getChildren().remove(component)) {
			getChildren().add(index, component);
			component.onMoved(index);
		}
	}

	public void moveToTop(GuiComponent component) { move(getChildren().size() - 1, component); }

	public void add(GuiComponent... components) {
		boolean reRect = false;
		for (GuiComponent component : components) {
			if (component == this) throw BecauseOf.illegalArgument("components", components);
			if (component.parent != null) {
				@Nullable GuiContainer parentOld = component.parent.get();
				if (parentOld != null) parentOld.remove(component);
			}
			getChildren().add(component);
			component.onAdded(this, getChildren().lastIndexOf(component));
			if (EnumGuiState.READY.isReachedBy(getState())) {
				new IGuiLifecycleHandler.Initializer(component).initialize(this);
				reRect = true;
			}
		}
		if (reRect) CacheKey.RESHAPE_HANDLER.get(this).reshape(this);
	}

	public void remove(GuiComponent... components) {
		boolean reRect = false;
		for (GuiComponent component : components) {
			getChildren().remove(component);
			component.onRemoved(this);
			if (EnumGuiState.READY.isReachedBy(getState())) reRect = true;
		}
		if (reRect) CacheKey.RESHAPE_HANDLER.get(this).reshape(this);
	}

	protected void transformChildren(AffineTransformStack stack) {
		Rectangle2D bound = getShape().getBounds2D();
		stack.delegated.peek().translate(bound.getX(), bound.getY());
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void render(AffineTransformStack stack, Point2D mouse, float partialTicks) {
		if (EnumGuiState.READY.isReachedBy(getState())) {
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
	public void onInitialize(IGuiLifecycleHandler handler, GuiComponent invoker) {
		super.onInitialize(handler, invoker);
		getChildren().forEach(component -> component.onInitialize(handler, invoker));
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onTick(IGuiLifecycleHandler handler, GuiComponent invoker) {
		super.onTick(handler, invoker);
		children.forEach(component -> component.onTick(handler, invoker));
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onClose(IGuiLifecycleHandler handler, GuiComponent invoker) {
		children.forEach(component -> component.onClose(handler, invoker));
		super.onClose(handler, invoker);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onDestroyed(IGuiLifecycleHandler handler, GuiComponent invoker) {
		children.forEach(component -> component.onDestroyed(handler, invoker));
		super.onDestroyed(handler, invoker);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onMouseHovering(AffineTransformStack stack, Point2D mouse) {
		@Nullable GuiComponent hovering = unboxOptional(getChildMouseOver(stack, mouse));
		if (hovering != this.hovering) {
			if (this.hovering != null)
				runChildTransformed(stack, this.hovering, () -> this.hovering.onMouseHovered(stack, mouse));
			this.hovering = hovering;
			if (hovering != null) runChildTransformed(stack, hovering, () -> hovering.onMouseHover(stack, mouse));
		}
		if (hovering != null) runChildTransformed(stack, hovering, () -> hovering.onMouseHovering(stack, mouse));
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onMouseHovered(AffineTransformStack stack, Point2D mouse) {
		if (hovering == null) return;
		runChildTransformed(stack, hovering, () -> {
			hovering.onMouseHovered(stack, mouse);
			hovering = null;
		});
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public EnumGuiMouseClickResult onMouseClicked(AffineTransformStack stack, GuiDragInfo drag, Point2D mouse, int button) {
		return getChildMouseOver(stack, mouse).map(c -> getChildTransformed(stack, c, () -> {
			GuiDragInfo dragC = new GuiDragInfo(c, mouse, button);
			EnumGuiMouseClickResult ret = c.onMouseClicked(stack, dragC, mouse, button);
			if (ret.result) setFocused(c);
			if (ret == EnumGuiMouseClickResult.DRAG) drags.put(button, dragC);
			return ret;
		})).orElse(EnumGuiMouseClickResult.PASS);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean onMouseDragging(AffineTransformStack stack, GuiDragInfo drag, Rectangle2D mouse, int button) {
		return Optional.ofNullable(this.drags.get(button)).map(d -> getChildTransformed(stack, d.dragged, () -> d.dragged.onMouseDragging(stack, d, mouse, button))).orElse(false);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean onMouseDragged(AffineTransformStack stack, GuiDragInfo drag, Point2D mouse, int button) {
		return Optional.ofNullable(this.drags.get(button)).map(d -> getChildTransformed(stack, d.dragged, () -> {
			boolean ret = d.dragged.onMouseDragged(stack, d, mouse, button);
			this.drags.remove(button);
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
		return focused != null && focused.onKeyPressed(key, scanCode, modifiers);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean onKeyReleased(int key, int scanCode, int modifiers) {
		boolean ret = focused != null && focused.onKeyReleased(key, scanCode, modifiers);
		super.onKeyReleased(key, scanCode, modifiers);
		return ret;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean onCharTyped(char codePoint, int modifiers) { return focused != null && focused.onCharTyped(codePoint, modifiers); }

	public ImmutableList<GuiComponent> getChildrenView() { return ImmutableList.copyOf(getChildren()); }

	protected List<GuiComponent> getChildren() { return children; }

	@Override
	public boolean onChangeFocus(boolean next) {
		boolean hasFocused = focused != null;
		if (hasFocused && focused.onChangeFocus(next)) return true;
		else {
			int j = getChildren().indexOf(focused);
			int i;
			if (hasFocused && j >= 0) i = j + (next ? 1 : 0);
			else if (next) i = 0;
			else i = getChildren().size();

			ListIterator<GuiComponent> iterator = getChildren().listIterator(i);
			BooleanSupplier canAdvance = next ? iterator::hasNext : iterator::hasPrevious;
			Supplier<GuiComponent> advance = next ? iterator::next : iterator::previous;

			while (canAdvance.getAsBoolean()) {
				GuiComponent advanced = advance.get();
				if (advanced.onChangeFocus(next)) {
					setFocused(advanced);
					return true;
				}
			}

			setFocused(null);
			return false;
		}
	}

	@Override
	protected boolean isBeingDragged() { return drags.isEmpty() && super.isBeingDragged(); }

	@Override
	protected boolean isBeingHovered() { return hovering == null && super.isBeingHovered(); }

	@Override
	protected boolean isBeingFocused() { return focused == null && super.isBeingFocused(); }

	protected void setFocused(@Nullable GuiComponent component) {
		if (focused != null)
			focused.onFocusLost(component);
		@Nullable GuiComponent from = MiscellaneousUtilities.K(focused, focused = component);
		if (focused != null)
			focused.onFocusGet(from);
	}

	protected Optional<GuiComponent> getChildMouseOver(AffineTransformStack stack, Point2D mouse) {
		for (GuiComponent c : Lists.reverse(getChildren())) {
			if (getChildTransformed(stack, c, () -> c.isMouseOver(stack, mouse)))
				return Optional.of(c);
		}
		return Optional.empty();
	}

	protected <R> R getChildTransformed(AffineTransformStack stack, GuiComponent child, Supplier<R> call) {
		stack.push();
		transformChildren(stack);
		stack.push();
		child.transformThis(stack);
		R ret = call.get();
		stack.delegated.pop();
		stack.delegated.pop();
		return ret;
	}

	protected void runChildTransformed(AffineTransformStack stack, GuiComponent child, Runnable call) {
		getChildTransformed(stack, child, () -> {
			call.run();
			return null;
		});
	}
}
