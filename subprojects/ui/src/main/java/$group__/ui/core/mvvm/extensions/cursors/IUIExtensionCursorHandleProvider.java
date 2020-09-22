package $group__.ui.core.mvvm.extensions.cursors;

import $group__.ui.core.mvvm.extensions.IUIExtension;
import $group__.ui.core.mvvm.views.IUIView;
import $group__.ui.mvvm.extensions.UIExtensionRegistry;
import $group__.utilities.extensions.DefaultExtensionType;
import $group__.utilities.extensions.core.IExtensionType;
import $group__.utilities.structures.INamespacePrefixedString;
import $group__.utilities.structures.ImmutableNamespacePrefixedString;
import $group__.utilities.structures.Registry;

import java.util.Optional;

public interface IUIExtensionCursorHandleProvider
		extends IUIExtension<INamespacePrefixedString, IUIView<?>>, ICursorHandleProvider {
	INamespacePrefixedString KEY = new ImmutableNamespacePrefixedString(INamespacePrefixedString.StaticHolder.DEFAULT_NAMESPACE, "cursor_custom");
	@SuppressWarnings("unchecked")
	Registry.RegistryObject<IExtensionType<INamespacePrefixedString, IUIExtensionCursorHandleProvider, IUIView<?>>> TYPE =
			UIExtensionRegistry.getInstance().registerApply(KEY, k -> new DefaultExtensionType<>(k, (t, i) -> (Optional<? extends IUIExtensionCursorHandleProvider>) i.getExtension(t.getKey())));
}
