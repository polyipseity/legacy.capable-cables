package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.time.core.ITicker;

import java.util.Optional;

public interface IInputDevices {
	ITicker getTicker();

	@Immutable
	Optional<? extends IInputPointerDevice> getPointerDevice();
}
