package $group__.client.gui.components;

import $group__.client.gui.components.roots.GuiRoot;
import $group__.client.gui.structures.AffineTransformStack;
import $group__.client.gui.structures.EnumGuiMouseClickResult;
import $group__.client.gui.structures.EnumGuiState;
import $group__.client.gui.structures.GuiCache.CacheKey;
import $group__.client.gui.structures.GuiDragInfo;
import $group__.client.gui.traits.handlers.IGuiLifecycleHandler;
import $group__.client.gui.utilities.GLUtilities;
import $group__.client.gui.utilities.GuiUtilities.UnboxingUtilities;
import $group__.utilities.specific.ThrowableUtilities.BecauseOf;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
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
	@Nullable
	protected GuiDragInfo drag = null;

	public GuiContainer(Shape shape) { super(shape); }

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

	protected void transformTransform(AffineTransformStack stack) {
		Rectangle2D bound = getShape().getBounds2D();
		stack.delegated.peek().translate(bound.getX(), bound.getY());
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void render(AffineTransformStack stack, Point2D mouse, float partialTicks) {
		if (EnumGuiState.READY.isReachedBy(getState())) {
			GuiRoot<?> root = CacheKey.ROOT.get(this);

			stack.push();
			transformTransform(stack);
			GLUtilities.push("GL_SCISSOR_TEST",
					() -> GL11.glEnable(GL11.GL_SCISSOR_TEST),
					() -> GL11.glDisable(GL11.GL_SCISSOR_TEST));
			getChildren().forEach(c -> {
				Rectangle2D bounds = stack.delegated.peek().createTransformedShape(c.getShape().getBounds2D()).getBounds2D();
				UnboxingUtilities.acceptRectangle(
						new Rectangle2D.Double(bounds.getX(), bounds.getMaxY(), bounds.getWidth(), bounds.getHeight()),
						(x, y) -> (w, h) ->
								GLUtilities.push("glScissor",
										() -> GL11.glScissor((int) coordinateConverters.toNativeCoordinate(x), (int) coordinateConverters.toNativeCoordinate(root.getRectangleView().getHeight() - y), (int) coordinateConverters.toNativeCoordinate(w), (int) coordinateConverters.toNativeCoordinate(h)),
										GLUtilities.GL_SCISSOR_FALLBACK));

				c.render(stack, mouse, partialTicks);
				GLUtilities.pop("glScissor");
			});
			GLUtilities.pop("GL_SCISSOR_TEST");
			stack.delegated.pop();
		}
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
		stack.push();
		transformTransform(stack);
		@Nullable GuiComponent hovering = unboxOptional(getChildMouseOver(stack, mouse));
		if (hovering != this.hovering) {
			if (this.hovering != null) this.hovering.onMouseHovered(stack, mouse);
			this.hovering = hovering;
			if (hovering != null) hovering.onMouseHover(stack, mouse);
		}
		if (hovering != null) hovering.onMouseHovering(stack, mouse);
		stack.delegated.pop();
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onMouseHovered(AffineTransformStack stack, Point2D mouse) {
		if (hovering != null) {
			stack.push();
			transformTransform(stack);
			hovering.onMouseHovered(stack, mouse);
			stack.delegated.pop();
			hovering = null;
		}
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public EnumGuiMouseClickResult onMouseClicked(AffineTransformStack stack, GuiDragInfo drag, Point2D mouse, int button) {
		if (this.drag != null) return EnumGuiMouseClickResult.PASS;
		return getChildMouseOver(stack, mouse).map(c -> {
			stack.push();
			transformTransform(stack);
			GuiDragInfo dragC = new GuiDragInfo(c, mouse, button);
			EnumGuiMouseClickResult ret = c.onMouseClicked(stack, dragC, mouse, button);
			stack.delegated.pop();
			if (ret.result) focused = c;
			if (ret == EnumGuiMouseClickResult.DRAG) this.drag = dragC;
			return ret;
		}).orElse(EnumGuiMouseClickResult.PASS);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean onMouseDragging(AffineTransformStack stack, GuiDragInfo drag, Rectangle2D mouse, int button) { return this.drag != null && this.drag.dragged.onMouseDragging(stack, this.drag, mouse, button); }

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean onMouseReleased(AffineTransformStack stack, Point2D mouse, int button) {
		if (drag != null) {
			stack.push();
			transformTransform(stack);
			boolean ret = drag.dragged.onMouseDragged(stack, drag, mouse, button);
			stack.delegated.pop();
			drag = null;
			if (ret) return true;
		}
		return getChildMouseOver(stack, mouse).filter(c -> {
			stack.push();
			transformTransform(stack);
			boolean ret = c.onMouseReleased(stack, mouse, button);
			stack.delegated.pop();
			return ret;
		}).isPresent();
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean onMouseScrolled(AffineTransformStack stack, Point2D mouse, double scrollDelta) {
		return getChildMouseOver(stack, mouse).filter(c -> {
			stack.push();
			transformTransform(stack);
			boolean ret = c.onMouseScrolled(stack, mouse, scrollDelta);
			stack.delegated.pop();
			return ret;
		}).isPresent();
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
					focused = advanced;
					return true;
				}
			}

			focused = null;
			return false;
		}
	}

	protected boolean isBeingDragged() { return getParent().map(p -> p.drag).filter(d -> d.dragged == this).filter(d -> drag == null).isPresent(); }

	protected Optional<GuiDragInfo> getDragForThis() { return getParent().map(p -> p.drag).filter(d -> d.dragged == this); }

	protected Optional<GuiComponent> getChildMouseOver(AffineTransformStack stack, Point2D mouse) {
		stack.push();
		transformTransform(stack);
		for (GuiComponent c : Lists.reverse(getChildren())) {
			if (c.isMouseOver(stack, mouse)) {
				stack.delegated.pop();
				return Optional.of(c);
			}
		}
		stack.delegated.pop();
		return Optional.empty();
	}
}
