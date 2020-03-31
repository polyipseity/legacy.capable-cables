package $group__.$modId__.utilities.helpers.specific;

import com.google.common.collect.MapMaker;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static $group__.$modId__.utilities.helpers.Capacities.INITIAL_CAPACITY_3;
import static $group__.$modId__.utilities.helpers.Concurrency.MULTI_THREAD_THREAD_COUNT;
import static $group__.$modId__.utilities.helpers.Concurrency.SINGLE_THREAD_THREAD_COUNT;

public enum MapsExtension {
	/* MARK empty */;


	/* SECTION static variables */

	public static final MapMaker
			MAP_MAKER_SINGLE_THREAD = new MapMaker().initialCapacity(INITIAL_CAPACITY_3).concurrencyLevel(SINGLE_THREAD_THREAD_COUNT),
			MAP_MAKER_MULTI_THREAD = new MapMaker().initialCapacity(INITIAL_CAPACITY_3).concurrencyLevel(MULTI_THREAD_THREAD_COUNT),
			MAP_MAKER_SINGLE_THREAD_WEAK_KEYS = new MapMaker().initialCapacity(INITIAL_CAPACITY_3).concurrencyLevel(SINGLE_THREAD_THREAD_COUNT).weakKeys(),
			MAP_MAKER_MULTI_THREAD_WEAK_KEYS = new MapMaker().initialCapacity(INITIAL_CAPACITY_3).concurrencyLevel(MULTI_THREAD_THREAD_COUNT).weakKeys();

	public static final long CACHE_EXPIRATION_ACCESS_DURATION = 15;
	public static final TimeUnit CACHE_EXPIRATION_ACCESS_TIME_UNIT = TimeUnit.MINUTES;



	/* SECTION static methods */

	public static <T extends Map<K, V>, K, V> T toMapFromValues(T map, Iterable<? extends V> values, Function<? super V, ? extends K> mapper) {
		values.forEach(t -> map.put(mapper.apply(t), t));
		return map;
	}
}
