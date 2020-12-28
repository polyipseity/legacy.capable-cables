package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates;

import java.util.ResourceBundle;

public abstract class SharedResourceBundleTemplate {
	private final ResourceBundle resourceBundle;

	protected SharedResourceBundleTemplate(ResourceBundle resourceBundle) {
		this.resourceBundle = resourceBundle;
	}

	public String getString(IResourceBundleKey key) { return getResourceBundle().getString(key.getKey()); }

	public ResourceBundle getResourceBundle() { return resourceBundle; }

	@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
	public interface IResourceBundleKey {
		String getKey();
	}
}
