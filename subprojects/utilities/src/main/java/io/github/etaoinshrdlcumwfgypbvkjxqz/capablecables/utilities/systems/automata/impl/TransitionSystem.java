package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.automata.impl;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.FunctionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.automata.core.IState;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.automata.core.ITransitionSystem;

import javax.annotation.Nullable;
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
	private final Map<BiPredicate<? super ITransitionSystem<S, E, D>, ? super D>, Function<? super D, ? extends S>> transitionsView;
	private S state;
	@Nullable
	private E input;

	public TransitionSystem(S state, @Nullable E input, Map<? extends BiPredicate<? super ITransitionSystem<S, E, D>, ? super D>, ? extends Function<? super D, ? extends S>> transitions) {
		this.state = state;
		this.input = input;
		this.transitionsView = ImmutableMap.copyOf(transitions);
	}

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

	@Override
	public Map<BiPredicate<? super ITransitionSystem<S, E, D>, ? super D>, Function<? super D, ? extends S>> getTransitionsView() { return transitionsView; }

	protected void setInput(@Nullable E input) { this.input = input; }

	protected void setState(S state) { this.state = state; }

	@Override
	public int hashCode() { return ObjectUtilities.hashCode(this, null, StaticHolder.getObjectVariables()); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(Object obj) { return ObjectUtilities.equals(this, CastUtilities.<Class<ITransitionSystem<?, ?, ?>>>castUnchecked(ITransitionSystem.class), obj, false, null, StaticHolder.getObjectVariables()); }

	@Override
	public String toString() { return ObjectUtilities.toString(this, super::toString, StaticHolder.getObjectVariablesMap()); }
}
