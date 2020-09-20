package $group__.ui.core.mvvm.views.components.extensions.caches;

import $group__.ui.core.mvvm.extensions.IUIExtension;
import $group__.utilities.extensions.AbstractExtensionRegistry;
import $group__.utilities.extensions.IExtensionContainer;
import $group__.utilities.structures.INamespacePrefixedString;
import $group__.utilities.structures.ImmutableNamespacePrefixedString;
import $group__.utilities.structures.Registry;
import com.google.common.cache.Cache;

import java.util.Optional;

public interface IUIExtensionCache
		extends IUIExtension<INamespacePrefixedString, IExtensionContainer<INamespacePrefixedString>> {
	INamespacePrefixedString KEY = new ImmutableNamespacePrefixedString(INamespacePrefixedString.StaticHolder.getDefaultNamespace(), AREA_UI + ".cache");
	@SuppressWarnings("unchecked")
	Registry.RegistryObject<IUIExtension.IType<INamespacePrefixedString, IUIExtensionCache, IExtensionContainer<INamespacePrefixedString>>> TYPE =
			AbstractExtensionRegistry.INSTANCE.registerApply(KEY, k -> new IUIExtension.IType.Impl<>(k, (t, i) -> (Optional<? extends IUIExtensionCache>) i.getExtension(t.getKey())));

	Cache<INamespacePrefixedString, Object> getDelegated();
}
