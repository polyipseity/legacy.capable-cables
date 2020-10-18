package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations;

import java.util.function.DoubleConsumer;

@FunctionalInterface
public interface IUIAnimationTarget
		extends DoubleConsumer {
	@Override
	void accept(double progress);

	default IUIAnimationTarget andThen(IUIAnimationTarget after) {
		return t -> {
			accept(t);
			after.accept(t);
		};
	}
}
