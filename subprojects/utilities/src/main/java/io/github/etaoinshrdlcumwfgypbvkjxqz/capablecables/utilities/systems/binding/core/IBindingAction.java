package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface IBindingAction
		extends Consumer<IBinder> {
	EnumActionType getActionType();

	Iterable<? extends IBinding<?>> getBindings();

	@Override
	default void accept(IBinder binder) {
		getActionType().accept(binder, getBindings());
	}

	enum EnumActionType
			implements BiConsumer<IBinder, Iterable<? extends IBinding<?>>> {
		BIND {
			@Override
			public void accept(IBinder binder, Iterable<? extends IBinding<?>> bindings) {
				binder.bind(bindings);
			}
		},
		UNBIND {
			@Override
			public void accept(IBinder binder, Iterable<? extends IBinding<?>> bindings) {
				binder.unbind(bindings);
			}
		},
	}
}
