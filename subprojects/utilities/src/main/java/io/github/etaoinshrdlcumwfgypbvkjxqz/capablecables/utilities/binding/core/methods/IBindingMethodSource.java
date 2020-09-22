package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.methods;

import io.reactivex.rxjava3.core.ObservableSource;

public interface IBindingMethodSource<T>
		extends IBindingMethod<T> {
	@Override
	default EnumMethodType getMethodType() { return EnumMethodType.SOURCE; }

	ObservableSource<T> getNotifier();

	void invoke(T argument);
}
