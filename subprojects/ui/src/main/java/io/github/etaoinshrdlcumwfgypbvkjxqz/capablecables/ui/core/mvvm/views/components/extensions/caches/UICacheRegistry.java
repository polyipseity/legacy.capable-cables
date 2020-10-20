package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.caches;

import com.google.common.base.Suppliers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.Registry;

import java.util.function.Supplier;

public final class UICacheRegistry
		extends Registry<INamespacePrefixedString, IUICacheType<?, ?>> {
	private static final Supplier<UICacheRegistry> INSTANCE = Suppliers.memoize(UICacheRegistry::new);

	protected UICacheRegistry() {
		super(true, UIConfiguration.getInstance().getLogger());
	}

	public static UICacheRegistry getInstance() { return AssertionUtilities.assertNonnull(INSTANCE.get()); }
}
