package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.types;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.types.IResourceBundleLocalizedString;

import java.util.ResourceBundle;

public final class ImmutableResourceBundleLocalizedString
		extends AbstractLocalizedString<String>
		implements IResourceBundleLocalizedString<String> {
	private final ResourceBundle resourceBundle;

	private ImmutableResourceBundleLocalizedString(CharSequence key, ResourceBundle resourceBundle) {
		super(key);
		this.resourceBundle = resourceBundle;
	}

	public static ImmutableResourceBundleLocalizedString of(CharSequence key, ResourceBundle resourceBundle) {
		return new ImmutableResourceBundleLocalizedString(key, resourceBundle);
	}

	@Override
	public String getBaseName() {
		return getResourceBundle().getBaseBundleName();
	}

	protected ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	@Override
	public String get() {
		return getResourceBundle().getString(getKey());
	}
}
