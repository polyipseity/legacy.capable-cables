package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations;

public interface IUIAnimationControllable {
	EnumUpdateResult update();

	void render();

	enum EnumUpdateResult {
		NORMAL,
		END,
	}
}
