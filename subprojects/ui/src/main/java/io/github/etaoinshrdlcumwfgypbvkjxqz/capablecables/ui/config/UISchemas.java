package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.config;

import org.jetbrains.annotations.PropertyKey;

import java.util.ResourceBundle;

public enum UISchemas {
	;

	public static final String RESOURCE_BUNDLE_PATH = "io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.config.UISchemas";
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(getResourceBundlePath());

	public static String getResourceBundlePath() {
		return RESOURCE_BUNDLE_PATH;
	}

	public static String getSchemaLocation(@PropertyKey(resourceBundle = RESOURCE_BUNDLE_PATH) CharSequence namespaceURI) {
		return getResourceBundle().getString(namespaceURI.toString());
	}

	private static ResourceBundle getResourceBundle() {
		return RESOURCE_BUNDLE;
	}
}
