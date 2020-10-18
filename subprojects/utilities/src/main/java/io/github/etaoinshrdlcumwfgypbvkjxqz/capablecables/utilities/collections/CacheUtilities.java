package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections;

import com.google.common.base.Supplier;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ConcurrencyUtilities;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public enum CacheUtilities {
	;

	public static final long CACHE_EXPIRATION_ACCESS_DURATION = 15;
	private static final TimeUnit CACHE_EXPIRATION_ACCESS_TIME_UNIT = TimeUnit.MINUTES;

	public static CacheBuilder<Object, Object> newCacheBuilderSingleThreaded() { return CacheBuilder.newBuilder().concurrencyLevel(ConcurrencyUtilities.getSingleThreadThreadCount()); }

	public static CacheBuilder<Object, Object> newCacheBuilderNormalThreaded() { return CacheBuilder.newBuilder().concurrencyLevel(ConcurrencyUtilities.getNormalThreadThreadCount()); }

	public static <K, V> CacheLoader<Object, Cache<K, V>> newCacheBuilderSingleThreadedLoader(int initialCapacity) {
		return CacheLoader.from((Supplier<Cache<K, V>>) newCacheBuilderSingleThreaded().initialCapacity(initialCapacity)::build);
	}

	public static <K, V> Optional<V> remove(Cache<? super K, ? extends V> instance, K key) {
		@Nullable V ret = instance.getIfPresent(key);
		instance.invalidate(key);
		return Optional.ofNullable(ret);
	}

	public static <K, V> Optional<V> replace(Cache<? super K, V> instance, K key, V value) {
		@Nullable V ret = instance.getIfPresent(key);
		instance.put(key, value);
		return Optional.ofNullable(ret);
	}

	public static long getCacheExpirationAccessDuration() {
		return CACHE_EXPIRATION_ACCESS_DURATION;
	}

	public static TimeUnit getCacheExpirationAccessTimeUnit() {
		return CACHE_EXPIRATION_ACCESS_TIME_UNIT;
	}
}
