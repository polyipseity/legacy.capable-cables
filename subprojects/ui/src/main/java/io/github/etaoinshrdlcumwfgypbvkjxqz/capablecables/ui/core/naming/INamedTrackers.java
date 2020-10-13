package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.naming;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;

import java.util.Map;
import java.util.Optional;

public interface INamedTrackers {
	<E extends INamed> boolean add(Class<E> clazz, E element)
			throws DuplicateNameException;

	<E extends INamed> boolean remove(Class<E> clazz, E element);

	<E extends INamed> boolean addAll(Class<E> clazz, Iterable<? extends E> elements)
			throws DuplicateNameException;

	@SuppressWarnings("UnusedReturnValue")
	<E extends INamed> boolean removeAll(Class<E> clazz, Iterable<? extends E> elements);

	<E extends INamed> Optional<? extends E> get(Class<E> clazz, CharSequence element);

	<E extends INamed> INamedTracker<E> getTracker(Class<E> clazz);

	long size();

	boolean isEmpty();

	@Immutable Map<Class<? extends INamed>, INamedTracker<?>> asMapView();
}
