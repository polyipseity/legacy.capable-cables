package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.methods;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinding;

public interface IBindingMethod<T>
		extends IBinding<T> {
	@Override
	default EnumBindingType getBindingType() { return EnumBindingType.METHOD; }

	EnumMethodType getMethodType();

	enum EnumMethodType {
		SOURCE,
		DESTINATION
	}
}
