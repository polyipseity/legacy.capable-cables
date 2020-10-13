package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.naming;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@FunctionalInterface
public interface INamed {
	Optional<? extends String> getName();

	@SuppressWarnings("UnstableApiUsage")
	static <T extends INamed> @Immutable Map<String, T> toNamedMap(Iterable<? extends T> namedIterable)
			throws DuplicateNameException {
		return Streams.stream(namedIterable).unordered()
				.filter(named -> named.getName().isPresent())
				.collect(ImmutableMap.toImmutableMap(
						named -> {
							Optional<? extends String> name = named.getName();
							assert name.isPresent();
							return name.get();
						},
						Function.identity(),
						(named1, named2) -> { throw new DuplicateNameException(); }
				));
	}
}
