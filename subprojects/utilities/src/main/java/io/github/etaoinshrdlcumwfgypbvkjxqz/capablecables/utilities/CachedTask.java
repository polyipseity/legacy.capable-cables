package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;

import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class CachedTask<T, R>
		extends AbstractDelegatingObject<Function<@Nonnull ? super T, @Nonnull ? extends R>>
		implements Function<T, R> {
	private final BiPredicate<@Nonnull ? super T, @Nonnull ? super T> inputChecker;

	private @Nullable T lastInput;
	private @Nullable R lastOutput;

	public CachedTask(Function<@Nonnull ? super T, @Nonnull ? extends R> delegate, BiPredicate<@Nonnull ? super T, @Nonnull ? super T> inputChecker) {
		super(delegate);
		this.inputChecker = inputChecker;
	}

	@Override
	public R apply(T t) {
		R result;
		if (getLastInput()
				.map(lastInput -> getInputChecker().test(lastInput, t)) // COMMENT true means reuse cache
				.orElse(false)) {
			assert getLastOutput().isPresent();
			result = getLastOutput().get();
		} else {
			setLastInput(t);
			setLastOutput(result = getDelegate().apply(t));
		}
		return result;
	}

	protected Optional<? extends T> getLastInput() {
		return Optional.ofNullable(lastInput);
	}

	protected BiPredicate<? super T, ? super T> getInputChecker() {
		return inputChecker;
	}

	protected Optional<? extends R> getLastOutput() {
		return Optional.ofNullable(lastOutput);
	}

	protected void setLastOutput(@Nullable R lastOutput) {
		this.lastOutput = lastOutput;
	}

	protected void setLastInput(@Nullable T lastInput) {
		this.lastInput = lastInput;
	}

	public void invalidate() {
		setLastInput(null);
		setLastOutput(null);
	}
}
