package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.registries;

import com.google.common.collect.MapMaker;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.adapters.IJAXBObjectAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.adapters.registries.IJAXBObjectAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.tuples.ITuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.def.IRegistryObjectInternal;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

public class JAXBDefaultObjectAdapterRegistry
		extends JAXBAbstractObjectAdapterRegistry
		implements IJAXBObjectAdapterRegistry {
	private static final long serialVersionUID = 8313852600800208719L;
	private static final MapMaker DATA_BUILDER = MapBuilderUtilities.newMapMakerNormalThreaded().initialCapacity(CapacityUtilities.getInitialCapacityMedium());
	private final ConcurrentMap<ITuple2<? extends Class<?>, ? extends Class<?>>, IRegistryObjectInternal<? extends IJAXBObjectAdapter<?, ?>>> data;
	private final ConcurrentMap<Class<?>, ITuple2<? extends Class<?>, ? extends Class<?>>> leftData;
	private final ConcurrentMap<Class<?>, ITuple2<? extends Class<?>, ? extends Class<?>>> rightData;

	public JAXBDefaultObjectAdapterRegistry() {
		super(true);
		this.data = getDataBuilder().makeMap();
		this.leftData = getDataBuilder().makeMap();
		this.rightData = getDataBuilder().makeMap();
	}

	protected static MapMaker getDataBuilder() {
		return DATA_BUILDER;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	@Override
	protected Map<ITuple2<? extends Class<?>, ? extends Class<?>>, IRegistryObjectInternal<? extends IJAXBObjectAdapter<?, ?>>> getData() {
		return data;
	}

	@Override
	protected Logger getLogger() {
		return UIConfiguration.getInstance().getLogger();
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	@Override
	protected ConcurrentMap<Class<?>, ITuple2<? extends Class<?>, ? extends Class<?>>> getLeftData() {
		return leftData;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	@Override
	protected ConcurrentMap<Class<?>, ITuple2<? extends Class<?>, ? extends Class<?>>> getRightData() {
		return rightData;
	}
}
