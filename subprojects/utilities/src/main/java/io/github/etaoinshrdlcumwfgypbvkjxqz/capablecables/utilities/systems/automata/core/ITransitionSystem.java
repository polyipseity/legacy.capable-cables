package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.automata.core;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * Based on <a href="https://stackoverflow.com/q/1647631/9341868">https://stackoverflow.com/q/1647631/9341868</a>.
 */
public interface ITransitionSystem<S extends IState<D>, E, D> {
	void step(@Nullable E input, D data);

	S getState();

	Optional<? extends E> getInput();

	Map<BiPredicate<? super ITransitionSystem<S, E, D>, ? super D>, Function<? super D, ? extends S>> getTransitionsView();

	enum StaticHolder {
		;

		private static final @Immutable @NonNls Map<String, Function<ITransitionSystem<?, ?, ?>, ?>> OBJECT_VARIABLE_MAP =
				ImmutableMap.<String, Function<ITransitionSystem<?, ?, ?>, ?>>builder()
						.put("state", ITransitionSystem::getState)
						.put("input", ITransitionSystem::getInput)
						.put("transitionsView", ITransitionSystem::getTransitionsView)
						.build();

		public static @Immutable Map<String, Function<ITransitionSystem<?, ?, ?>, ?>> getObjectVariableMap() { return OBJECT_VARIABLE_MAP; }
	}
}
