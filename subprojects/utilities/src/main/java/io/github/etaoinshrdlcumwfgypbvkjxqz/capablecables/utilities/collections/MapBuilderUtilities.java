package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections;

import com.google.common.collect.MapMaker;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ConcurrencyUtilities;

public enum MapBuilderUtilities {
	;

	public static MapMaker newMapMakerSingleThreaded() { return new MapMaker().concurrencyLevel(ConcurrencyUtilities.SINGLE_THREAD_THREAD_COUNT); }

	public static MapMaker newMapMakerNormalThreaded() { return new MapMaker().concurrencyLevel(ConcurrencyUtilities.NORMAL_THREAD_THREAD_COUNT); }
}
