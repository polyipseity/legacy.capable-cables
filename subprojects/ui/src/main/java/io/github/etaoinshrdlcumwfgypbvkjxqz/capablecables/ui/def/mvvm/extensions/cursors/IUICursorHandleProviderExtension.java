package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.extensions.cursors;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.extensions.IUIExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.extensions.UIExtensionRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.def.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.impl.ImmutableExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.def.IRegistryObject;

import java.util.Optional;

public interface IUICursorHandleProviderExtension
		extends IUIExtension<IIdentifier, IUIView<?>>, ICursorHandleProvider {
	enum StaticHolder {
		;

		private static final IIdentifier KEY = ImmutableIdentifier.ofInterning(IUIExtension.StaticHolder.getDefaultNamespace(), "cursor_custom");
		@SuppressWarnings("unchecked")
		private static final
		IRegistryObject<IExtensionType<IIdentifier, IUICursorHandleProviderExtension, IUIView<?>>> TYPE =
				UIExtensionRegistry.getInstance().register(getKey(), new ImmutableExtensionType<>(getKey(), (t, i) -> (Optional<? extends IUICursorHandleProviderExtension>) i.getExtension(t.getKey())));

		public static IIdentifier getKey() {
			return KEY;
		}

		public static IRegistryObject<IExtensionType<IIdentifier, IUICursorHandleProviderExtension, IUIView<?>>> getType() {
			return TYPE;
		}
	}
}
