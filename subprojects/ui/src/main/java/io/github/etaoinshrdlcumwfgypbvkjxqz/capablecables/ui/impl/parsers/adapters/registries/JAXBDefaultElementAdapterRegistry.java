package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.registries;

import com.google.common.collect.MapMaker;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.adapters.IJAXBElementAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.tuples.ITuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.def.IRegistryObjectInternal;
import org.slf4j.Logger;

import javax.xml.namespace.QName;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

public class JAXBDefaultElementAdapterRegistry
		extends JAXBAbstractElementAdapterRegistry {
	private static final MapMaker DATA_BUILDER = MapBuilderUtilities.newMapMakerNormalThreaded().initialCapacity(CapacityUtilities.getInitialCapacityMedium());
	private static final long serialVersionUID = -7322246695259185454L;
	private final ConcurrentMap<ITuple2<? extends QName, ? extends Class<?>>, IRegistryObjectInternal<? extends IJAXBElementAdapter<?, ?>>> data;
	private final ConcurrentMap<QName, ITuple2<? extends QName, ? extends Class<?>>> leftData;
	private final ConcurrentMap<Class<?>, ITuple2<? extends QName, ? extends Class<?>>> rightData;

	public JAXBDefaultElementAdapterRegistry() {
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
	protected Map<ITuple2<? extends QName, ? extends Class<?>>, IRegistryObjectInternal<? extends IJAXBElementAdapter<?, ?>>> getData() {
		return data;
	}

	@Override
	protected Logger getLogger() {
		return UIConfiguration.getInstance().getLogger();
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	@Override
	protected Map<QName, ITuple2<? extends QName, ? extends Class<?>>> getLeftData() {
		return leftData;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	@Override
	protected Map<Class<?>, ITuple2<? extends QName, ? extends Class<?>>> getRightData() {
		return rightData;
	}
}
