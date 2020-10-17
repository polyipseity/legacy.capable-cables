package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.caches;

import com.google.common.cache.Cache;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.extensions.IUIExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.extensions.UIExtensionRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.DefaultExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.registering.Registry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;

import java.util.Optional;

public interface IUICacheExtension
		extends IUIExtension<INamespacePrefixedString, IExtensionContainer<INamespacePrefixedString>> {
	INamespacePrefixedString KEY = ImmutableNamespacePrefixedString.of(INamespacePrefixedString.StaticHolder.DEFAULT_NAMESPACE, "cache");
	@SuppressWarnings("unchecked")
	Registry.RegistryObject<IExtensionType<INamespacePrefixedString, IUICacheExtension, IExtensionContainer<INamespacePrefixedString>>> TYPE =
			UIExtensionRegistry.getInstance().registerApply(KEY, k -> new DefaultExtensionType<>(k, (t, i) -> (Optional<? extends IUICacheExtension>) i.getExtension(t.getKey())));

	Cache<INamespacePrefixedString, Object> getDelegated();
}
