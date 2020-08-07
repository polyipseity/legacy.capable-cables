package $group__.utilities.interfaces;

import java.util.Map;
import java.util.Optional;

@SuppressWarnings("UnusedReturnValue")
public interface IExtensionContainer<K, V extends IExtension<?>> {
	Optional<V> getExtension(K key);

	Optional<V> addExtension(V extension);

	Optional<V> removeExtension(K key);

	Map<K, V> getExtensionsView();
}
