package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;

import java.util.function.Consumer;

public interface IUIAnimationControl
		extends IUIAnimationControllable {
	void play();

	void pause();

	void reverse();

	void reset();

	void seek(long progress);

	IUIAnimationTime getDuration();

	void onEnd(Consumer<@Nonnull ? super IUIAnimationControl> action);
}
