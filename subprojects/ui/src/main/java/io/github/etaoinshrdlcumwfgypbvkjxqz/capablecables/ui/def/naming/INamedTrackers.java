package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.naming;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.IUIViewCoordinator;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

public interface INamedTrackers
		extends IUIViewCoordinator {
	<E extends INamed> boolean add(Class<E> clazz, Iterator<? extends E> elements)
			throws DuplicateNameException;

	@SuppressWarnings("UnusedReturnValue")
	<E extends INamed> boolean remove(Class<E> clazz, Iterator<? extends E> elements);

	<E extends INamed> Optional<? extends E> get(Class<E> clazz, CharSequence element);

	<E extends INamed> INamedTracker<E> getTracker(Class<E> clazz);

	long size();

	boolean isEmpty();

	@Immutable Map<Class<? extends INamed>, INamedTracker<?>> asMapView();
}
