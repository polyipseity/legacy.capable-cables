package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IJAXBAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IJAXBAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.core.UncheckedAutoCloseable;

import java.util.Optional;

public abstract class JAXBAbstractAdapter<L, R>
		implements IJAXBAdapter<L, R> {
	private final ThreadLocal<IJAXBAdapterContext> contextThreadLocal = new ThreadLocal<>();

	@Override
	public UncheckedAutoCloseable pushThreadLocalContext(@Nullable IJAXBAdapterContext context) {
		@Nullable IJAXBAdapterContext previousContext = getThreadLocalContext().orElse(null);
		getContextThreadLocal().set(context);
		return () -> pushThreadLocalContext(previousContext);
	}

	protected ThreadLocal<IJAXBAdapterContext> getContextThreadLocal() {
		return contextThreadLocal;
	}

	@Override
	public Optional<? extends IJAXBAdapterContext> getThreadLocalContext() {
		return Optional.ofNullable(getContextThreadLocal().get());
	}
}
