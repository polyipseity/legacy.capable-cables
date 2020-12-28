package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IJAXBAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.ui.components.IJAXBUIComponentBasedAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.JAXBAbstractObjectAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import jakarta.xml.bind.JAXBElement;

import javax.xml.namespace.QName;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;

public abstract class JAXBUIAbstractComponentBasedAdapter<L, R>
		extends JAXBAbstractObjectAdapter<L, R>
		implements IJAXBUIComponentBasedAdapter<L, R> {
	private final ConcurrentMap<Class<?>, BiConsumer<@Nonnull ? super IJAXBAdapterContext, @Nonnull ?>> objectHandlers =
			MapBuilderUtilities.newMapMakerNormalThreaded().weakKeys().initialCapacity(CapacityUtilities.getInitialCapacitySmall()).makeMap();
	private final ConcurrentMap<QName, BiConsumer<@Nonnull ? super IJAXBAdapterContext, @Nonnull ? super JAXBElement<?>>> elementHandlers =
			MapBuilderUtilities.newMapMakerNormalThreaded().initialCapacity(CapacityUtilities.getInitialCapacitySmall()).makeMap();

	@Override
	public <T> void addObjectHandler(Class<T> key, BiConsumer<@Nonnull ? super IJAXBAdapterContext, @Nonnull ? super T> handler) {
		getObjectHandlers().put(key, handler);
	}

	@Override
	public void addElementHandler(QName key, BiConsumer<@Nonnull ? super IJAXBAdapterContext, @Nonnull ? super JAXBElement<?>> handler) {
		getElementHandlers().put(key, handler);
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<QName, BiConsumer<@Nonnull ? super IJAXBAdapterContext, @Nonnull ? super JAXBElement<?>>> getElementHandlers() {
		return elementHandlers;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<Class<?>, BiConsumer<@Nonnull ? super IJAXBAdapterContext, @Nonnull ?>> getObjectHandlers() {
		return objectHandlers;
	}
}
