package $group__.utilities.collections;

import $group__.utilities.interfaces.IDelegating;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class DelegatingMap<K, V, M extends Map<K, V>>
		extends IDelegating.Impl<M>
		implements Map<K, V> {
	public DelegatingMap(M delegated) { super(delegated); }

	@Override
	public int size() {return getDelegated().size();}

	@Override
	public boolean isEmpty() {return getDelegated().isEmpty();}

	@Override
	public boolean containsKey(Object key) {return getDelegated().containsKey(key);}

	@Override
	public boolean containsValue(Object value) {return getDelegated().containsValue(value);}

	@Nullable
	@Override
	public V get(Object key) {return getDelegated().get(key);}

	@Override
	public V put(K key, V value) {return getDelegated().put(key, value);}

	@Nullable
	@Override
	public V remove(Object key) {return getDelegated().remove(key);}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {getDelegated().putAll(m);}

	@Override
	public void clear() {getDelegated().clear();}

	@Override
	public Set<K> keySet() {return getDelegated().keySet();}

	@Override
	public Collection<V> values() {return getDelegated().values();}

	@Override
	public Set<Entry<K, V>> entrySet() {return getDelegated().entrySet();}

	@Override
	public V getOrDefault(Object key, V defaultValue) {return getDelegated().getOrDefault(key, defaultValue);}

	@Override
	public void forEach(BiConsumer<? super K, ? super V> action) {getDelegated().forEach(action);}

	@Override
	public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {getDelegated().replaceAll(function);}

	@Override
	public V putIfAbsent(K key, V value) {return getDelegated().putIfAbsent(key, value);}

	@Override
	public boolean remove(Object key, Object value) {return getDelegated().remove(key, value);}

	@Override
	public boolean replace(K key, V oldValue, V newValue) {return getDelegated().replace(key, oldValue, newValue);}

	@Override
	public V replace(K key, V value) {return getDelegated().replace(key, value);}

	@Override
	public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {return getDelegated().computeIfAbsent(key, mappingFunction);}

	@Nullable
	@Override
	public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {return getDelegated().computeIfPresent(key, remappingFunction);}

	@Override
	public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {return getDelegated().compute(key, remappingFunction);}

	@Override
	public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {return getDelegated().merge(key, value, remappingFunction);}

	@Override
	public int hashCode() {return getDelegated().hashCode();}

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(Object o) {return getDelegated().equals(o);}
}
