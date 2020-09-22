package $group__.ui.core.mvvm.views.components.extensions.caches;

import $group__.ui.core.mvvm.extensions.IUIExtension;
import $group__.ui.mvvm.extensions.UIExtensionRegistry;
import $group__.utilities.extensions.DefaultExtensionType;
import $group__.utilities.extensions.core.IExtensionContainer;
import $group__.utilities.extensions.core.IExtensionType;
import $group__.utilities.structures.INamespacePrefixedString;
import $group__.utilities.structures.ImmutableNamespacePrefixedString;
import $group__.utilities.structures.Registry;
import com.google.common.cache.Cache;

import java.util.Optional;

public interface IUICacheExtension
		extends IUIExtension<INamespacePrefixedString, IExtensionContainer<INamespacePrefixedString>> {
	INamespacePrefixedString KEY = new ImmutableNamespacePrefixedString(INamespacePrefixedString.StaticHolder.DEFAULT_NAMESPACE, "cache");
	@SuppressWarnings("unchecked")
	Registry.RegistryObject<IExtensionType<INamespacePrefixedString, IUICacheExtension, IExtensionContainer<INamespacePrefixedString>>> TYPE =
			UIExtensionRegistry.getInstance().registerApply(KEY, k -> new DefaultExtensionType<>(k, (t, i) -> (Optional<? extends IUICacheExtension>) i.getExtension(t.getKey())));

	Cache<INamespacePrefixedString, Object> getDelegated();
}
