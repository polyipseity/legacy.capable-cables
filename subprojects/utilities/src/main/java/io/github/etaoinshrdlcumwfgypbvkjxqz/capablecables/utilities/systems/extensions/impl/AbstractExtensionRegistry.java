package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.impl.AbstractRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;

import java.util.ResourceBundle;

public abstract class AbstractExtensionRegistry<K, V extends IExtensionType<? extends K, ?, ?>>
		extends AbstractRegistry<K, V> {
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UtilitiesConfiguration.getInstance());
	private static final long serialVersionUID = -7272349906937393891L;

	public AbstractExtensionRegistry(@SuppressWarnings("SameParameterValue") boolean overridable) {
		super(overridable);
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
