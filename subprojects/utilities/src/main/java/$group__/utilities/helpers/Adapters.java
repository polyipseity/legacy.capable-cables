package $group__.utilities.helpers;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

import static $group__.utilities.helpers.Capacities.INITIAL_CAPACITY_2;
import static $group__.utilities.helpers.Casts.castUnchecked;
import static $group__.utilities.helpers.specific.MapsExtension.MAP_MAKER_MULTI_THREAD;

public enum Adapters {
	;

	private static final ConcurrentMap<Class<?>, ConcurrentMap<Class<?>, List<Function<?, ?>>>> REGISTRY = MAP_MAKER_MULTI_THREAD.makeMap();

	public static <T, R> int register(Class<T> from, Class<R> to, Function<? super T, ? extends R> adapter) {
		List<Function<?, ?>> l = REGISTRY.computeIfAbsent(from, k -> MAP_MAKER_MULTI_THREAD.makeMap()).computeIfAbsent(to, k -> new ArrayList<>(INITIAL_CAPACITY_2));
		l.add(adapter);
		return l.size() - 1;
	}

	public static <T, R> Optional<Function<? super T, ? extends R>> get(Class<T> from, Class<R> to, int index) {
		@Nullable ConcurrentMap<Class<?>, List<Function<?, ?>>> r2 = REGISTRY.get(from);
		if (r2 != null) {
			@Nullable List<Function<?, ?>> r3 = r2.get(to);
			if (r3 != null) return castUnchecked(r3.get(index));
		}
		return Optional.empty();
	}
}
