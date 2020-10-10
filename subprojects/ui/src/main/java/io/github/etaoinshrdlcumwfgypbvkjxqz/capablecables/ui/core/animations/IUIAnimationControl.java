package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations;

import java.util.function.Consumer;

public interface IUIAnimationControl
		extends IUIAnimationControllable {
	void play();

	void pause();

	void reverse();

	void reset();

	void seek(long progress);

	long getDuration();

	void onEnd(Consumer<? super IUIAnimationControl> action);
}
