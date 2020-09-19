package $group__.ui.core.mvvm.extensions.cursors;

import $group__.ui.core.mvvm.extensions.IUIExtension;
import $group__.ui.core.mvvm.views.IUIView;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.structures.NamespacePrefixedString;
import $group__.utilities.structures.Registry;

import java.util.Optional;

public interface IUIExtensionCursorHandleProvider
		extends IUIExtension<INamespacePrefixedString, IUIView<?>>, ICursorHandleProvider {
	INamespacePrefixedString KEY = new NamespacePrefixedString(INamespacePrefixedString.DEFAULT_NAMESPACE, AREA_UI + ".cursor_custom");
	@SuppressWarnings("unchecked")
	Registry.RegistryObject<IType<INamespacePrefixedString, IUIExtensionCursorHandleProvider, IUIView<?>>> TYPE =
			ExtensionRegistry.INSTANCE.registerApply(KEY, k -> new IType.Impl<>(k, (t, i) -> (Optional<? extends IUIExtensionCursorHandleProvider>) i.getExtension(t.getKey())));
}
