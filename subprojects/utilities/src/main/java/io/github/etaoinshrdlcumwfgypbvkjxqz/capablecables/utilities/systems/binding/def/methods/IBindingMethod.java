package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.methods;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.IBinding;

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
