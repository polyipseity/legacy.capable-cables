package $group__.utilities.collections;

import java.util.Map;

public class WrappingNestedMap<K, KN, V extends Map<KN, ?>, M extends Map<K, V>>
		extends DelegatingMap<K, V, M>
		implements INestedMap<K, KN, V> {
	public WrappingNestedMap(M delegated) { super(delegated); }
}
