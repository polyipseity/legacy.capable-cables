package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.naming;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

public interface INamedTracker<E extends INamed> {
	boolean add(Iterator<? extends E> elements)
			throws DuplicateNameException;

	boolean remove(Iterator<? extends E> elements);

	Optional<? extends E> get(CharSequence name);

	@Immutable Map<String, E> asMapView();

	long size();

	boolean isEmpty();
}
