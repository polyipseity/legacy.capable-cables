package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.components.handlers;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.NonnullBiConsumer;

public abstract class UIAbstractDefaultParserHandler<C, O>
		implements NonnullBiConsumer<C, O> {
	@Override
	public abstract void acceptNonnull(C context, O object);
}
