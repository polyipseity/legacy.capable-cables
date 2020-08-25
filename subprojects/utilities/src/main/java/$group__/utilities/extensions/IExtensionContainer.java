package $group__.utilities.extensions;

import $group__.utilities.CastUtilities;
import $group__.utilities.ThrowableUtilities.BecauseOf;
import $group__.utilities.interfaces.IHasGenericClass;
import $group__.utilities.interfaces.INamespacePrefixedString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Optional;

public interface IExtensionContainer<K> {
	Logger LOGGER = LogManager.getLogger();

	@SuppressWarnings("UnusedReturnValue")
	static <K, V extends IExtension<? extends K, ? super C>, C extends IExtensionContainer<K>> Optional<? extends IExtension<? extends K, ?>> addExtensionSafe(C container, V extension) { return container.addExtension(extension); }

	Optional<? extends IExtension<? extends K, ?>> addExtension(IExtension<? extends K, ?> extension);

	@SuppressWarnings("UnusedReturnValue")
	static <K, V extends IExtension<? extends K, C> & IHasGenericClass.Extended<C, ? super E>, C extends IExtensionContainer<K>, E extends C> Optional<? extends IExtension<? extends K, ?>> addExtensionSafeExtended(E container, V extension) { return container.addExtension(extension); }

	static <K> Optional<? extends IExtension<? extends K, ?>> getExtension(Map<K, ? extends IExtension<? extends K, ?>> extensions, K key) { return Optional.ofNullable(extensions.get(key)); }

	@SuppressWarnings("unchecked")
	static <K> Optional<? extends IExtension<? extends K, ?>> addExtensionImpl(IExtensionContainer<K> self, Map<K, ? super IExtension<? extends K, ?>> extensions, K key, IExtension<? extends K, ?> extension) {
		if (extension.getType().getKey() instanceof INamespacePrefixedString)
			IExtension.RegExtension.checkExtensionRegistered((IExtension<? extends INamespacePrefixedString, ?>) extension); // COMMENT checked
		if (!extension.getGenericClass().isInstance(self))
			throw BecauseOf.illegalArgument("Self is not an instance of extension's container class",
					"self.getClass()", self.getClass(),
					"extension.getGenericClass()", extension.getGenericClass(),
					"extension", extension,
					"self", self);

		Optional<? extends IExtension<? extends K, ?>> ret = self.removeExtension(key);
		extensions.put(key, extension);
		extension.onExtensionAdded(CastUtilities.castUnchecked(self)); // COMMENT type checked above
		return ret;
	}

	Optional<? extends IExtension<? extends K, ?>> removeExtension(K key);

	static <K, V extends IExtension<? extends K, ?>> Optional<V> removeExtension(Map<K, V> extensions, K key) {
		return Optional.ofNullable(extensions.remove(key)).filter(eo -> {
			eo.onExtensionRemoved();
			return true;
		});
	}

	Optional<? extends IExtension<? extends K, ?>> getExtension(K key);

	Map<K, IExtension<? extends K, ?>> getExtensionsView();
}
