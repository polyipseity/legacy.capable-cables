package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.extensions.cursors;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.extensions.IUIExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.extensions.UIExtensionRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.ImmutableExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.registering.Registry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;

import java.util.Optional;

public interface IUICursorHandleProviderExtension
		extends IUIExtension<INamespacePrefixedString, IUIView<?>>, ICursorHandleProvider {
	enum StaticHolder {
		;

		private static final INamespacePrefixedString KEY = ImmutableNamespacePrefixedString.of(IUIExtension.StaticHolder.getDefaultNamespace(), "cursor_custom");
		@SuppressWarnings("unchecked")
		private static final
		Registry.RegistryObject<IExtensionType<INamespacePrefixedString, IUICursorHandleProviderExtension, IUIView<?>>> TYPE =
				UIExtensionRegistry.getInstance().registerApply(getKey(), k -> new ImmutableExtensionType<>(k, (t, i) -> (Optional<? extends IUICursorHandleProviderExtension>) i.getExtension(t.getKey())));

		public static INamespacePrefixedString getKey() {
			return KEY;
		}

		public static Registry.RegistryObject<IExtensionType<INamespacePrefixedString, IUICursorHandleProviderExtension, IUIView<?>>> getType() {
			return TYPE;
		}
	}
}
