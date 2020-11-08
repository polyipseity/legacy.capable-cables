package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui.components;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.Using;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IThrowingFunction;

import java.util.Map;

public enum JAXBUIComponentUtilities {
	;

	@SuppressWarnings("UnstableApiUsage")
	public static @Immutable Map<String, Class<?>> adaptUsingFromJAXB(Iterable<? extends Using> using)
			throws ClassNotFoundException {
		return Streams.stream(using).unordered()
				.map(IThrowingFunction.executeNow(u -> {
					assert u != null;
					return Maps.immutableEntry(u.getAlias(), Class.forName(u.getTarget()));
				}))
				.collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));
	}
}
