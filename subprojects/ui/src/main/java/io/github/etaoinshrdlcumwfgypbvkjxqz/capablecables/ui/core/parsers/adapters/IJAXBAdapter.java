package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.core.IDuplexFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.core.UncheckedAutoCloseable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.ISealedClassCandidate;

import java.util.Optional;

public interface IJAXBAdapter<L, R>
		extends IDuplexFunction<@Nonnull L, @Nonnull R>, ISealedClassCandidate {
	@SuppressWarnings("try")
	static <L, R> R leftToRight(IJAXBAdapter<L, R> instance, IJAXBAdapterContext context, L left)
			throws IllegalArgumentException {
		try (UncheckedAutoCloseable ignored = instance.pushThreadLocalContext(context)) {
			return AssertionUtilities.assertNonnull(instance.leftToRight(left));
		}
	}

	UncheckedAutoCloseable pushThreadLocalContext(@Nullable IJAXBAdapterContext context);

	@Override
	@Nonnull
	@Deprecated
	R leftToRight(@Nonnull L left);

	@Override
	@Nonnull
	@Deprecated
	L rightToLeft(@Nonnull R right);

	@SuppressWarnings("try")
	static <L, R> L rightToLeft(IJAXBAdapter<L, R> instance, IJAXBAdapterContext context, R right)
			throws IllegalArgumentException {
		try (UncheckedAutoCloseable ignored = instance.pushThreadLocalContext(context)) {
			return AssertionUtilities.assertNonnull(instance.rightToLeft(right));
		}
	}

	Optional<? extends IJAXBAdapterContext> getThreadLocalContext();
}
