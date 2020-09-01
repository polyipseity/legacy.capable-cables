package $group__.utilities.collections;

import java.util.Map;

public interface ILoadingMap<K, V>
		extends Map<K, V> {
	V getLoaded(K key);
}
