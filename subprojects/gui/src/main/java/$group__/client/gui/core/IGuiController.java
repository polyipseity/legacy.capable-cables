package $group__.client.gui.core;

import $group__.client.gui.core.structures.AffineTransformStack;
import $group__.client.gui.core.structures.GuiKeyboardKeyPressData;
import $group__.client.gui.core.structures.GuiMouseButtonPressData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.awt.geom.Point2D;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public interface IGuiController<C extends IGuiComponent<?, ?, ?>> {
	Optional<C> getComponent();

	boolean isActive();

	boolean setActive(final AffineTransformStack stack, boolean active);

	boolean onMouseMove(final AffineTransformStack stack, Point2D mouse);

	boolean onMouseHover(final AffineTransformStack stack, EnumStage stage, Point2D mouse);

	boolean onMouseButtonPress(final AffineTransformStack stack, EnumStage stage, GuiMouseButtonPressData data);

	boolean onMouseScroll(final AffineTransformStack stack, Point2D mouse, double delta);

	boolean onKeyboardKeyPress(final AffineTransformStack stack, EnumStage stage, GuiKeyboardKeyPressData data);

	boolean onKeyboardCharType(final AffineTransformStack stack, char codePoint, int modifiers);

	boolean onFocus(final AffineTransformStack stack, EnumStage stage);

	boolean changeFocus(final AffineTransformStack stack, boolean next);

	@OnlyIn(Dist.CLIENT)
	enum EnumStage {
		START,
		ONGOING,
		END,
	}

	@OnlyIn(Dist.CLIENT)
	interface IContainer<C extends IGuiComponent<?, ?, ?>, CH extends IGuiComponent<?, ?, ?>> extends IGuiController<C> {
		static boolean onChangeFocusWithThisFocusable(IGuiComponent.IContainer<?, ?, ?, ?> self, Function<Boolean, Boolean> superMethod, boolean next) {
			return next ? !self.getController().getFocus().isPresent() && !IGuiComponent.isBeingFocused(self) || superMethod.apply(true)
					: superMethod.apply(false) || !IGuiComponent.isBeingFocused(self);
		}

		Optional<CH> getFocus();

		void setFocus(final AffineTransformStack stack, @Nullable CH focus);

		Optional<CH> getMouseHover();

		Map<Integer, GuiKeyboardKeyPressData.Tracked> getKeyboardKeysBeingPressedView();

		Map<Integer, GuiMouseButtonPressData.Tracked> getMouseButtonsBeingPressedView();
	}

	@OnlyIn(Dist.CLIENT)
	interface IManager<C extends IGuiComponent<?, ?, ?>, CH extends IGuiComponent<?, ?, ?>> extends IContainer<C, CH> {}
}
