package $group__.utilities.collections;

import $group__.utilities.CastUtilities;

import java.util.Map;

public interface INestedMap<K, KN, V extends Map<KN, ?>>
		extends Map<K, V> {
	default boolean clean() {
		final boolean[] ret = {false};
		entrySet().removeIf(e ->
				e.getValue() != null
						&& (e.getValue().isEmpty()
						|| CastUtilities.castChecked(INestedMap.class, e)
						.filter(INestedMap::clean)
						.isPresent())
						&& (ret[0] = true));
		return ret[0];
	}
}
