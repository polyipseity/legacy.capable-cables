package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.methods;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.methods.IBindingMethodDestination;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.IHasGenericClass;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;

public class BindingMethodDestination<T>
		extends IHasGenericClass.Impl<T>
		implements IBindingMethodDestination<T> {
	@Nullable
	protected final INamespacePrefixedString bindingKey;
	protected final Consumer<T> action;

	public BindingMethodDestination(Class<T> genericClass, @Nullable INamespacePrefixedString bindingKey, Consumer<T> action) {
		super(genericClass);
		this.bindingKey = bindingKey;
		this.action = action;
	}

	@Override
	public Optional<? extends INamespacePrefixedString> getBindingKey() { return Optional.ofNullable(bindingKey); }

	@Override
	public void accept(T argument) { action.accept(argument); }
}
