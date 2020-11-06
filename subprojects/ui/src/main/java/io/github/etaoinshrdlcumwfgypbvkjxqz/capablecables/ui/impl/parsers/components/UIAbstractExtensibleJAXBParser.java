package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.UIAbstractExtensibleParser;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import jakarta.xml.bind.JAXBElement;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;

public abstract class UIAbstractExtensibleJAXBParser<T, R, P, C>
		extends UIAbstractExtensibleParser<T, R, P, BiConsumer<@Nonnull ? super C, @Nonnull ?>, Class<?>> {
	private final ConcurrentMap<Class<?>, BiConsumer<@Nonnull ? super C, @Nonnull ?>> handlers =
			MapBuilderUtilities.newMapMakerNormalThreaded().weakKeys().initialCapacity(CapacityUtilities.getInitialCapacitySmall()).makeMap();

	public <O> void addObjectHandler(Class<O> discriminator, BiConsumer<@Nonnull ? super C, @Nonnull ? extends O> handler) { addHandler(discriminator, handler); }

	@Override
	@Deprecated
	public void addHandler(Class<?> discriminator, BiConsumer<@Nonnull ? super C, @Nonnull ?> handler) { super.addHandler(discriminator, handler); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	@Override
	protected ConcurrentMap<Class<?>, BiConsumer<@Nonnull ? super C, @Nonnull ?>> getHandlers() { return handlers; }

	public <O> void addElementHandler(Class<O> discriminator, BiConsumer<@Nonnull ? super C, @Nonnull ? extends JAXBElement<? super O>> handler) { addHandler(discriminator, handler); }
}
