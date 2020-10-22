package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.extensions;

import com.google.common.base.Suppliers;
import com.google.common.collect.MapMaker;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.impl.AbstractExtensionRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.core.IRegistryObjectInternal;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

public final class UIExtensionRegistry
		extends AbstractExtensionRegistry<INamespacePrefixedString, IExtensionType<? extends INamespacePrefixedString, ?, ?>> {
	private static final Supplier<UIExtensionRegistry> INSTANCE = Suppliers.memoize(UIExtensionRegistry::new);
	private static final long serialVersionUID = -7931184191525680470L;
	private static final MapMaker DATA_BUILDER =
			MapBuilderUtilities.newMapMakerNormalThreaded().initialCapacity(CapacityUtilities.getInitialCapacityMedium());
	private final ConcurrentMap<INamespacePrefixedString, IRegistryObjectInternal<? extends IExtensionType<? extends INamespacePrefixedString, ?, ?>>> data =
			getDataBuilder().makeMap();

	private UIExtensionRegistry() {
		super(true);
	}

	private static MapMaker getDataBuilder() { return DATA_BUILDER; }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	@Override
	protected Map<INamespacePrefixedString, IRegistryObjectInternal<? extends IExtensionType<? extends INamespacePrefixedString, ?, ?>>> getData() {
		return data;
	}

	@Override
	protected Logger getLogger() {
		return UIConfiguration.getInstance().getLogger();
	}

	public static UIExtensionRegistry getInstance() { return AssertionUtilities.assertNonnull(INSTANCE.get()); }
}
