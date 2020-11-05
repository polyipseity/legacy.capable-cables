package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IJAXBAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IJAXBAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.UncheckedAutoCloseable;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.BiFunction;

public abstract class AbstractJAXBAdapter<L, R>
		implements IJAXBAdapter<L, R> {
	private final BiFunction<? super IJAXBAdapterContext, ? super L, ? extends R> leftToRightFunction;
	private final BiFunction<? super IJAXBAdapterContext, ? super R, ? extends L> rightToLeftFunction;
	private final ThreadLocal<IJAXBAdapterContext> contextThreadLocal = new ThreadLocal<>();

	public AbstractJAXBAdapter(BiFunction<? super IJAXBAdapterContext, ? super L, ? extends R> leftToRightFunction,
	                           BiFunction<? super IJAXBAdapterContext, ? super R, ? extends L> rightToLeftFunction) {
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

	protected BiFunction<? super IJAXBAdapterContext, ? super L, ? extends R> getLeftToRightFunction() {
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

	protected BiFunction<? super IJAXBAdapterContext, ? super R, ? extends L> getRightToLeftFunction() {
		return rightToLeftFunction;
	}
}
