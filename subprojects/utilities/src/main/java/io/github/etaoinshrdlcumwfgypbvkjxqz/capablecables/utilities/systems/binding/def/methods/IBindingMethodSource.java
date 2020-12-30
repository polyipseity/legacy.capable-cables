package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.methods;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.INotifier;

public interface IBindingMethodSource<T>
		extends IBindingMethod<T>, INotifier<T> {
	@Override
	default EnumMethodType getMethodType() { return EnumMethodType.SOURCE; }

	void invoke(T argument);
}
