package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.lifecycles;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.lifecycles.EnumUILifecycleState;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.lifecycles.IUILifecycleStateTracker;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.impl.FunctionUtilities;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

public class UIDefaultLifecycleStateTracker
		implements IUILifecycleStateTracker {
	private final ConcurrentMap<EnumUILifecycleState, Consumer<? super Object>> stateAppliers =
			MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(EnumUILifecycleState.values().length).makeMap();

	@Override
	public void apply(Iterator<?> statefulObjects) {
		statefulObjects.forEachRemaining(getStateAppliers().entrySet().stream()
				.sorted(Comparator.comparingInt(entry -> AssertionUtilities.assertNonnull(entry.getKey()).ordinal()))
				.map(Map.Entry::getValue)
				.map(AssertionUtilities::assertNonnull)
				.reduce(FunctionUtilities.getEmptyConsumer(), Consumer::andThen));
	}

	@Override
	public boolean containsState(EnumUILifecycleState state) {
		return getStateAppliers().containsKey(state);
	}

	@Override
	public Optional<? extends Consumer<? super Object>> getStateApplier(EnumUILifecycleState state) {
		return Optional.ofNullable(getStateAppliers().get(state));
	}

	@Override
	public Optional<? extends Consumer<? super Object>> addState(EnumUILifecycleState state, Consumer<? super Object> stateApplier) {
		return Optional.ofNullable(getStateAppliers().put(state, stateApplier));
	}

	@Override
	public Optional<? extends Consumer<? super Object>> removeState(EnumUILifecycleState state) {
		return Optional.ofNullable(getStateAppliers().remove(state));
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<EnumUILifecycleState, Consumer<? super Object>> getStateAppliers() {
		return stateAppliers;
	}
}
