package $group__.client.gui.components;

import $group__.utilities.helpers.specific.ThrowableUtilities.BecauseOf;
import com.google.common.collect.ImmutableList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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

import static $group__.utilities.helpers.Capacities.INITIAL_CAPACITY_2;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

@OnlyIn(Dist.CLIENT)
public class GuiContainer extends GuiComponent {
	protected List<GuiComponent> children = new ArrayList<>(INITIAL_CAPACITY_2);
	@Nullable
	protected GuiComponent focused = null;
	protected boolean dragging = false;

	public GuiContainer(GuiComponent... components) { add(components); }

	protected static Point2D toRelativePointForComponent(GuiComponent component, Point2D point) { return new Point2D.Double(point.getX() - component.getRectangleView().getX(), point.getY() - component.getRectangleView().getY()); }

	public Optional<Rectangle2D> getPreferredRectangle() {
		Rectangle2D rectangle = new Rectangle();
		children.forEach(c -> Rectangle2D.union(rectangle, c.getPreferredRectangle().orElseGet(c::getRectangleView), rectangle));
		return Optional.of(rectangle);
	}

	public List<GuiComponent> getChildren() { return ImmutableList.copyOf(children); }

	public void add(GuiComponent... components) {
		boolean resize = false;
		for (GuiComponent component : components) {
			if (component == this) throw BecauseOf.illegalArgument("components", components);
			if (component.parent != null) {
				@Nullable GuiContainer parentOld = component.parent.get();
				if (parentOld != null) parentOld.remove(component);
			}
			children.add(component);
			component.onAdded(this);
			if (EnumState.INITIALIZED.isReachedBy(state)) component.onInitialize();
			resize = true;
		}
		if (resize && EnumState.READY.isReachedBy(state)) onResize();
	}

	public void remove(GuiComponent... components) {
		for (GuiComponent component : components) {
			children.remove(component);
			component.onRemoved(this);
		}
	}

	public void remove(int... indexes) { for (int index : indexes) children.remove(index).onRemoved(this); }

	@Override
	@OverridingMethodsMustInvokeSuper
	public void render(Point2D mouse, float partialTicks) {
		if (EnumState.READY.isReachedBy(state))
			children.forEach(c -> c.render(toRelativePointForComponent(c, mouse), partialTicks));
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	protected void onAdded(GuiContainer parent) {
		super.onAdded(parent);
		children.forEach(c -> c.onAdded(parent));
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	protected void onResize() {
		super.onResize();
		children.forEach(GuiComponent::onResize);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	protected void onTick() { children.forEach(GuiComponent::onTick); }

	@Override
	@OverridingMethodsMustInvokeSuper
	protected void onInitialize() {
		super.onInitialize();
		children.forEach(GuiComponent::onInitialize);
	}

	@Override
	protected void onRemoved(GuiContainer parent) {
		super.onRemoved(parent);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	protected void onClose() {
		children.forEach(GuiComponent::onClose);
		super.onClose();
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	protected void onDestroy() {
		children.forEach(GuiComponent::onDestroy);
		super.onDestroy();
	}

	@Override
	public void onMouseMoved(Point2D mouse) { children.forEach(c -> c.onMouseMoved(toRelativePointForComponent(c, mouse))); }

	@Override
	public boolean onMouseClicked(Point2D mouse, int button) {
		for (GuiComponent child : children) {
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

	@Override
	public boolean onChangeFocus(boolean next) {
		boolean hasFocused = focused != null;
		if (hasFocused && focused.onChangeFocus(next)) return true;
		else {
			int j = children.indexOf(focused);
			int i;
			if (hasFocused && j >= 0) i = j + (next ? 1 : 0);
			else if (next) i = 0;
			else i = children.size();

			ListIterator<? extends GuiComponent> iterator = children.listIterator(i);
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
		for (GuiComponent child : children)
			if (child.isMouseOver(toRelativePointForComponent(child, mouse))) return Optional.of(child);
		return Optional.empty();
	}
}

