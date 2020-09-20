package $group__.ui.mvvm.extensions;

import $group__.ui.UIConfiguration;
import $group__.utilities.PreconditionUtilities;
import $group__.utilities.extensions.AbstractExtensionRegistry;
import $group__.utilities.extensions.IExtension;
import $group__.utilities.structures.INamespacePrefixedString;

public final class UIExtensionRegistry
		extends AbstractExtensionRegistry<INamespacePrefixedString, IExtension.IType<? extends INamespacePrefixedString, ?, ?>> {
	private static final UIExtensionRegistry INSTANCE = new UIExtensionRegistry();

	private UIExtensionRegistry() {
		super(true, UIConfiguration.getInstance().getLogger());
		PreconditionUtilities.requireRunOnceOnly(UIConfiguration.getInstance().getLogger());
	}

	public static UIExtensionRegistry getInstance() { return INSTANCE; }
}
