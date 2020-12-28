package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.extensions.caches;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.caches.IUICacheExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.caches.IUICacheType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.CacheLoaderLoadedNullException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

public abstract class UIAbstractCacheType<V0, V, C extends IExtensionContainer<IIdentifier>>
		implements IUICacheType<V, C> {
	private final IIdentifier key;

	public UIAbstractCacheType(IIdentifier key) { this.key = key; }

	@Override
	public void invalidate(C container) { IUICacheType.invalidateImpl(container, getKey()); }

	@Override
	public IIdentifier getKey() { return key; }

	@SuppressWarnings("unchecked")
	@Override
	public final Optional<? extends V> get(C container) {
		return IUICacheExtension.StaticHolder.getType().getValue().find(container)
				.flatMap(cache -> {
					while (true) {
						@Nullable V0 loaded;
						try {
							loaded = (V0) cache.getDelegate()
									.get(getKey(), () -> load(container));
						} catch (ExecutionException e) {
							if (e.getCause() instanceof CacheLoaderLoadedNullException)
								loaded = null;
							else
								throw ThrowableUtilities.propagate(e);
						}
						try {
							return transform(container, loaded);
						} catch (ReloadException e) {
							cache.getDelegate().invalidate(getKey());
							// COMMENT reload
						}
					}
				});
	}

	protected abstract V0 load(C container)
			throws Exception;

	protected abstract Optional<? extends V> transform(C container, @Nullable V0 value)
			throws ReloadException;

	public static class ReloadException
			extends Exception {
		private static final long serialVersionUID = -3573856739814562214L;
	}

	public static abstract class Identity<V, C extends IExtensionContainer<IIdentifier>>
			extends UIAbstractCacheType<V, V, C> {
		public Identity(IIdentifier key) {
			super(key);
		}

		@Override
		protected final Optional<? extends V> transform(C container, @Nullable V value) {
			return Optional.ofNullable(value);
		}
	}
}
