package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.extensions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionContainer;
import org.jetbrains.annotations.NonNls;

public interface IUIExtension<K extends INamespacePrefixedString, C extends IExtensionContainer<? super K>>
		extends IExtension<K, C> {
	enum StaticHolder {
		;

		public static final @NonNls String DEFAULT_NAMESPACE = "default";
		public static final @NonNls String DEFAULT_PREFIX = DEFAULT_NAMESPACE + INamespacePrefixedString.StaticHolder.SEPARATOR;

		public static @NonNls String getDefaultNamespace() { return DEFAULT_NAMESPACE; }

		public static @NonNls String getDefaultPrefix() { return DEFAULT_PREFIX; }
	}
}
