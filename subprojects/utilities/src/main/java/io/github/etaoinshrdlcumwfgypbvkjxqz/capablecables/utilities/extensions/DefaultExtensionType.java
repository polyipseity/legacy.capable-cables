package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionType;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class DefaultExtensionType<K, V extends IExtension<K, C>, C extends IExtensionContainer<? super K>>
		implements IExtensionType<K, V, C> {
	protected final K key;
	protected final BiFunction<? super IExtensionType<K, V, C>, ? super C, ? extends Optional<? extends V>> getter;

	public DefaultExtensionType(K key, BiFunction<? super IExtensionType<K, V, C>, ? super C, ? extends Optional<? extends V>> getter) {
		this.key = key;
		this.getter = getter;
	}

	@Override
	public Optional<? extends V> find(C container) { return getGetter().apply(this, container).map(Function.identity()); }

	@Override
	public K getKey() { return key; }

	protected BiFunction<? super IExtensionType<K, V, C>, ? super C, ? extends Optional<? extends V>> getGetter() { return getter; }
}
