package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.naming;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;

import java.util.Map;
import java.util.Optional;

public interface INamedTracker<E extends INamed> {
	boolean add(E element)
			throws DuplicateNameException;

	boolean remove(E element);

	boolean addAll(Iterable<? extends E> elements)
			throws DuplicateNameException;

	boolean removeAll(Iterable<? extends E> elements);

	Optional<? extends E> get(CharSequence name);

	@Immutable Map<String, E> asMapView();

	long size();

	boolean isEmpty();
}
