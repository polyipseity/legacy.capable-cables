package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.caches;

import com.google.common.base.Suppliers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ConcurrencyUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.AbstractRegistry;

import java.util.function.Supplier;

public final class UICacheRegistry
		extends AbstractRegistry<INamespacePrefixedString, IUICacheType<?, ?>> {
	private static final Supplier<UICacheRegistry> INSTANCE = Suppliers.memoize(UICacheRegistry::new);
	private static final long serialVersionUID = 6387752008636256568L;

	protected UICacheRegistry() {
		super(true, UIConfiguration.getInstance().getLogger(),
				builder -> builder
						.concurrencyLevel(ConcurrencyUtilities.getNormalThreadThreadCount())
						.initialCapacity(CapacityUtilities.getInitialCapacityMedium()));
	}

	public static UICacheRegistry getInstance() { return AssertionUtilities.assertNonnull(INSTANCE.get()); }
}
