package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.lifecycles;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.lifecycles.EnumUILifecycleState;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.lifecycles.IUILifecycle;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.impl.FunctionUtilities;

import java.util.function.Consumer;

public enum UILifecycleUtilities {
	;

	public static <C> void addStateIdempotent(IUILifecycle instance, EnumUILifecycleState state, @Nullable C context, boolean storeContext, Consumer<? super C> action) {
		if (instance.getLifecycleStateTracker().containsState(state))
			return;
		Consumer<Object> stateApplier = storeContext
				? statefulObject -> state.applyState(statefulObject, context)
				: FunctionUtilities.getEmptyConsumer();
		instance.getLifecycleStateTracker().addState(state, stateApplier);
		action.accept(context);
	}

	public static <C> void removeStateIdempotent(IUILifecycle instance, EnumUILifecycleState state, @Nullable C context, Consumer<? super C> action) {
		if (!instance.getLifecycleStateTracker().removeState(state).isPresent())
			return;
		action.accept(context);
	}
}
