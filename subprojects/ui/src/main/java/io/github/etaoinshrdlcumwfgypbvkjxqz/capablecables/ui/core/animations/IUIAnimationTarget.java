package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations;

import javax.annotation.Nonnull;
import java.util.function.DoubleConsumer;

@FunctionalInterface
public interface IUIAnimationTarget
		extends DoubleConsumer {
	@Override
	void accept(double progress);

	@Nonnull
	@Override
	@Deprecated
	default DoubleConsumer andThen(@Nonnull DoubleConsumer after) { return DoubleConsumer.super.andThen(after); }
}
