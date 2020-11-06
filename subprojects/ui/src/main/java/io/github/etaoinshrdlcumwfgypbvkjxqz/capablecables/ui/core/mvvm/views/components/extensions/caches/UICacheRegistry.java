package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.caches;

import com.google.common.base.Suppliers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.core.IRegistryObjectInternal;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.impl.AbstractRegistry;
import org.slf4j.Logger;

import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

public final class UICacheRegistry
		extends AbstractRegistry<INamespacePrefixedString, IUICacheType<?, ?>> {
	private static final Supplier<@Nonnull UICacheRegistry> INSTANCE = Suppliers.memoize(UICacheRegistry::new);
	private static final long serialVersionUID = 6387752008636256568L;

	private final ConcurrentMap<INamespacePrefixedString, IRegistryObjectInternal<? extends IUICacheType<?, ?>>> data =
			MapBuilderUtilities.newMapMakerNormalThreaded().initialCapacity(CapacityUtilities.getInitialCapacityMedium()).makeMap();

	private UICacheRegistry() {
		super(true);
	}

	public static UICacheRegistry getInstance() { return INSTANCE.get(); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	@Override
	protected ConcurrentMap<INamespacePrefixedString, IRegistryObjectInternal<? extends IUICacheType<?, ?>>> getData() {
		return data;
	}

	@Override
	protected Logger getLogger() {
		return UIConfiguration.getInstance().getLogger();
	}
}
