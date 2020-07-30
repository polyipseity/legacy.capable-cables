package $group__.utilities.specific;

import com.google.common.collect.MapMaker;

import java.util.concurrent.TimeUnit;

import static $group__.utilities.ConcurrencyUtilities.MULTI_THREAD_THREAD_COUNT;
import static $group__.utilities.ConcurrencyUtilities.SINGLE_THREAD_THREAD_COUNT;

public enum MapUtilities {
	;

	public static final long CACHE_EXPIRATION_ACCESS_DURATION = 15;
	public static final TimeUnit CACHE_EXPIRATION_ACCESS_TIME_UNIT = TimeUnit.MINUTES;

	public static MapMaker getMapMakerSingleThread() {
		return new MapMaker().concurrencyLevel(SINGLE_THREAD_THREAD_COUNT);
	}

	public static MapMaker getMapMakerMultiThread() {
		return new MapMaker().concurrencyLevel(MULTI_THREAD_THREAD_COUNT);
	}

	public static MapMaker getMapMakerMultiThreadWeakKeys() {
		return new MapMaker().concurrencyLevel(SINGLE_THREAD_THREAD_COUNT).weakKeys();
	}

	public static MapMaker getMapMakerSingleThreadWeakKeys() {
		return new MapMaker().concurrencyLevel(MULTI_THREAD_THREAD_COUNT).weakKeys();
	}
}
