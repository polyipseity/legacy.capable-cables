package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.impl;

import com.google.common.collect.MapMaker;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.AbstractRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;
import org.slf4j.Logger;

import java.util.ResourceBundle;
import java.util.function.Consumer;

public abstract class AbstractExtensionRegistry<K, V extends IExtensionType<? extends K, ?, ?>>
		extends AbstractRegistry<K, V> {
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UtilitiesConfiguration.getInstance());
	private static final long serialVersionUID = -7272349906937393891L;

	protected AbstractExtensionRegistry(@SuppressWarnings("SameParameterValue") boolean overridable, Logger logger, Consumer<? super MapMaker> configuration) {
		super(overridable, logger, configuration);
	}

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
