package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.extensions.caches;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.caches.IUICacheExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.caches.IUICacheType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.CacheLoaderLoadedNullException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

public abstract class UIAbstractCacheType<V, C extends IExtensionContainer<INamespacePrefixedString>>
		implements IUICacheType<V, C> {
	private final INamespacePrefixedString key;

	public UIAbstractCacheType(INamespacePrefixedString key) { this.key = key; }

	@Override
	public void invalidate(C container) { IUICacheType.invalidateImpl(container, getKey()); }

	@Override
	public INamespacePrefixedString getKey() { return key; }

	@SuppressWarnings("unchecked")
	@Override
	public Optional<? extends V> get(C container) {
		return IUICacheExtension.StaticHolder.getType().getValue().find(container)
				.map(cache -> {
					try {
						return (V) cache.getDelegate()
								.get(getKey(), () -> load(container));
					} catch (ExecutionException e) {
						if (e.getCause() instanceof CacheLoaderLoadedNullException)
							return null;
						throw ThrowableUtilities.propagate(e);
					}
				});
	}

	protected abstract V load(C container)
			throws Exception;
}
