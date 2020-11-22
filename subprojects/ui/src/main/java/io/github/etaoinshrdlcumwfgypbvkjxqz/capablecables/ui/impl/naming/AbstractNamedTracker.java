package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.naming;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.naming.DuplicateNameException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.naming.INamed;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.naming.INamedTracker;
import org.apache.http.annotation.NotThreadSafe;

import java.util.Map;
import java.util.Optional;

@NotThreadSafe
public abstract class AbstractNamedTracker<E extends INamed>
		implements INamedTracker<E> {
	protected abstract Map<String, E> getData();

	@Override
	public boolean add(E element)
			throws DuplicateNameException {
		return element.getName()
				.map(name -> {
					@Nullable E previousElement = getData().put(name, element);
					if (!(previousElement == null || element.equals(previousElement)))
						throw new DuplicateNameException(name);
					return true;
				})
				.orElse(false);
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public boolean addAll(Iterable<? extends E> elements)
			throws DuplicateNameException {
		return Streams.stream(elements).unordered()
				.map(this::add)
				.reduce(false, Boolean::logicalOr);
	}

	@Override
	public boolean remove(E element) {
		return element.getName()
				.map(getData()::remove)
				.isPresent();
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public boolean removeAll(Iterable<? extends E> elements) {
		return Streams.stream(elements).unordered()
				.map(this::remove)
				.reduce(false, Boolean::logicalOr);
	}

	@Override
	public Optional<? extends E> get(CharSequence name) {
		return Optional.ofNullable(getData().get(name.toString()));
	}

	@Override
	public long size() { return getData().size(); }

	@Override
	public boolean isEmpty() { return size() == 0; }

	@Override
	public @Immutable Map<String, E> asMapView() { return ImmutableMap.copyOf(getData()); }
}
