package $group__.utilities.specific;

import com.google.common.collect.Iterables;
import com.google.common.collect.MapMaker;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static $group__.utilities.ConcurrencyUtilities.MULTI_THREAD_THREAD_COUNT;
import static $group__.utilities.ConcurrencyUtilities.SINGLE_THREAD_THREAD_COUNT;

public enum MapUtilities {
	;

	public static final long CACHE_EXPIRATION_ACCESS_DURATION = 15;
	public static final TimeUnit CACHE_EXPIRATION_ACCESS_TIME_UNIT = TimeUnit.MINUTES;

	public static MapMaker getMapMakerSingleThreaded() {
		return new MapMaker().concurrencyLevel(SINGLE_THREAD_THREAD_COUNT);
	}

	public static MapMaker getMapMakerMultiThreaded() {
		return new MapMaker().concurrencyLevel(MULTI_THREAD_THREAD_COUNT);
	}

	public static MapMaker getMapMakerMultiThreadedWithWeakKeys() {
		return new MapMaker().concurrencyLevel(SINGLE_THREAD_THREAD_COUNT).weakKeys();
	}

	public static MapMaker getMapMakerSingleThreadedWithWeakKeys() {
		return new MapMaker().concurrencyLevel(MULTI_THREAD_THREAD_COUNT).weakKeys();
	}

	@SafeVarargs
	public static <K, V> Map<K, V> concatMaps(Map<? extends K, ? extends V>... maps) {
		List<Iterable<? extends K>> keys = new ArrayList<>(maps.length);
		List<Iterable<? extends V>> values = new ArrayList<>(maps.length);
		int size = 0;
		for (Map<? extends K, ? extends V> map : maps) {
			Collection<? extends K> mk = map.keySet();
			size += mk.size();
			keys.add(mk);
			values.add(map.values());
		}
		return stitchIterables(size, Iterables.concat(keys), Iterables.concat(values));
	}

	public static <K, V> Map<K, V> stitchIterables(int size, Iterable<? extends K> keys, Iterable<? extends V> values) {
		Map<K, V> ret = new HashMap<>(size);

		Iterator<? extends V> iteratorValues = values.iterator();
		keys.forEach(k -> {
			if (!iteratorValues.hasNext())
				throw ThrowableUtilities.BecauseOf.illegalArgument("Keys too long", "keys", keys, "values", values);
			ret.put(k, iteratorValues.next());
		});
		if (iteratorValues.hasNext())
			throw ThrowableUtilities.BecauseOf.illegalArgument("Values too long", "keys", keys, "values", values);

		return ret;
	}
}
