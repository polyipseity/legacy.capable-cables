package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.methods;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.INotifier;

public interface IBindingMethodSource<T>
		extends IBindingMethod<T>, INotifier<T> {
	@Override
	default EnumMethodType getMethodType() { return EnumMethodType.SOURCE; }

	void invoke(T argument);
}
