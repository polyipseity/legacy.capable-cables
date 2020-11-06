package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.IUIParser;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.UIParserCheckedException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.UIParserUncheckedException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;

import java.util.Optional;

public abstract class UIAbstractParser<T, R, P>
		implements IUIParser<T, R> {
	@Nullable
	private P parsed;

	protected Optional<? extends P> getParsed() { return Optional.ofNullable(parsed); }

	protected void setParsed(@Nullable P parsed) { this.parsed = parsed; }

	@Override
	public final void parse(R resource)
			throws UIParserCheckedException, UIParserUncheckedException {
		reset();
		try {
			setParsed(parse0(resource));
		} catch (Throwable throwable) {
			throw ThrowableUtilities.propagate(throwable, UIParserCheckedException::new, UIParserUncheckedException::new);
		}
	}

	protected abstract P parse0(R resource)
			throws Throwable;

	@Override
	public void reset() {
		setParsed(null);
	}
}
