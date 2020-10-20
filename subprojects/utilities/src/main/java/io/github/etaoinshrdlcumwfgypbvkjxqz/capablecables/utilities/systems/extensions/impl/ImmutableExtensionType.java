package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionType;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class ImmutableExtensionType<K, V extends IExtension<K, C>, C extends IExtensionContainer<? super K>>
		implements IExtensionType<K, V, C> {
	private final K key;
	private final BiFunction<? super IExtensionType<K, V, C>, ? super C, ? extends Optional<? extends V>> getter;

	public ImmutableExtensionType(K key, BiFunction<? super IExtensionType<K, V, C>, ? super C, ? extends Optional<? extends V>> getter) {
		this.key = key;
		this.getter = getter;
	}

	@Override
	public Optional<? extends V> find(C container) { return getGetter().apply(this, container).map(Function.identity()); }

	@Override
	public K getKey() { return key; }

	protected BiFunction<? super IExtensionType<K, V, C>, ? super C, ? extends Optional<? extends V>> getGetter() { return getter; }
}
