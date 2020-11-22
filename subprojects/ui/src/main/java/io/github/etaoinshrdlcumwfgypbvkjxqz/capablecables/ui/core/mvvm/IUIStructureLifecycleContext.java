package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.reactivex.rxjava3.observers.DisposableObserver;

import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IUIStructureLifecycleContext {
	Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> getBinderObserverSupplier();
}
