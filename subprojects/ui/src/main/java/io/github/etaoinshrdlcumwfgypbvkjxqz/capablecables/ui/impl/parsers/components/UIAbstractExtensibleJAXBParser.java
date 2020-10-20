package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.UIAbstractExtensibleParser;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.NonnullBiConsumer;
import jakarta.xml.bind.JAXBElement;

import java.util.concurrent.ConcurrentMap;

public abstract class UIAbstractExtensibleJAXBParser<T, R, P, C>
		extends UIAbstractExtensibleParser<T, R, P, NonnullBiConsumer<? super C, ?>, Class<?>> {
	private final ConcurrentMap<Class<?>, NonnullBiConsumer<? super C, ?>> handlers =
			MapBuilderUtilities.newMapMakerNormalThreaded().weakKeys().initialCapacity(CapacityUtilities.getInitialCapacitySmall()).makeMap();

	public <O> void addObjectHandler(Class<O> discriminator, NonnullBiConsumer<? super C, ? extends O> handler) { addHandler(discriminator, handler); }

	@Override
	@Deprecated
	public void addHandler(Class<?> discriminator, NonnullBiConsumer<? super C, ?> handler) { super.addHandler(discriminator, handler); }

	public <O> void addElementHandler(Class<O> discriminator, NonnullBiConsumer<? super C, ? extends JAXBElement<? super O>> handler) { addHandler(discriminator, handler); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	@Override
	protected ConcurrentMap<Class<?>, NonnullBiConsumer<? super C, ?>> getHandlers() { return handlers; }
}
