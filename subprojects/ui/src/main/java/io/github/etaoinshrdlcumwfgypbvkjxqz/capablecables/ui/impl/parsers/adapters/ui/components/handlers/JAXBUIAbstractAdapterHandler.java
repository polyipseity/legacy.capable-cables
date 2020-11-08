package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui.components.handlers;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IJAXBAdapterContext;

import java.util.function.BiConsumer;

public abstract class JAXBUIAbstractAdapterHandler<L>
		implements BiConsumer<@Nonnull IJAXBAdapterContext, @Nonnull L> {}
