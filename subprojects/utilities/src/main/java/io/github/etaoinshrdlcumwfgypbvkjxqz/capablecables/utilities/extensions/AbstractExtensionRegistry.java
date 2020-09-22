package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.Registry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;
import org.slf4j.Logger;

import java.util.ResourceBundle;

public abstract class AbstractExtensionRegistry<K, V extends IExtensionType<? extends K, ?, ?>>
		extends Registry<K, V> {
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UtilitiesConfiguration.getInstance());

	protected AbstractExtensionRegistry(@SuppressWarnings("SameParameterValue") boolean overridable, Logger logger) { super(overridable, logger); }

	public void checkExtensionRegistered(IExtension<?, ?> extension) {
		if (!containsValue(extension.getType()))
			throw new IllegalArgumentException(
					new LogMessageBuilder()
							.addMarkers(this::getMarker)
							.addKeyValue("extension", extension)
							.addMessages(() -> getResourceBundle().getString("check.registered.fail"))
							.build()
			);
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }
}
