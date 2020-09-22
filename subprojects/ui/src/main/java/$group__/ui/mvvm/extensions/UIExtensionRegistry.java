package $group__.ui.mvvm.extensions;

import $group__.ui.UIConfiguration;
import $group__.utilities.PreconditionUtilities;
import $group__.utilities.extensions.AbstractExtensionRegistry;
import $group__.utilities.extensions.core.IExtensionType;
import $group__.utilities.structures.INamespacePrefixedString;

public final class UIExtensionRegistry
		extends AbstractExtensionRegistry<INamespacePrefixedString, IExtensionType<? extends INamespacePrefixedString, ?, ?>> {
	private static final UIExtensionRegistry INSTANCE = new UIExtensionRegistry();

	private UIExtensionRegistry() {
		super(true, UIConfiguration.getInstance().getLogger());
		PreconditionUtilities.requireRunOnceOnly();
	}

	public static UIExtensionRegistry getInstance() { return INSTANCE; }
}
