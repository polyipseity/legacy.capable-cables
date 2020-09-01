package $group__.utilities.collections;

import java.util.Collections;
import java.util.Set;

public enum CollectionUtilities {
	;

	@SuppressWarnings("unused")
	public static <T> Set<T> newConcurrentWeakSet() {
		return Collections.newSetFromMap(
				MapUtilities.getMapMakerMultiThreaded().makeMap());
	}

	public static <T> Set<T> newConcurrentWeakSet(int initialCapacity) {
		return Collections.newSetFromMap(
				MapUtilities.getMapMakerMultiThreaded().initialCapacity(initialCapacity).makeMap());
	}
}
