package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.naming;

import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.naming.DuplicateNameException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.naming.INamed;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.naming.INamedTracker;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.naming.INamedTrackers;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Map;
import java.util.Optional;

@ThreadSafe
public abstract class AbstractNamedTrackers
		implements INamedTrackers {
	@Override
	public <E extends INamed> boolean add(Class<E> clazz, E element) throws DuplicateNameException {
		return getTracker(this, clazz).add(element);
	}

	@Override
	public <E extends INamed> boolean remove(Class<E> clazz, E element) {
		boolean result = getTracker(this, clazz).remove(element);
		getData().cleanUp();
		return result;
	}

	@Override
	public <E extends INamed> boolean addAll(Class<E> clazz, Iterable<? extends E> elements) throws DuplicateNameException {
		return getTracker(this, clazz).addAll(elements);
	}

	@Override
	public <E extends INamed> boolean removeAll(Class<E> clazz, Iterable<? extends E> elements) {
		boolean result = getTracker(this, clazz).removeAll(elements);
		getData().cleanUp();
		return result;
	}

	@Override
	public <E extends INamed> Optional<? extends E> get(Class<E> clazz, CharSequence element) {
		return getTracker(this, clazz).get(element);
	}

	@Override
	public long size() { return getData().size(); }

	@Override
	public boolean isEmpty() { return size() == 0; }

	@Override
	public @Immutable Map<Class<? extends INamed>, INamedTracker<?>> asMapView() { return ImmutableMap.copyOf(getData().asMap()); }

	@SuppressWarnings("unchecked")
	protected static <E extends INamed> INamedTracker<E> getTracker(AbstractNamedTrackers trackers, Class<E> clazz) {
		return (INamedTracker<E>) trackers.getData().getUnchecked(clazz);
	}

	protected abstract LoadingCache<Class<? extends INamed>, INamedTracker<?>> getData();
}
