package $group__.client.gui.components;

import $group__.client.gui.components.roots.GuiRoot;
import $group__.client.gui.structures.GuiDragInfo;
import $group__.client.gui.traits.IGuiLifecycleHandler;
import $group__.client.gui.traits.IGuiReRectangleHandler;
import $group__.utilities.helpers.specific.ThrowableUtilities.BecauseOf;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import static $group__.utilities.helpers.Capacities.INITIAL_CAPACITY_2;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

@OnlyIn(Dist.CLIENT)
public class GuiContainer extends GuiComponent {
	protected List<GuiComponent> children = new ArrayList<>(INITIAL_CAPACITY_2);
	@Nullable
	protected GuiComponent focused = null;
	@Nullable
	protected GuiDragInfo drag = null;

	public GuiContainer(Rectangle2D rectangle) { super(rectangle); }

	protected void add(int index, GuiComponent component) {
		if (component == this) throw BecauseOf.illegalArgument("component", component);
		if (component.parent != null) {
			@Nullable GuiContainer parentOld = component.parent.get();
			if (parentOld != null) parentOld.remove(component);
		}
		getChildren().add(index, component);
		component.onAdded(this, index);
		if (EnumState.READY.isReachedBy(getState())) {
			new IGuiLifecycleHandler.Initializer(component).initialize(this);
			getNearestParentThatIs(IGuiReRectangleHandler.class).orElseThrow(BecauseOf::unexpected).reRectangle(this);
		}
	}

	public void move(int index, GuiComponent component) {
		if (getChildren().remove(component)) {
			getChildren().add(index, component);
			component.onMoved(index);
		}
	}

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
			if (EnumState.READY.isReachedBy(getState())) {
				new IGuiLifecycleHandler.Initializer(component).initialize(this);
				reRect = true;
			}
		}
		if (reRect)
			getNearestParentThatIs(IGuiReRectangleHandler.class).orElseThrow(BecauseOf::unexpected).reRectangle(this);
	}

	public void remove(GuiComponent... components) {
		boolean reRect = false;
		for (GuiComponent component : components) {
			getChildren().remove(component);
			component.onRemoved(this);
			if (EnumState.READY.isReachedBy(getState())) reRect = true;
		}
		if (reRect)
			getNearestParentThatIs(IGuiReRectangleHandler.class).orElseThrow(BecauseOf::unexpected).reRectangle(this);
	}

	protected MatrixStack transformMatrixForComponent(MatrixStack matrix, GuiComponent component) {
		Rectangle2D rectC = component.getRectangle();
		matrix.translate(rectC.getX(), rectC.getY(), 0);
		return matrix;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void render(MatrixStack matrix, Point2D mouse, float partialTicks) {
		if (EnumState.READY.isReachedBy(getState())) {
			GuiRoot<?> root = getNearestParentThatIs(GuiRoot.class).orElseThrow(BecauseOf::unexpected);
			getChildren().forEach(c -> {
				Rectangle2D rectC = c.getRectangle();
				matrix.push();
				Point2D xyAbs = toAbsolutePointWithMatrix(transformMatrixForComponent(matrix, c).getLast().getMatrix(), new Point2D.Double(0, rectC.getHeight()));
				GL11.glEnable(GL11.GL_SCISSOR_TEST);
				GL11.glScissor((int) toNativeCoordinate(xyAbs.getX()), (int) toNativeCoordinate(root.getRectangle().getHeight() - xyAbs.getY()), (int) toNativeCoordinate(rectC.getWidth()), (int) toNativeCoordinate(rectC.getHeight()));
				c.render(matrix, mouse, partialTicks);
				GL11.glDisable(GL11.GL_SCISSOR_TEST);
				matrix.pop();
			});
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
	public void onMouseMoved(MatrixStack matrix, Point2D mouse) {
		matrix.push();
		getChildren().forEach(c -> c.onMouseMoved(transformMatrixForComponent(matrix, c), mouse));
		matrix.pop();
	}

	@Override
	public boolean onMouseClicked(MatrixStack matrix, Point2D mouse, int button) {
		return getChildMouseOver(matrix, mouse).filter(c -> {
			matrix.push();
			boolean ret = c.onMouseClicked(transformMatrixForComponent(matrix, c), mouse, button);
			matrix.pop();
			if (ret) {
				focused = c;
				if (button == GLFW_MOUSE_BUTTON_LEFT)
					drag = new GuiDragInfo(c, mouse);
			}
			return ret;
		}).isPresent();
	}

	@Override
	public boolean onMouseDragging(MatrixStack matrix, Rectangle2D mouse, int button) { return drag != null && drag.dragged.onMouseDragging(matrix, mouse, button); }

	@Override
	public boolean onMouseReleased(MatrixStack matrix, Point2D mouse, int button) {
		if (drag != null) {
			matrix.push();
			boolean ret = drag.dragged.onMouseDragged(transformMatrixForComponent(matrix, drag.dragged), drag, mouse, button);
			matrix.pop();
			drag = null;
			if (ret) return true;
		}
		return getChildMouseOver(matrix, mouse).filter(c -> {
			matrix.push();
			boolean ret = c.onMouseReleased(transformMatrixForComponent(matrix, c), mouse, button);
			matrix.pop();
			return ret;
		}).isPresent();
	}

	@Override
	public boolean onMouseScrolled(MatrixStack matrix, Point2D mouse, double scrollDelta) {
		return getChildMouseOver(matrix, mouse).filter(c -> {
			matrix.push();
			boolean ret = c.onMouseScrolled(transformMatrixForComponent(matrix, c), mouse, scrollDelta);
			matrix.pop();
			return ret;
		}).isPresent();
	}

	@Override
	public boolean onKeyPressed(int key, int scanCode, int modifiers) { return focused != null && focused.onKeyPressed(key, scanCode, modifiers); }

	@Override
	public boolean onKeyReleased(int key, int scanCode, int modifiers) { return focused != null && focused.onKeyReleased(key, scanCode, modifiers); }

	@Override
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

	protected boolean isBeingDragged() { return getParent().map(p -> p.drag).filter(d -> d.dragged == this).isPresent(); }

	protected Optional<GuiDragInfo> getDragForThis() { return getParent().map(p -> p.drag).filter(d -> d.dragged == this); }

	protected Optional<GuiComponent> getChildMouseOver(MatrixStack matrix, Point2D mouse) {
		for (GuiComponent child : Lists.reverse(getChildren())) {
			matrix.push();
			boolean over = child.isMouseOver(transformMatrixForComponent(matrix, child), mouse);
			matrix.pop();
			if (over) return Optional.of(child);
		}
		return Optional.empty();
	}
}
