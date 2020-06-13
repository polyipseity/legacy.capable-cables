package $group__.utilities.extensions.delegated;

import $group__.traits.IAdapter;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface IMapDelegated<M extends Map<K, V>, K, V> extends Map<K, V>, IAdapter<M> {
	@Override
	@Deprecated
	default M get() { return getMap(); }

	@Override
	@Deprecated
	default void set(M value) { setMap(value); }

	M getMap();

	void setMap(M map);

	@Override
	default int size() { return getMap().size(); }

	@Override
	default boolean isEmpty() { return getMap().isEmpty(); }

	@Override
	default boolean containsKey(Object key) { return getMap().containsKey(key); }

	@Override
	default boolean containsValue(Object value) { return getMap().containsValue(value); }

	@Override
	default V get(Object key) { return getMap().get(key); }


	@Override
	default V put(K key, V value) { return getMap().put(key, value); }

	@Override
	default V remove(Object key) { return getMap().remove(key); }


	@Override
	default void putAll(Map<? extends K, ? extends V> m) { getMap().putAll(m); }

	@Override
	default void clear() { getMap().clear(); }


	@Override
	default Set<K> keySet() { return getMap().keySet(); }

	@Override
	default Collection<V> values() { return getMap().values(); }

	@Override
	default Set<Entry<K, V>> entrySet() { return getMap().entrySet(); }


	@Override
	boolean equals(Object o);

	@Override
	int hashCode();


	@Override
	default V getOrDefault(Object key, V defaultValue) { return getMap().getOrDefault(key, defaultValue); }

	@Override
	default void forEach(BiConsumer<? super K, ? super V> action) { getMap().forEach(action); }

	@Override
	default void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) { getMap().replaceAll(function); }

	@Override
	default V putIfAbsent(K key, V value) { return getMap().putIfAbsent(key, value); }

	@Override
	default boolean remove(Object key, Object value) { return getMap().remove(key, value); }

	@Override
	default boolean replace(K key, V oldValue, V newValue) { return getMap().replace(key, oldValue, newValue); }

	@Override
	default V replace(K key, V value) { return getMap().replace(key, value); }

	@Override
	default V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) { return getMap().computeIfAbsent(key, mappingFunction); }

	@Override
	@Nullable
	default V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) { return getMap().computeIfPresent(key, remappingFunction); }

	@Override
	default V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) { return getMap().compute(key, remappingFunction); }

	@Override
	default V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) { return getMap().merge(key, value, remappingFunction); }
}
