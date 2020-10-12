package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IObjectStack;
import org.jetbrains.annotations.NonNls;

import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities.assertNonnull;

public abstract class AbstractObjectStack<T>
		implements IObjectStack<T> {
	private static final @Immutable List<Function<? super AbstractObjectStack<?>, ?>> OBJECT_VARIABLES = ImmutableList.of(
			AbstractObjectStack::getData);
	@NonNls
	private static final @Immutable Map<String, Function<? super AbstractObjectStack<?>, ?>> OBJECT_VARIABLES_MAP = ImmutableMap.copyOf(MapUtilities.zipKeysValues(
			ImmutableList.of("data"),
			getObjectVariables()));

	@Override
	public T push(T element) {
		getData().push(element);
		return element;
	}

	@Override
	public T pop() { return getData().pop(); }

	@Override
	public T element()
			throws NoSuchElementException { return assertNonnull(getData().element()); }

	@Override
	public int size() { return getData().size(); }

	protected abstract Deque<T> getData();

	@Override
	public int hashCode() { return ObjectUtilities.hashCode(this, null, getObjectVariables()); }

	public static @Immutable List<Function<? super AbstractObjectStack<?>, ?>> getObjectVariables() {
		return OBJECT_VARIABLES;
	}

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(Object obj) { return ObjectUtilities.equals(this, obj, false, null, getObjectVariables()); }

	@Override
	public String toString() { return ObjectUtilities.toString(this, super::toString, getObjectVariablesMap()); }

	public static @NonNls @Immutable Map<String, Function<? super AbstractObjectStack<?>, ?>> getObjectVariablesMap() {
		return OBJECT_VARIABLES_MAP;
	}

	public abstract static class CopyPushable<T>
			extends AbstractObjectStack<T>
			implements IObjectStack.ICopyPushable<T> {
		@Override
		public T push() {
			T ret = copyElement(assertNonnull(getData().element()));
			getData().push(ret);
			return ret;
		}

		protected abstract T copyElement(T object);
	}
}
