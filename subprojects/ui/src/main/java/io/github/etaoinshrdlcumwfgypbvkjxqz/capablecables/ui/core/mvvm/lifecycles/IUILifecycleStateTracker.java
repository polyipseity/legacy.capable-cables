package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.lifecycles;

import java.util.Optional;
import java.util.function.Consumer;

public interface IUILifecycleStateTracker {
	void apply(Iterable<?> statefulObjects);

	boolean containsState(EnumUILifecycleState state);

	Optional<? extends Consumer<? super Object>> getStateApplier(EnumUILifecycleState state);

	@SuppressWarnings("UnusedReturnValue")
	Optional<? extends Consumer<? super Object>> addState(EnumUILifecycleState state, Consumer<? super Object> stateApplier);

	Optional<? extends Consumer<? super Object>> removeState(EnumUILifecycleState state);
}
