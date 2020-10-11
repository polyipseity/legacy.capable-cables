package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.components.handlers;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.IParserContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IConsumer3;

import javax.annotation.Nullable;

public abstract class UIAbstractComponentParserHandler<C, O, TH extends Throwable>
		implements IConsumer3<IParserContext, C, O, TH> {
	@Override
	public final void accept(@Nullable IParserContext context, @Nullable C container, @Nullable O object) throws TH {
		assert context != null;
		assert container != null;
		assert object != null;
		accept0(context, container, object);
	}

	public abstract void accept0(IParserContext context, C container, O object)
			throws TH;
}
