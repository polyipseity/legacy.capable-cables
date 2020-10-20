package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.inputs.core;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;

import java.util.Optional;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IInputDevices {
	@Immutable
	Optional<? extends IInputPointerDevice> getPointerDevice();
}
