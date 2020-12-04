package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.DefaultValueHolder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderObserverSupplierHolder;
import io.reactivex.rxjava3.observers.DisposableObserver;

import java.util.Optional;
import java.util.function.Supplier;

public class DefaultBinderObserverSupplierHolder
		extends DefaultValueHolder<Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>>>
		implements IBinderObserverSupplierHolder {
	public DefaultBinderObserverSupplierHolder() {
		super(null);
	}
}
