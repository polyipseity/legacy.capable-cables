package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.def.IExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.def.IExtensionContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.def.IExtensionType;

import java.util.Optional;
import java.util.function.BiFunction;

public final class ImmutableExtensionType<K, V extends IExtension<K, C>, C extends IExtensionContainer<? super K>>
		implements IExtensionType<K, V, C> {
	private final K key;
	private final BiFunction<@Nonnull ? super IExtensionType<K, V, C>, @Nonnull ? super C, @Nonnull ? extends Optional<? extends V>> getter;

	public ImmutableExtensionType(K key, BiFunction<@Nonnull ? super IExtensionType<K, V, C>, @Nonnull ? super C, @Nonnull ? extends Optional<? extends V>> getter) {
		this.key = key;
		this.getter = getter;
	}

	@Override
	public Optional<? extends V> find(C container) {
		return getGetter().apply(this, container);
	}

	@Override
	public K getKey() { return key; }

	protected BiFunction<@Nonnull ? super IExtensionType<K, V, C>, @Nonnull ? super C, @Nonnull ? extends Optional<? extends V>> getGetter() { return getter; }
}
