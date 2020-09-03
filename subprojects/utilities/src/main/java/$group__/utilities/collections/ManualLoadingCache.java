package $group__.utilities.collections;

import $group__.utilities.AssertionUtilities;
import com.google.common.cache.Cache;
import com.google.common.cache.ForwardingLoadingCache;
import com.google.common.cache.LoadingCache;

import java.util.function.Consumer;

public class ManualLoadingCache<K, V>
		extends ForwardingLoadingCache.SimpleForwardingLoadingCache<K, V> {
	protected final Consumer<? super LoadingCache<K, V>> cleaner;

	public ManualLoadingCache(LoadingCache<K, V> delegate, Consumer<? super LoadingCache<K, V>> cleaner) {
		super(delegate);
		this.cleaner = cleaner;
	}

	public static <K, V extends Cache<?, ?>> LoadingCache<K, V> newNestedLoadingCache(LoadingCache<K, V> delegate) {
		return new ManualLoadingCache<>(delegate,
				t -> t.asMap().entrySet()
						.removeIf(e -> {
							V ev = AssertionUtilities.assertNonnull(e.getValue());
							ev.cleanUp();
							return ev.size() == 0;
						}));
	}

	@Override
	public void cleanUp() {
		super.cleanUp();
		getCleaner().accept(this);
	}

	protected Consumer<? super LoadingCache<K, V>> getCleaner() { return cleaner; }
}
