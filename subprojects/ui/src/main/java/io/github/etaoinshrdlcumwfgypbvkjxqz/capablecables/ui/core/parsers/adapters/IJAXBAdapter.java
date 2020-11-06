package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IDuplexFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.UncheckedAutoCloseable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.ISealedClassCandidate;

import java.util.Optional;

public interface IJAXBAdapter<L, R>
		extends IDuplexFunction<L, R>, ISealedClassCandidate {
	static <L, R> R leftToRight(IJAXBAdapter<L, R> instance, IJAXBAdapterContext context, @Nullable L left)
			throws IllegalArgumentException {
		try (@SuppressWarnings("try") UncheckedAutoCloseable ignored = instance.pushThreadLocalContext(context)) {
			return AssertionUtilities.assertNonnull(instance.leftToRight(left));
		}
	}

	UncheckedAutoCloseable pushThreadLocalContext(@Nullable IJAXBAdapterContext context);

	@Nullable
	@Override
	@Deprecated
	R leftToRight(@Nullable L left);

	@Nullable
	@Override
	@Deprecated
	L rightToLeft(@Nullable R right);

	static <L, R> L rightToLeft(IJAXBAdapter<L, R> instance, IJAXBAdapterContext context, @Nullable R right)
			throws IllegalArgumentException {
		try (@SuppressWarnings("try") UncheckedAutoCloseable ignored = instance.pushThreadLocalContext(context)) {
			return AssertionUtilities.assertNonnull(instance.rightToLeft(right));
		}
	}

	Optional<? extends IJAXBAdapterContext> getThreadLocalContext();
}
