package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.caches;

import com.google.common.cache.Cache;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.extensions.IUIExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.extensions.UIExtensionRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.impl.ImmutableExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.core.IRegistryObject;

import java.util.Optional;

public interface IUICacheExtension
		extends IUIExtension<IIdentifier, IExtensionContainer<IIdentifier>> {

	Cache<IIdentifier, Object> getDelegate();

	enum StaticHolder {
		;

		private static final IIdentifier KEY = ImmutableIdentifier.ofInterning(IUIExtension.StaticHolder.getDefaultNamespace(), "cache");
		@SuppressWarnings("unchecked")
		private static final IRegistryObject<IExtensionType<IIdentifier, IUICacheExtension, IExtensionContainer<IIdentifier>>> TYPE =
				UIExtensionRegistry.getInstance().register(getKey(), new ImmutableExtensionType<>(getKey(), (t, i) -> (Optional<? extends IUICacheExtension>) i.getExtension(t.getKey())));

		public static IIdentifier getKey() {
			return KEY;
		}

		public static IRegistryObject<IExtensionType<IIdentifier, IUICacheExtension, IExtensionContainer<IIdentifier>>> getType() {
			return TYPE;
		}
	}
}
