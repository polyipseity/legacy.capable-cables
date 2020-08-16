package $group__.utilities.automata;

import $group__.utilities.ObjectUtilities;
import $group__.utilities.automata.core.IState;
import $group__.utilities.automata.core.ITransitionSystem;
import com.google.common.collect.ImmutableMap;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class TransitionSystem<S extends IState<D>, E, D>
		implements ITransitionSystem<S, E, D> {
	protected static final ITransitionSystem<IState<Object>, Object, Object> UNINITIALIZED = new TransitionSystem<>(new IState<Object>() {
		@Override
		public void transitFromThis(Object argument) { throw new UnsupportedOperationException(); }

		@Override
		public void transitToThis(Object argument) { throw new UnsupportedOperationException(); }
	}, null, ImmutableMap
			.of((ts, d) -> true, d -> { throw new UnsupportedOperationException(); }));
	protected final Map<? extends BiPredicate<? super ITransitionSystem<? extends S, ? extends E, ? extends D>, ? super D>, ? extends Function<? super D, ? extends S>> transitionsView;
	protected S state;
	@Nullable
	protected E input;

	public TransitionSystem(S state, @Nullable E input, Map<? extends BiPredicate<? super ITransitionSystem<? extends S, ? extends E, ? extends D>, ? super D>, ? extends Function<? super D, ? extends S>> transitions) {
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
		for (Map.Entry<? extends BiPredicate<? super ITransitionSystem<? extends S, ? extends E, ? extends D>, ? super D>, ? extends Function<? super D, ? extends S>> t
				: getTransitionsView().entrySet()) {
			if (t.getKey().test(this, data)) {
				getState().transitFromThis(data);
				setState(t.getValue().apply(data));
				getState().transitToThis(data);
				break;
			}
		}
	}

	@Override
	public S getState() { return state; }

	@Override
	public Optional<E> getInput() { return Optional.ofNullable(input); }

	@Override
	public Map<? extends BiPredicate<? super ITransitionSystem<? extends S, ? extends E, ? extends D>, ? super D>, ? extends Function<? super D, ? extends S>> getTransitionsView() { return transitionsView; }

	protected void setInput(@Nullable E input) { this.input = input; }

	protected void setState(S state) { this.state = state; }

	@Override
	public int hashCode() { return ObjectUtilities.hashCode(this, null, OBJECT_VARIABLES); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(Object obj) { return ObjectUtilities.equals(this, obj, false, null, OBJECT_VARIABLES); }

	@Override
	public String toString() { return ObjectUtilities.toString(this, super::toString, OBJECT_VARIABLES_MAP); }
}
