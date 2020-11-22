package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.IUIStructureLifecycleContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinderAction;
import io.reactivex.rxjava3.observers.DisposableObserver;

import java.util.Optional;
import java.util.function.Supplier;

public class UIImmutableStructureLifecycleContext
		implements IUIStructureLifecycleContext {
	private final Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier;

	private UIImmutableStructureLifecycleContext(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		this.binderObserverSupplier = binderObserverSupplier;
	}

	public static UIImmutableStructureLifecycleContext of(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier) {
		return new UIImmutableStructureLifecycleContext(binderObserverSupplier);
	}

	@Override
	public Supplier<@Nonnull ? extends Optional<? extends DisposableObserver<IBinderAction>>> getBinderObserverSupplier() {
		return binderObserverSupplier;
	}
}
