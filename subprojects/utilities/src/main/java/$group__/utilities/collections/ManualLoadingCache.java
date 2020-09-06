package $group__.utilities.collections;

import $group__.utilities.AssertionUtilities;
import com.google.common.cache.Cache;
import com.google.common.cache.ForwardingLoadingCache;
import com.google.common.cache.LoadingCache;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ManualLoadingCache<K, V>
		extends ForwardingLoadingCache.SimpleForwardingLoadingCache<K, V> {
	protected final Consumer<? super LoadingCache<K, V>> cleaner;

	public ManualLoadingCache(LoadingCache<K, V> delegate, Consumer<? super LoadingCache<K, V>> cleaner) {
		super(delegate);
		this.cleaner = cleaner;
	}

	public static <K, V extends Cache<?, ?>> LoadingCache<K, V> newNestedLoadingCacheCache(LoadingCache<K, V> delegate) {
		return newNestedLoadingCache(delegate, e -> {
			V ev = AssertionUtilities.assertNonnull(e.getValue());
			ev.cleanUp();
			return ev.asMap().isEmpty();
		});
	}

	public static <K, V> LoadingCache<K, V> newNestedLoadingCache(LoadingCache<K, V> delegate, Predicate<? super Map.Entry<K, V>> filter) {
		return new ManualLoadingCache<>(delegate,
				t -> t.asMap().entrySet()
						.removeIf(filter));
	}

	public static <K, V extends Map<?, ?>> LoadingCache<K, V> newNestedLoadingCacheMap(LoadingCache<K, V> delegate) {
		return newNestedLoadingCache(delegate, e ->
				AssertionUtilities.assertNonnull(e.getValue()).isEmpty());
	}

	public static <K, V extends Collection<?>> LoadingCache<K, V> newNestedLoadingCacheCollection(LoadingCache<K, V> delegate) {
		return newNestedLoadingCache(delegate, e ->
				AssertionUtilities.assertNonnull(e.getValue()).isEmpty());
	}

	@Override
	public void cleanUp() {
		super.cleanUp();
		getCleaner().accept(this);
	}

	protected Consumer<? super LoadingCache<K, V>> getCleaner() { return cleaner; }
}
