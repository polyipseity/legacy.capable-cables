package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.extensions;

import com.google.common.base.Suppliers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.AbstractExtensionRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.INamespacePrefixedString;

import java.util.function.Supplier;

public final class UIExtensionRegistry
		extends AbstractExtensionRegistry<INamespacePrefixedString, IExtensionType<? extends INamespacePrefixedString, ?, ?>> {
	private static final Supplier<UIExtensionRegistry> INSTANCE = Suppliers.memoize(UIExtensionRegistry::new);

	private UIExtensionRegistry() {
		super(true, UIConfiguration.getInstance().getLogger());
	}

	public static UIExtensionRegistry getInstance() { return AssertionUtilities.assertNonnull(INSTANCE.get()); }
}
