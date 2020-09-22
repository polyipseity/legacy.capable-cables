package $group__.ui.mvvm.views.components.extensions.caches;

import $group__.ui.UIConfiguration;
import $group__.ui.core.mvvm.views.components.extensions.caches.IUICacheType;
import $group__.utilities.collections.CacheLoaderLoadedNullException;
import $group__.utilities.extensions.core.IExtensionContainer;
import $group__.utilities.structures.INamespacePrefixedString;
import $group__.utilities.throwable.ThrowableUtilities;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

public abstract class AbstractUICacheType<V, C extends IExtensionContainer<INamespacePrefixedString>>
		implements IUICacheType<V, C> {
	private final INamespacePrefixedString key;

	public AbstractUICacheType(INamespacePrefixedString key) { this.key = key; }

	@Override
	public void invalidate(C container) { StaticHolder.invalidateImpl(container, getKey()); }

	@Override
	public INamespacePrefixedString getKey() { return key; }

	@SuppressWarnings("unchecked")
	@Override
	public Optional<? extends V> get(C container) {
		return UICacheExtension.TYPE.getValue().find(container)
				.map(cache -> {
					try {
						return (V) cache.getDelegated()
								.get(getKey(), () -> load(container));
					} catch (ExecutionException e) {
						if (e.getCause() instanceof CacheLoaderLoadedNullException) {
							UIConfiguration.getInstance().getThrowableHandler().catch_(e);
							return null;
						}
						throw ThrowableUtilities.propagate(e);
					}
				});
	}

	protected abstract V load(C container)
			throws Exception;
}
