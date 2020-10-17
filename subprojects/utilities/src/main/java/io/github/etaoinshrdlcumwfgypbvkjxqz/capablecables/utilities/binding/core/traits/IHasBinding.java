package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.traits;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.IBinderAction;
import io.reactivex.rxjava3.observers.DisposableObserver;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
public interface IHasBinding {
	@OverridingMethodsMustInvokeSuper
	void initializeBindings(Supplier<? extends Optional<? extends DisposableObserver<IBinderAction>>> binderObserverSupplier);

	// TODO Is binding un-initialization needed?  If this exists, this will allow for manual unbinding.
}
