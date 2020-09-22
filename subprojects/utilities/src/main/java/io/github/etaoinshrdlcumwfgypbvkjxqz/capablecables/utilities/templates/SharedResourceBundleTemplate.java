package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.Singleton;
import org.slf4j.Logger;

import java.util.ResourceBundle;

public abstract class SharedResourceBundleTemplate
		extends Singleton {
	private final ResourceBundle resourceBundle;

	protected SharedResourceBundleTemplate(ResourceBundle resourceBundle, Logger logger) {
		super(logger);
		this.resourceBundle = resourceBundle;
	}

	public String getString(IResourceBundleKey key) { return getResourceBundle().getString(key.getKey()); }

	public ResourceBundle getResourceBundle() { return resourceBundle; }

	@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
	public interface IResourceBundleKey {
		String getKey();
	}
}
