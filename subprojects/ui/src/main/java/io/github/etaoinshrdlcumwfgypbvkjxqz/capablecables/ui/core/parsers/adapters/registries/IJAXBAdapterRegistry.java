package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.registries;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IJAXBAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ImmutableJAXBAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;

import java.util.Optional;
import java.util.ResourceBundle;

public interface IJAXBAdapterRegistry {
	static Object adaptFromJAXB(IJAXBAdapterRegistry instance, Object jaxbObject)
			throws IllegalArgumentException {
		return IJAXBAdapter.leftToRight(getFromJAXBAdapter(instance, jaxbObject),
				ImmutableJAXBAdapterContext.of(instance), jaxbObject);
	}

	static <L> IJAXBAdapter<L, ?> getFromJAXBAdapter(IJAXBAdapterRegistry instance, L jaxbObject)
			throws IllegalArgumentException {
		return instance.findFromJAXBAdapter(jaxbObject)
				.orElseThrow(() ->
						new IllegalArgumentException(
								new LogMessageBuilder()
										.addMarkers(UIMarkers.getInstance()::getMarkerJAXB)
										.addKeyValue("instance", instance).addKeyValue("jaxbObject", jaxbObject)
										.addMessages(() -> StaticHolder.getResourceBundle().getString("adapter.from.find.not_found"))
										.build()
						));
	}

	<L> Optional<? extends IJAXBAdapter<L, ?>> findFromJAXBAdapter(L jaxbObject);

	static Object adaptToJAXB(IJAXBAdapterRegistry instance, Object object)
			throws IllegalArgumentException {
		return IJAXBAdapter.rightToLeft(getToJAXBAdapter(instance, object),
				ImmutableJAXBAdapterContext.of(instance), object);
	}

	static <R> IJAXBAdapter<?, R> getToJAXBAdapter(IJAXBAdapterRegistry instance, R object)
			throws IllegalArgumentException, IllegalStateException {
		return instance.findToJAXBAdapter(object)
				.orElseThrow(() ->
						new IllegalArgumentException(
								new LogMessageBuilder()
										.addMarkers(UIMarkers.getInstance()::getMarkerJAXB)
										.addKeyValue("instance", instance).addKeyValue("object", object)
										.addMessages(() -> StaticHolder.getResourceBundle().getString("adapter.to.find.not_found"))
										.build()
						));
	}

	<R> Optional<? extends IJAXBAdapter<?, R>> findToJAXBAdapter(R object);

	IJAXBElementAdapterRegistry getElementRegistry();

	IJAXBObjectAdapterRegistry getObjectRegistry();

	enum StaticHolder {
		;

		private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());

		public static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }
	}
}
