package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.inputs;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;

import java.util.Optional;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IInputDevices {
	@Immutable
	Optional<? extends IInputPointerDevice> getPointerDevice();
}
