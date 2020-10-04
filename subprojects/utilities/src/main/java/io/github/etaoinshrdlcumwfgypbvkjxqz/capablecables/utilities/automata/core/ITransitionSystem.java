package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.automata.core;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapUtilities;
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

		private static final ImmutableList<Function<? super ITransitionSystem<?, ?, ?>, ?>> OBJECT_VARIABLES = ImmutableList.of(
				ITransitionSystem::getState, ITransitionSystem::getInput, ITransitionSystem::getTransitionsView);
		@NonNls
		private static final
		ImmutableMap<String, Function<? super ITransitionSystem<?, ?, ?>, ?>> OBJECT_VARIABLES_MAP = ImmutableMap.copyOf(MapUtilities.zipKeysValues(
				ImmutableList.of("state", "input", "transitionsView"),
				getObjectVariables()));

		public static ImmutableList<Function<? super ITransitionSystem<?, ?, ?>, ?>> getObjectVariables() { return OBJECT_VARIABLES; }

		public static ImmutableMap<String, Function<? super ITransitionSystem<?, ?, ?>, ?>> getObjectVariablesMap() { return OBJECT_VARIABLES_MAP; }
	}
}
