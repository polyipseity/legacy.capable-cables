package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.extensions.cursors;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core.ICursor;

import java.util.Optional;

@FunctionalInterface
public interface ICursorHandleProvider {
	Optional<? extends ICursor> getCursorHandle();
}
