package $group__.ui.core.mvvm.views.components.extensions.caches;

import $group__.ui.core.mvvm.extensions.IUIExtension;
import $group__.utilities.extensions.IExtensionContainer;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.structures.NamespacePrefixedString;
import $group__.utilities.structures.Registry;
import com.google.common.cache.Cache;

import java.util.Optional;

public interface IUIExtensionCache
		extends IUIExtension<INamespacePrefixedString, IExtensionContainer<INamespacePrefixedString>> {
	INamespacePrefixedString KEY = new NamespacePrefixedString(INamespacePrefixedString.DEFAULT_NAMESPACE, AREA_UI + ".cache");
	@SuppressWarnings("unchecked")
	Registry.RegistryObject<IUIExtension.IType<INamespacePrefixedString, IUIExtensionCache, IExtensionContainer<INamespacePrefixedString>>> TYPE =
			ExtensionRegistry.INSTANCE.registerApply(KEY, k -> new IUIExtension.IType.Impl<>(k, (t, i) -> (Optional<? extends IUIExtensionCache>) i.getExtension(t.getKey())));

	Cache<INamespacePrefixedString, Object> getDelegated();
}
