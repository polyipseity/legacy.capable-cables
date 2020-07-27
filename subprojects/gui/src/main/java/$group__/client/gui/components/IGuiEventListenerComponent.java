package $group__.client.gui.components;

import $group__.annotations.OverridingStatus;
import $group__.client.gui.ConstantsGui;
import $group__.client.gui.structures.AffineTransformStack;
import $group__.client.gui.structures.EnumGuiMouseClickResult;
import $group__.client.gui.structures.GuiDragInfo;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import javax.annotation.meta.When;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Optional;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@SuppressWarnings("DeprecatedIsStillUsed")
@OnlyIn(CLIENT)
public interface IGuiEventListenerComponent extends IGuiEventListener {
	GuiComponent<?> getComponent();

	Optional<GuiDragInfo> getDragInfo(int button);

	default void onMouseHover(AffineTransformStack stack, Point2D mouse) {}

	default void onMouseHovering(AffineTransformStack stack, Point2D mouse) {}

	default void onMouseHovered(AffineTransformStack stack, Point2D mouse) {}

	default EnumGuiMouseClickResult onMouseClicked(AffineTransformStack stack, GuiDragInfo drag, Point2D mouse, int button) { return EnumGuiMouseClickResult.PASS; }

	default boolean onMouseReleased(AffineTransformStack stack, Point2D mouse, int button) { return false; }

	default boolean onMouseDragging(AffineTransformStack stack, GuiDragInfo drag, Rectangle2D mouse, int button) { return false; }

	default boolean onMouseDragged(AffineTransformStack stack, GuiDragInfo drag, Point2D mouse, int button) { return false; }

	default boolean onMouseScrolled(AffineTransformStack stack, Point2D mouse, double scrollDelta) { return false; }

	default boolean isMouseOver(AffineTransformStack stack, Point2D mouse) { return false; }

	default boolean onKeyPressed(int key, int scanCode, int modifiers) { return false; }

	default boolean onKeyReleased(int key, int scanCode, int modifiers) { return false; }

	default boolean onCharTyped(char codePoint, int modifiers) { return false; }

	default boolean onChangeFocus(boolean next) { return false; }

	default void onFocusLost(@Nullable GuiComponent<?> to) {}

	default void onFocusGet(@Nullable GuiComponent<?> from) {}

	////////// Deprecated //////////

	@Override
	@Deprecated
	@OverridingStatus(group = ConstantsGui.GROUP, when = When.NEVER)
	default void mouseMoved(double mouseX, double mouseY) { onMouseHovering(new AffineTransformStack(), new Point2D.Double(mouseX, mouseY)); }

	@Override
	@Deprecated
	@OverridingStatus(group = ConstantsGui.GROUP, when = When.NEVER)
	default boolean mouseClicked(double mouseX, double mouseY, int button) {
		AffineTransformStack stack = new AffineTransformStack();
		return onMouseClicked(stack, new GuiDragInfo(getComponent(), new Point2D.Double(mouseX, mouseY), button), new Point2D.Double(mouseX, mouseY), button).result;
	}

	@Override
	@Deprecated
	@OverridingStatus(group = ConstantsGui.GROUP, when = When.NEVER)
	default boolean mouseReleased(double mouseX, double mouseY, int button) { return onMouseReleased(new AffineTransformStack(), new Point2D.Double(mouseX, mouseY), button); }

	@Override
	@Deprecated
	@OverridingStatus(group = ConstantsGui.GROUP, when = When.NEVER)
	default boolean mouseDragged(double mouseX, double mouseY, int button, double mouseXDiff, double mouseYDiff) { return getDragInfo(button).filter(d -> onMouseDragging(new AffineTransformStack(), d, new Rectangle2D.Double(mouseX, mouseY, mouseXDiff, mouseYDiff), button)).isPresent(); }

	@Override
	@OverridingStatus(group = ConstantsGui.GROUP, when = When.NEVER)
	@Deprecated
	default boolean mouseScrolled(double mouseX, double mouseY, double scrollDelta) { return onMouseScrolled(new AffineTransformStack(), new Point2D.Double(mouseX, mouseY), scrollDelta); }

	@Override
	@Deprecated
	@OverridingStatus(group = ConstantsGui.GROUP, when = When.NEVER)
	default boolean isMouseOver(double mouseX, double mouseY) { return isMouseOver(new AffineTransformStack(), new Point2D.Double(mouseX, mouseY)); }

	@Override
	@Deprecated
	@OverridingStatus(group = ConstantsGui.GROUP, when = When.NEVER)
	default boolean keyPressed(int key, int scanCode, int modifiers) { return onKeyPressed(key, scanCode, modifiers); }

	@Override
	@Deprecated
	@OverridingStatus(group = ConstantsGui.GROUP, when = When.NEVER)
	default boolean keyReleased(int key, int scanCode, int modifiers) { return onKeyReleased(key, scanCode, modifiers); }

	@Override
	@Deprecated
	@OverridingStatus(group = ConstantsGui.GROUP, when = When.NEVER)
	default boolean charTyped(char codePoint, int modifiers) { return onCharTyped(codePoint, modifiers); }

	@Override
	@Deprecated
	@OverridingStatus(group = ConstantsGui.GROUP, when = When.NEVER)
	default boolean changeFocus(boolean next) { return onChangeFocus(next); }
}
