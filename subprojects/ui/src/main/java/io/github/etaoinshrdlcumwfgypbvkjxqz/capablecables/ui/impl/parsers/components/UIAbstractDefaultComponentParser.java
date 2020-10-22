package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.components;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.Using;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.UIParserCheckedException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.UIParserUncheckedException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.components.contexts.IUIAbstractDefaultComponentParserContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IThrowingFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;

import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentMap;

public abstract class UIAbstractDefaultComponentParser<T, R, P, C extends IUIAbstractDefaultComponentParserContext<?>>
		extends UIAbstractExtensibleJAXBParser<T, R, P, C> {
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());

	private final ConcurrentMap<String, Class<?>> aliases = MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(CapacityUtilities.getInitialCapacityMedium()).weakValues().makeMap();

	@SuppressWarnings("UnstableApiUsage")
	@Override
	protected final P parse0(R resource)
			throws Throwable {
		getAliases().putAll(
				Streams.stream(getRawAliases(resource)).unordered()
						.map(IThrowingFunction.executeNow(u -> {
							assert u != null;
							return Maps.immutableEntry(u.getAlias(), Class.forName(u.getTarget()));
						}))
						.collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue)));
		return parse1(resource);
	}

	@Override
	public void reset() {
		super.reset();
		getAliases().clear();
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<String, Class<?>> getAliases() { return aliases; }

	protected abstract Iterable<? extends Using> getRawAliases(R resource)
			throws UIParserCheckedException, UIParserUncheckedException;

	protected abstract P parse1(R resource)
			throws Throwable;

	@Override
	public final T construct()
			throws UIParserCheckedException, UIParserUncheckedException {
		try {
			return getParsed()
					.map(IThrowingFunction.executeNow(this::construct0))
					.orElseThrow(() ->
							new IllegalStateException(
									new LogMessageBuilder()
											.addMarkers(UIMarkers.getInstance()::getMarkerParser)
											.addMessages(() -> getResourceBundle().getString("construct.parsed.missing"))
											.build()
							)
					);
		} catch (Throwable throwable) {
			throw ThrowableUtilities.propagate(throwable, UIParserCheckedException::new, UIParserUncheckedException::new);
		}
	}

	protected abstract T construct0(P parsed)
			throws Throwable;

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }
}
