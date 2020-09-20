package $group__.ui.core.mvvm.views.components.extensions.caches;

import $group__.ui.UIConfiguration;
import $group__.utilities.PreconditionUtilities;
import $group__.utilities.structures.INamespacePrefixedString;
import $group__.utilities.structures.Registry;

public final class UICacheRegistry
		extends Registry<INamespacePrefixedString, IUIExtensionCacheType<?, ?>> {
	private static final UICacheRegistry INSTANCE = new UICacheRegistry();

	protected UICacheRegistry() {
		super(true, UIConfiguration.getInstance().getLogger());
		PreconditionUtilities.requireRunOnceOnly(UIConfiguration.getInstance().getLogger());
	}

	public static UICacheRegistry getInstance() { return INSTANCE; }
}
