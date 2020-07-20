package $group__.client.gui.components;

import $group__.annotations.OverridingStatus;
import $group__.client.gui.ConstantsGui;
import $group__.client.gui.structures.GuiDragInfo;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.meta.When;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@SuppressWarnings("DeprecatedIsStillUsed")
@OnlyIn(CLIENT)
public interface IGuiEventListenerComponent extends IGuiEventListener {
	GuiComponent getComponent();

	default void onMouseMoved(MatrixStack matrix, Point2D mouse) {}

	default boolean onMouseClicked(MatrixStack matrix, Point2D mouse, int button) { return false; }

	default boolean onMouseReleased(MatrixStack matrix, Point2D mouse, int button) { return false; }

	default boolean onMouseDragging(MatrixStack matrix, Rectangle2D mouse, int button) { return false; }

	default boolean onMouseDragged(MatrixStack matrix, GuiDragInfo drag, Point2D mouse, int button) { return false; }

	default boolean onMouseScrolled(MatrixStack matrix, Point2D mouse, double scrollDelta) { return false; }

	default boolean isMouseOver(MatrixStack matrix, Point2D mouse) { return false; }

	default boolean onKeyPressed(int key, int scanCode, int modifiers) { return false; }

	default boolean onKeyReleased(int key, int scanCode, int modifiers) { return false; }

	default boolean onCharTyped(char codePoint, int modifiers) { return false; }

	default boolean onChangeFocus(boolean next) { return false; }

	////////// Deprecated //////////

	@OverridingStatus(group = ConstantsGui.GROUP, when = When.NEVER)
	@Deprecated
	default void mouseMoved(double mouseX, double mouseY) { onMouseMoved(new MatrixStack(), new Point2D.Double(mouseX, mouseY)); }

	@OverridingStatus(group = ConstantsGui.GROUP, when = When.NEVER)
	@Deprecated
	default boolean mouseClicked(double mouseX, double mouseY, int button) { return onMouseClicked(new MatrixStack(), new Point2D.Double(mouseX, mouseY), button); }

	@OverridingStatus(group = ConstantsGui.GROUP, when = When.NEVER)
	@Deprecated
	default boolean mouseReleased(double mouseX, double mouseY, int button) { return onMouseReleased(new MatrixStack(), new Point2D.Double(mouseX, mouseY), button); }

	@OverridingStatus(group = ConstantsGui.GROUP, when = When.NEVER)
	@Deprecated
	default boolean mouseDragged(double mouseX, double mouseY, int button, double mouseXDiff, double mouseYDiff) { return onMouseDragging(new MatrixStack(), new Rectangle2D.Double(mouseX, mouseY, mouseXDiff, mouseYDiff), button); }

	@OverridingStatus(group = ConstantsGui.GROUP, when = When.NEVER)
	@Deprecated
	default boolean mouseScrolled(double mouseX, double mouseY, double scrollDelta) { return onMouseScrolled(new MatrixStack(), new Point2D.Double(mouseX, mouseY), scrollDelta); }

	@OverridingStatus(group = ConstantsGui.GROUP, when = When.NEVER)
	@Deprecated
	default boolean isMouseOver(double mouseX, double mouseY) { return isMouseOver(new MatrixStack(), new Point2D.Double(mouseX, mouseY)); }

	@OverridingStatus(group = ConstantsGui.GROUP, when = When.NEVER)
	@Deprecated
	default boolean keyPressed(int key, int scanCode, int modifiers) { return onKeyPressed(key, scanCode, modifiers); }

	@OverridingStatus(group = ConstantsGui.GROUP, when = When.NEVER)
	@Deprecated
	default boolean keyReleased(int key, int scanCode, int modifiers) { return onKeyReleased(key, scanCode, modifiers); }

	@OverridingStatus(group = ConstantsGui.GROUP, when = When.NEVER)
	@Deprecated
	default boolean charTyped(char codePoint, int modifiers) { return onCharTyped(codePoint, modifiers); }

	@OverridingStatus(group = ConstantsGui.GROUP, when = When.NEVER)
	@Deprecated
	default boolean changeFocus(boolean next) { return onChangeFocus(next); }
}
