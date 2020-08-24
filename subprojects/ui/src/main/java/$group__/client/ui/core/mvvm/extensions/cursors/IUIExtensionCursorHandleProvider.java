package $group__.client.ui.core.mvvm.extensions.cursors;

import $group__.client.ui.core.mvvm.extensions.IUIExtension;
import $group__.client.ui.core.mvvm.views.IUIView;
import $group__.utilities.CastUtilities;
import $group__.utilities.NamespaceUtilities;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.structures.NamespacePrefixedString;
import $group__.utilities.structures.Registry;

public interface IUIExtensionCursorHandleProvider
		extends IUIExtension<INamespacePrefixedString, IUIView<?>>, ICursorHandleProvider {
	INamespacePrefixedString KEY = new NamespacePrefixedString(NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT, AREA_UI + ".cursor_custom");
	Registry.RegistryObject<IType<INamespacePrefixedString, IUIExtensionCursorHandleProvider, IUIView<?>>> TYPE =
			RegExtension.INSTANCE.registerApply(KEY, k -> new IType.Impl<>(k, (t, i) -> i.getExtension(t.getKey()).map(CastUtilities::castUnchecked)));
}
