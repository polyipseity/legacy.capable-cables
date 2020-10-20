package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.traits;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import org.jetbrains.annotations.NonNls;

import java.util.Optional;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IHasBindingKey {
	Optional<? extends INamespacePrefixedString> getBindingKey();

	enum StaticHolder {
		;

		public static final @NonNls String DEFAULT_NAMESPACE = "default";
		public static final @NonNls String DEFAULT_PREFIX = DEFAULT_NAMESPACE + INamespacePrefixedString.StaticHolder.SEPARATOR;

		public static @NonNls String getDefaultNamespace() { return DEFAULT_NAMESPACE; }

		public static @NonNls String getDefaultPrefix() { return DEFAULT_PREFIX; }
	}
}
