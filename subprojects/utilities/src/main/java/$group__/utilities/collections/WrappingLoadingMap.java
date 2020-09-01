package $group__.utilities.collections;

import java.util.Map;
import java.util.function.Function;

public class WrappingLoadingMap<K, V, M extends Map<K, V>>
		extends DelegatingMap<K, V, M>
		implements ILoadingMap<K, V> {
	protected final Function<? super K, ? extends V> mappingFunction;

	public WrappingLoadingMap(M delegated, Function<? super K, ? extends V> mappingFunction) {
		super(delegated);
		this.mappingFunction = mappingFunction;
	}

	@Override
	public V getLoaded(K key) { return computeIfAbsent(key, getMappingFunction()); }

	protected Function<? super K, ? extends V> getMappingFunction() { return mappingFunction; }
}
