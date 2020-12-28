package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.types;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.core.ICompatibilitySupplier;

public interface ILocalizedString<T extends CharSequence>
		extends ICompatibilitySupplier<T> {
	String getKey();
}
