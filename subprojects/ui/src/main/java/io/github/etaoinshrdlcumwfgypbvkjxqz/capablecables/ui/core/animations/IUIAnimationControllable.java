package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations;

@FunctionalInterface
public interface IUIAnimationControllable {
	IUIAnimationControl.EnumUpdateResult update();

	enum EnumUpdateResult {
		NORMAL,
		END,
	}
}
