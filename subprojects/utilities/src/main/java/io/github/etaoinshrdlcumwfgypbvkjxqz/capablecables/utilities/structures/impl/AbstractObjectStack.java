package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IObjectStack;
import org.jetbrains.annotations.NonNls;

import java.util.Deque;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities.assertNonnull;

public abstract class AbstractObjectStack<T>
		implements IObjectStack<T> {
	@NonNls
	private static final @Immutable Map<String, Function<? super AbstractObjectStack<?>, ?>> OBJECT_VARIABLES_MAP =
			ImmutableMap.<String, Function<? super AbstractObjectStack<?>, ?>>builder()
					.put("data", AbstractObjectStack::getData)
					.build();

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
	public int hashCode() {
		return ObjectUtilities.hashCodeImpl(this, getObjectVariablesMap().values());
	}

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(Object obj) {
		return ObjectUtilities.equalsImpl(this, obj, CastUtilities.<Class<AbstractObjectStack<?>>>castUnchecked(AbstractObjectStack.class), false, getObjectVariablesMap().values());
	}

	@Override
	public String toString() { return ObjectUtilities.toStringImpl(this, getObjectVariablesMap()); }

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
