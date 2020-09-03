package $group__.utilities.collections;

import com.google.common.base.Supplier;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;

import javax.annotation.Nullable;
import java.util.Optional;

import static $group__.utilities.ConcurrencyUtilities.NORMAL_THREAD_THREAD_COUNT;
import static $group__.utilities.ConcurrencyUtilities.SINGLE_THREAD_THREAD_COUNT;

public enum CacheUtilities {
	;

	public static CacheBuilder<Object, Object> newCacheBuilderSingleThreaded() { return CacheBuilder.newBuilder().concurrencyLevel(SINGLE_THREAD_THREAD_COUNT); }

	public static CacheBuilder<Object, Object> newCacheBuilderNormalThreaded() { return CacheBuilder.newBuilder().concurrencyLevel(NORMAL_THREAD_THREAD_COUNT); }

	public static <K, V> CacheLoader<Object, Cache<K, V>> newCacheBuilderSingleThreadedLoader(int initialCapacity) {
		return CacheLoader.from((Supplier<Cache<K, V>>) newCacheBuilderSingleThreaded().initialCapacity(initialCapacity)::build);
	}

	public static <K, V> Optional<V> getAndInvalidate(Cache<? super K, ? extends V> instance, K key) {
		@Nullable V ret = instance.getIfPresent(key);
		instance.invalidate(key);
		return Optional.ofNullable(ret);
	}

	public static <K, V> Optional<V> getAndPut(Cache<? super K, V> instance, K key, V value) {
		@Nullable V ret = instance.getIfPresent(key);
		instance.put(key, value);
		return Optional.ofNullable(ret);
	}
}
