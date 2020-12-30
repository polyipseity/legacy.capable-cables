package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.types;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.def.ICompatibilitySupplier;

public interface ILocalizedString<T extends CharSequence>
		extends ICompatibilitySupplier<T> {
	String getKey();
}
