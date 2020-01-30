package $group__.$modId__.utilities.helpers;

import java.util.Map;
import java.util.function.Function;

public enum MapsExtension {
	/* MARK empty */;


	/* SECTION static methods */

	public static <T extends Map<K, V>, K, V> T toMapFromValues(T map, Iterable<? extends V> values, Function<? super V, ? extends K> mapper) {
		values.forEach(t -> map.put(mapper.apply(t), t));
		return map;
	}
}
