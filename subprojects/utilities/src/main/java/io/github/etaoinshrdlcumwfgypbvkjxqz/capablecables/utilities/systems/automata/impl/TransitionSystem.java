package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.automata.impl;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.FunctionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.automata.core.IState;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.automata.core.ITransitionSystem;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class TransitionSystem<S extends IState<D>, E, D>
		implements ITransitionSystem<S, E, D> {
	private static final ITransitionSystem<IState<Object>, Object, Object> UNINITIALIZED = new TransitionSystem<>(new IState<Object>() {
		@Override
		public void transitFromThis(Object argument) { throw new UnsupportedOperationException(); }

		@Override
		public void transitToThis(Object argument) { throw new UnsupportedOperationException(); }
	}, null, ImmutableMap
			.of(FunctionUtilities.getAlwaysTrueBiPredicate(), d -> { throw new UnsupportedOperationException(); }));
	private final @Immutable Map<BiPredicate<@Nonnull ? super ITransitionSystem<S, E, D>, @Nonnull ? super D>, Function<@Nonnull ? super D, @Nonnull ? extends S>> transitions;
	private S state;
	@Nullable
	private E input;

	public TransitionSystem(S state, @Nullable E input, Map<BiPredicate<@Nonnull ? super ITransitionSystem<S, E, D>, @Nonnull ? super D>, Function<@Nonnull ? super D, @Nonnull ? extends S>> transitions) {
		this.state = state;
		this.input = input;
		this.transitions = ImmutableMap.copyOf(transitions);
	}

	@Override
	public @Immutable Map<BiPredicate<@Nonnull ? super ITransitionSystem<S, E, D>, @Nonnull ? super D>, Function<@Nonnull ? super D, @Nonnull ? extends S>> getTransitionsView() { return ImmutableMap.copyOf(getTransitions()); }

	@SuppressWarnings("unchecked")
	public static <S extends IState<D>, E, D> ITransitionSystem<S, E, D> getUninitialized() {
		return (ITransitionSystem<S, E, D>) UNINITIALIZED; // COMMENT step always throw
	}

	@Override
	public void step(@Nullable E input, D data) {
		setInput(input);
		getTransitionsView().entrySet().stream().unordered()
				.filter(entry -> AssertionUtilities.assertNonnull(entry.getKey()).test(this, data))
				.map(Map.Entry::getValue)
				.findFirst()
				.ifPresent(newState -> {
					getState().transitFromThis(data);
					setState(AssertionUtilities.assertNonnull(newState.apply(data)));
					getState().transitToThis(data);
				});
	}

	@Override
	public S getState() { return state; }

	@Override
	public Optional<? extends E> getInput() { return Optional.ofNullable(input); }

	protected @Immutable Map<BiPredicate<@Nonnull ? super ITransitionSystem<S, E, D>, @Nonnull ? super D>, Function<@Nonnull ? super D, @Nonnull ? extends S>> getTransitions() {
		return transitions;
	}

	protected void setInput(@Nullable E input) { this.input = input; }

	protected void setState(S state) { this.state = state; }

	@Override
	public int hashCode() { return ObjectUtilities.hashCodeImpl(this, StaticHolder.getObjectVariableMap().values()); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(Object obj) { return ObjectUtilities.equalsImpl(this, obj, CastUtilities.<Class<ITransitionSystem<?, ?, ?>>>castUnchecked(ITransitionSystem.class), true, StaticHolder.getObjectVariableMap().values()); }

	@Override
	public String toString() { return ObjectUtilities.toStringImpl(this, StaticHolder.getObjectVariableMap()); }
}
