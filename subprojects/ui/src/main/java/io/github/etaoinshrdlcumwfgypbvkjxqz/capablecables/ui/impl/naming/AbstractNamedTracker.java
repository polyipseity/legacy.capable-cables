package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.naming;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.naming.DuplicateNameException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.naming.INamed;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.naming.INamedTracker;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.primitives.BooleanUtilities.PaddedBool;
import org.apache.http.annotation.NotThreadSafe;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.primitives.BooleanUtilities.PaddedBool.*;

@NotThreadSafe
public abstract class AbstractNamedTracker<E extends INamed>
		implements INamedTracker<E> {
	protected abstract Map<String, E> getData();

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public boolean add(Iterator<? extends E> elements)
			throws DuplicateNameException {
		return stripBool(
				Streams.stream(elements).unordered()
						.filter(element -> element.getName().isPresent())
						.peek(element -> {
							String name = element.getName().get();
							@Nullable E previousElement = getData().putIfAbsent(name, element); // COMMENT ensure not modified after the exception is thrown
							if (!(previousElement == null || element.equals(previousElement)))
								throw new DuplicateNameException(name);
						})
						.mapToInt(element -> tBool())
						.reduce(fBool(), PaddedBool::orBool)
		);
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public boolean remove(Iterator<? extends E> elements) {
		Map<String, E> data = getData();
		return stripBool(
				Streams.stream(elements).unordered()
						.filter(element -> element.getName().isPresent())
						.mapToInt(element -> padBool(data.remove(element.getName().get(), element)))
						.reduce(fBool(), PaddedBool::orBool)
		);
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
