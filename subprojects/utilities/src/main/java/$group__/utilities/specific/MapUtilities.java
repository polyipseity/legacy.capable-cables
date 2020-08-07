package $group__.utilities.specific;

import com.google.common.collect.MapMaker;
import com.google.common.collect.Maps;

import java.util.Iterator;
import java.util.Map;
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

	public static <K, V> Map<K, V> stitchIterables(int size, Iterable<? extends K> keys, Iterable<? extends V> values) {
		Map<K, V> ret = Maps.newHashMapWithExpectedSize(size);

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
