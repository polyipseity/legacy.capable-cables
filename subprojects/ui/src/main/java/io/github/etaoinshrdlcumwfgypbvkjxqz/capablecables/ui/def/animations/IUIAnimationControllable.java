package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.animations;

public interface IUIAnimationControllable {
	EnumUpdateResult update();

	void render();

	enum EnumUpdateResult {
		NORMAL,
		END,
	}
}
