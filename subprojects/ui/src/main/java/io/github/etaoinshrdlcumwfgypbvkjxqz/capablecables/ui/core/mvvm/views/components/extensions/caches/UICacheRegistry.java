package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.caches;

import com.google.common.base.Suppliers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.Registry;

public final class UICacheRegistry
		extends Registry<INamespacePrefixedString, IUICacheType<?, ?>> {
	private static final UICacheRegistry INSTANCE = new UICacheRegistry();

	protected UICacheRegistry() {
		super(true, UIConfiguration.getInstance().getLogger());
	}

	public static UICacheRegistry getInstance() { return INSTANCE; }
}
