package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IValueHolder;
import io.reactivex.rxjava3.observers.DisposableObserver;

import java.util.Optional;
import java.util.function.Supplier;

public interface IBinderObserverSupplierHolder
		extends IValueHolder<Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>>> {}
