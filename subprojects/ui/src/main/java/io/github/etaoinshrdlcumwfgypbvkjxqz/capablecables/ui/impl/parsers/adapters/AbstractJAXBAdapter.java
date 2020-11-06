package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IJAXBAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IJAXBAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.UncheckedAutoCloseable;

import java.util.Optional;
import java.util.function.BiFunction;

public abstract class AbstractJAXBAdapter<L, R>
		implements IJAXBAdapter<L, R> {
	private final BiFunction<@Nonnull ? super IJAXBAdapterContext, @Nonnull ? super L, @Nonnull ? extends R> leftToRightFunction;
	private final BiFunction<@Nonnull ? super IJAXBAdapterContext, @Nonnull ? super R, @Nonnull ? extends L> rightToLeftFunction;
	private final ThreadLocal<IJAXBAdapterContext> contextThreadLocal = new ThreadLocal<>();

	public AbstractJAXBAdapter(BiFunction<@Nonnull ? super IJAXBAdapterContext, @Nonnull ? super L, @Nonnull ? extends R> leftToRightFunction,
	                           BiFunction<@Nonnull ? super IJAXBAdapterContext, @Nonnull ? super R, @Nonnull ? extends L> rightToLeftFunction) {
		this.leftToRightFunction = leftToRightFunction;
		this.rightToLeftFunction = rightToLeftFunction;
	}

	@Override
	public UncheckedAutoCloseable pushThreadLocalContext(@Nullable IJAXBAdapterContext context) {
		@Nullable IJAXBAdapterContext previousContext = getThreadLocalContext().orElse(null);
		getContextThreadLocal().set(context);
		return () -> pushThreadLocalContext(previousContext);
	}

	@Nullable
	@Override
	@Deprecated
	public R leftToRight(@Nullable L left) {
		assert left != null;
		return getThreadLocalContext()
				.map(context -> getLeftToRightFunction().apply(context, left))
				.orElseThrow(IllegalStateException::new);
	}

	protected BiFunction<@Nonnull ? super IJAXBAdapterContext, @Nonnull ? super L, @Nonnull ? extends R> getLeftToRightFunction() {
		return leftToRightFunction;
	}

	protected ThreadLocal<IJAXBAdapterContext> getContextThreadLocal() {
		return contextThreadLocal;
	}

	@Nullable
	@Override
	@Deprecated
	public L rightToLeft(@Nullable R right) {
		assert right != null;
		return getThreadLocalContext()
				.map(context -> getRightToLeftFunction().apply(context, right))
				.orElseThrow(IllegalStateException::new);
	}

	@Override
	public Optional<? extends IJAXBAdapterContext> getThreadLocalContext() {
		return Optional.ofNullable(getContextThreadLocal().get());
	}

	protected BiFunction<@Nonnull ? super IJAXBAdapterContext, @Nonnull ? super R, @Nonnull ? extends L> getRightToLeftFunction() {
		return rightToLeftFunction;
	}
}
