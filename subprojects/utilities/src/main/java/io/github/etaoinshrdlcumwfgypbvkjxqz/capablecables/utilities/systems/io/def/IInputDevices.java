package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.io.def;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.time.def.ITicker;

import java.util.Optional;

public interface IInputDevices {
	ITicker getTicker(); // COMMENT should always be available, 'System#nanoTime'

	Optional<@Immutable ? extends IPointerDevice> getPointerDevice();

	Optional<@Immutable ? extends IKeyboardDevice> getKeyboardDevice();
}
