package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.reactivex.rxjava3.observers.DisposableObserver;

import java.util.Optional;
import java.util.function.Supplier;

public class BinderObserverSupplierHolder {
	@Nullable
	private Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier;

	public Optional<? extends Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>>> getBinderObserverSupplier() {
		return Optional.ofNullable(binderObserverSupplier);
	}

	public void setBinderObserverSupplier(@Nullable Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		this.binderObserverSupplier = binderObserverSupplier;
	}
}
