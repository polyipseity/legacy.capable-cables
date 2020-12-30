package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.adapters.IJAXBAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.adapters.IJAXBAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.def.UncheckedAutoCloseable;

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

	@Override
	public Optional<? extends IJAXBAdapterContext> getThreadLocalContext() {
		return Optional.ofNullable(getContextThreadLocal().get());
	}

	protected ThreadLocal<IJAXBAdapterContext> getContextThreadLocal() {
		return contextThreadLocal;
	}
}
