package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.methods;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;

import java.util.function.Consumer;

public interface IBindingMethodDestination<T>
		extends IBindingMethod<T>, Consumer<@Nonnull T> {
	@Override
	default EnumMethodType getMethodType() { return EnumMethodType.DESTINATION; }

	@Override
	void accept(@Nonnull T argument);
}
