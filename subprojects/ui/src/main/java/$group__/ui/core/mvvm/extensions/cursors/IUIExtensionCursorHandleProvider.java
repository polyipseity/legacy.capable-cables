package $group__.ui.core.mvvm.extensions.cursors;

import $group__.ui.core.mvvm.extensions.IUIExtension;
import $group__.ui.core.mvvm.views.IUIView;
import $group__.utilities.extensions.AbstractExtensionRegistry;
import $group__.utilities.structures.INamespacePrefixedString;
import $group__.utilities.structures.ImmutableNamespacePrefixedString;
import $group__.utilities.structures.Registry;

import java.util.Optional;

public interface IUIExtensionCursorHandleProvider
		extends IUIExtension<INamespacePrefixedString, IUIView<?>>, ICursorHandleProvider {
	INamespacePrefixedString KEY = new ImmutableNamespacePrefixedString(INamespacePrefixedString.StaticHolder.getDefaultNamespace(), AREA_UI + ".cursor_custom");
	@SuppressWarnings("unchecked")
	Registry.RegistryObject<IType<INamespacePrefixedString, IUIExtensionCursorHandleProvider, IUIView<?>>> TYPE =
			AbstractExtensionRegistry.INSTANCE.registerApply(KEY, k -> new IType.Impl<>(k, (t, i) -> (Optional<? extends IUIExtensionCursorHandleProvider>) i.getExtension(t.getKey())));
}
