package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.extensions.cursors;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.io.def.ICursor;

import java.util.Optional;

@FunctionalInterface
public interface ICursorHandleProvider {
	Optional<? extends ICursor> getCursorHandle();
}
