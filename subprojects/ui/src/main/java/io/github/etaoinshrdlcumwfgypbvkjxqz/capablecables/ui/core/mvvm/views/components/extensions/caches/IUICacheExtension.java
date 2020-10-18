package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.caches;

import com.google.common.cache.Cache;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.extensions.IUIExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.extensions.UIExtensionRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.ImmutableExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.registering.Registry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;

import java.util.Optional;

public interface IUICacheExtension
		extends IUIExtension<INamespacePrefixedString, IExtensionContainer<INamespacePrefixedString>> {

	Cache<INamespacePrefixedString, Object> getDelegate();

	enum StaticHolder {
		;

		private static final INamespacePrefixedString KEY = ImmutableNamespacePrefixedString.of(IUIExtension.StaticHolder.getDefaultNamespace(), "cache");
		@SuppressWarnings("unchecked")
		private static final
		Registry.RegistryObject<IExtensionType<INamespacePrefixedString, IUICacheExtension, IExtensionContainer<INamespacePrefixedString>>> TYPE =
				UIExtensionRegistry.getInstance().registerApply(getKey(), k -> new ImmutableExtensionType<>(k, (t, i) -> (Optional<? extends IUICacheExtension>) i.getExtension(t.getKey())));

		public static INamespacePrefixedString getKey() {
			return KEY;
		}

		public static Registry.RegistryObject<IExtensionType<INamespacePrefixedString, IUICacheExtension, IExtensionContainer<INamespacePrefixedString>>> getType() {
			return TYPE;
		}
	}
}
