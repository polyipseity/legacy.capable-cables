package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.types;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.types.ILocalizedString;

public abstract class AbstractLocalizedString<T extends CharSequence>
		implements ILocalizedString<T> {
	private final String key;

	public AbstractLocalizedString(CharSequence key) {
		this.key = key.toString();
	}

	@Override
	public String getKey() {
		return key;
	}
}
