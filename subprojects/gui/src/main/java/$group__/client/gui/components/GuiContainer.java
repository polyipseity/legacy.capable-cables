package $group__.client.gui.components;

import $group__.client.gui.components.roots.GuiRoot;
import $group__.client.gui.traits.IGuiLifecycleHandler;
import $group__.utilities.helpers.specific.ThrowableUtilities.BecauseOf;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
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
	protected boolean dragging = false;

	public GuiContainer(Rectangle2D rectangle) { super(rectangle); }

	protected static Point2D toRelativePointForComponent(GuiComponent component, Point2D point) { return new Point2D.Double(point.getX() - component.getRectangleView().getX(), point.getY() - component.getRectangleView().getY()); }

	public void add(GuiComponent... components) {
		boolean reRect = false;
		for (GuiComponent component : components) {
			if (component == this) throw BecauseOf.illegalArgument("components", components);
			if (component.parent != null) {
				@Nullable GuiContainer parentOld = component.parent.get();
				if (parentOld != null) parentOld.remove(component);
			}
			getChildren().add(component);
			component.onAdded(this);
			if (EnumState.READY.isReachedBy(getState())) {
				new IGuiLifecycleHandler.Initializer(component).initialize(this);
				reRect = true;
			}
		}
		if (reRect) getReRectangleHandler().orElseThrow(BecauseOf::unexpected).reRectangle(this);
	}

	public void remove(GuiComponent... components) {
		boolean reRect = false;
		for (GuiComponent component : components) {
			getChildren().remove(component);
			component.onRemoved(this);
			if (EnumState.READY.isReachedBy(getState())) reRect = true;
		}
		if (reRect) getReRectangleHandler().orElseThrow(BecauseOf::unexpected).reRectangle(this);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void render(MatrixStack matrix, Point2D mouse, float partialTicks) {
		GuiRoot<?> root = getRoot().orElseThrow(BecauseOf::unexpected);
		if (EnumState.READY.isReachedBy(getState())) {
			RenderSystem.pushMatrix();
			getChildren().forEach(c -> {
				Rectangle2D rectC = c.getRectangle();
				matrix.push();
				matrix.translate(rectC.getX(), rectC.getY(), 0);
				GL11.glEnable(GL11.GL_SCISSOR_TEST);
				Point2D xyAbs = toAbsolutePoint(new Point2D.Double(rectC.getX(), rectC.getY()));
				GL11.glScissor((int) xyAbs.getX(), (int) (root.getScreen().height - xyAbs.getY()), (int) rectC.getWidth(), (int) rectC.getHeight());
				c.render(matrix, toRelativePointForComponent(c, mouse), partialTicks);
				GL11.glScissor(0, 0, root.getScreen().width, root.getScreen().height);
				GL11.glDisable(GL11.GL_SCISSOR_TEST);
				matrix.pop();
			});
			RenderSystem.popMatrix();
		}
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public void onAdded(GuiContainer parent) {
		super.onAdded(parent);
		getChildren().forEach(c -> c.onAdded(parent));
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
	public void onMouseMoved(Point2D mouse) { getChildren().forEach(c -> c.onMouseMoved(toRelativePointForComponent(c, mouse))); }

	@Override
	public boolean onMouseClicked(Point2D mouse, int button) {
		for (GuiComponent child : getChildren()) {
			if (child.onMouseClicked(toRelativePointForComponent(child, mouse), button)) {
				focused = child;
				if (button == GLFW_MOUSE_BUTTON_LEFT) dragging = true;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onMouseReleased(Point2D mouse, int button) {
		dragging = false;
		return getChildMouseOver(mouse).filter(c -> c.onMouseReleased(toRelativePointForComponent(c, mouse), button)).isPresent();
	}

	@Override
	public boolean onMouseScrolled(Point2D mouse, double scrollDelta) { return getChildMouseOver(mouse).filter(c -> c.onMouseScrolled(toRelativePointForComponent(c, mouse), scrollDelta)).isPresent(); }

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

			ListIterator<? extends GuiComponent> iterator = getChildren().listIterator(i);
			BooleanSupplier canAdvance = next ? iterator::hasNext : iterator::hasPrevious;
			Supplier<? extends GuiComponent> advance = next ? iterator::next : iterator::previous;

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

	protected Optional<? extends GuiComponent> getChildMouseOver(Point2D mouse) {
		for (GuiComponent child : getChildren())
			if (child.isMouseOver(toRelativePointForComponent(child, mouse))) return Optional.of(child);
		return Optional.empty();
	}
}
