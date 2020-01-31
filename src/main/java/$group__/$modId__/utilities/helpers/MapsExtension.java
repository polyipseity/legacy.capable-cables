package $group__.$modId__.utilities.helpers;

import com.google.common.collect.MapMaker;

import java.util.Map;
import java.util.function.Function;

import static $group__.$modId__.utilities.variables.Constants.MULTI_THREAD_THREAD_COUNT;
import static $group__.$modId__.utilities.variables.Constants.SINGLE_THREAD_THREAD_COUNT;

public enum MapsExtension {
	/* MARK empty */;


	/* SECTION static variables */

	public static final MapMaker
			SINGLE_THREAD_MAP_MAKER = new MapMaker().concurrencyLevel(SINGLE_THREAD_THREAD_COUNT),
			MULTI_THREAD_MAP_MAKER = new MapMaker().concurrencyLevel(MULTI_THREAD_THREAD_COUNT),
			SINGLE_THREAD_WEAK_KEY_MAP_MAKER = new MapMaker().concurrencyLevel(SINGLE_THREAD_THREAD_COUNT).weakKeys(),
			MULTI_THREAD_WEAK_KEY_MAP_MAKER = new MapMaker().concurrencyLevel(MULTI_THREAD_THREAD_COUNT).weakKeys();


	/* SECTION static methods */

	public static <T extends Map<K, V>, K, V> T toMapFromValues(T map, Iterable<? extends V> values, Function<? super V, ? extends K> mapper) {
		values.forEach(t -> map.put(mapper.apply(t), t));
		return map;
	}
}
