package $group__.utilities.extensions;

import $group__.utilities.CastUtilities;
import $group__.utilities.specific.ThrowableUtilities.BecauseOf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Optional;

@SuppressWarnings("UnusedReturnValue")
public interface IExtensionContainer<K, V extends IExtension<?>> {
	Logger LOGGER = LogManager.getLogger();

	static <C extends IExtensionContainer<?, ? super V>, V extends IExtension<? super C>> Optional<? super V> addExtensionSafe(C container, V extension) { return container.addExtension(extension); }

	Optional<V> addExtension(V extension);

	static <K, V extends IExtension<?>> Optional<V> getExtension(Map<K, V> extensions, K key) { return Optional.ofNullable(extensions.get(key)); }

	static <K, V extends IExtension<?>> Optional<V> addExtension(IExtensionContainer<K, V> self, Map<K, V> extensions, K key, V extension) {
		if (!extension.getGenericClass().isInstance(self))
			throw BecauseOf.illegalArgument("Self is not an instance of extension's container class",
					"self.getClass()", self.getClass(),
					"extension.getGenericClass()", extension.getGenericClass(),
					"extension", extension,
					"self", self);

		Optional<V> ret = self.removeExtension(key);
		extensions.put(key, extension);
		extension.onExtensionAdded(CastUtilities.castUnchecked(self)); // COMMENT type checked above
		return ret;
	}

	Optional<V> removeExtension(K key);

	static <K, V extends IExtension<?>> Optional<V> removeExtension(Map<K, V> extensions, K key) {
		return Optional.ofNullable(extensions.remove(key)).filter(eo -> {
			eo.onExtensionRemoved();
			return true;
		});
	}

	Optional<V> getExtension(K key);

	Map<K, V> getExtensionsView();
}
