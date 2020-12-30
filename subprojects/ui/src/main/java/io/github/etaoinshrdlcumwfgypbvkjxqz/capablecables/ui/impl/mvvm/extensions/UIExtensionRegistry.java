package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.extensions;

import com.google.common.base.Suppliers;
import com.google.common.collect.MapMaker;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.def.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.impl.AbstractExtensionRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.def.IRegistryObjectInternal;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

public final class UIExtensionRegistry
		extends AbstractExtensionRegistry<IIdentifier, IExtensionType<? extends IIdentifier, ?, ?>> {
	private static final Supplier<@Nonnull UIExtensionRegistry> INSTANCE = Suppliers.memoize(UIExtensionRegistry::new);
	private static final long serialVersionUID = -7931184191525680470L;
	private static final MapMaker DATA_BUILDER =
			MapBuilderUtilities.newMapMakerNormalThreaded().initialCapacity(CapacityUtilities.getInitialCapacityMedium());
	private final ConcurrentMap<IIdentifier, IRegistryObjectInternal<? extends IExtensionType<? extends IIdentifier, ?, ?>>> data =
			getDataBuilder().makeMap();

	private UIExtensionRegistry() {
		super(true);
	}

	private static MapMaker getDataBuilder() { return DATA_BUILDER; }

	public static UIExtensionRegistry getInstance() { return INSTANCE.get(); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	@Override
	protected Map<IIdentifier, IRegistryObjectInternal<? extends IExtensionType<? extends IIdentifier, ?, ?>>> getData() {
		return data;
	}

	@Override
	protected Logger getLogger() {
		return UIConfiguration.getInstance().getLogger();
	}
}
