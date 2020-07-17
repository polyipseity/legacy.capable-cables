package $group__.client.gui.components;

import net.minecraft.client.gui.IGuiEventListener;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@SuppressWarnings("DeprecatedIsStillUsed")
@OnlyIn(CLIENT)
public interface IGuiEventListenerImproved extends IGuiEventListener {
	default void onMouseMoved(Point2D mouse) {}

	default boolean onMouseClicked(Point2D mouse, int button) { return false; }

	default boolean onMouseReleased(Point2D mouse, int button) { return false; }

	default boolean onMouseDragged(Rectangle2D mouse, int button) { return false; }

	default boolean onMouseScrolled(Point2D mouse, double scrollDelta) { return false; }

	default boolean isMouseOver(Point2D mouse) { return false; }

	default boolean onKeyPressed(int key, int scanCode, int modifiers) { return false; }

	default boolean onKeyReleased(int key, int scanCode, int modifiers) { return false; }

	default boolean onCharTyped(char codePoint, int modifiers) { return false; }

	default boolean onChangeFocus(boolean next) { return false; }

	////////// Deprecated //////////

	@Deprecated
	default void mouseMoved(double mouseX, double mouseY) { onMouseMoved(new Point2D.Double(mouseX, mouseY)); }

	@Deprecated
	default boolean mouseClicked(double mouseX, double mouseY, int button) { return onMouseClicked(new Point2D.Double(mouseX, mouseY), button); }

	@Deprecated
	default boolean mouseReleased(double mouseX, double mouseY, int button) { return onMouseReleased(new Point2D.Double(mouseX, mouseY), button); }

	@Deprecated
	default boolean mouseDragged(double mouseX, double mouseY, int button, double mouseXDiff, double mouseYDiff) { return onMouseDragged(new Rectangle2D.Double(mouseX, mouseY, mouseXDiff, mouseYDiff), button); }

	@Deprecated
	default boolean mouseScrolled(double mouseX, double mouseY, double scrollDelta) { return onMouseScrolled(new Point2D.Double(mouseX, mouseY), scrollDelta); }

	@Deprecated
	default boolean isMouseOver(double mouseX, double mouseY) { return isMouseOver(new Point2D.Double(mouseX, mouseY)); }

	@Deprecated
	default boolean keyPressed(int key, int scanCode, int modifiers) { return onKeyPressed(key, scanCode, modifiers); }

	@Deprecated
	default boolean keyReleased(int key, int scanCode, int modifiers) { return onKeyReleased(key, scanCode, modifiers); }

	@Deprecated
	default boolean charTyped(char codePoint, int modifiers) { return onCharTyped(codePoint, modifiers); }

	@Deprecated
	default boolean changeFocus(boolean next) { return onChangeFocus(next); }
}
