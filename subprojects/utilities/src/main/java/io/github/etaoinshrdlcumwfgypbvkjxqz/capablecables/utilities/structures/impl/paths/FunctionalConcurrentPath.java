package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.paths;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.DynamicUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;

import java.util.List;
import java.util.function.Function;

public class FunctionalConcurrentPath<T>
		extends AbstractConcurrentPath<T> {
	private static final long DATA_FIELD_OFFSET;

	static {
		try {
			DATA_FIELD_OFFSET = DynamicUtilities.getUnsafe().objectFieldOffset(FunctionalConcurrentPath.class.getDeclaredField("data"));
		} catch (NoSuchFieldException e) {
			throw ThrowableUtilities.propagate(e);
		}
	}

	private final List<T> data;
	private final Function<@Nonnull ? super Iterable<? extends T>, @Nonnull ? extends List<T>> generator;

	public FunctionalConcurrentPath(Iterable<? extends T> data, Function<@Nonnull ? super Iterable<? extends T>, @Nonnull ? extends List<T>> generator) {
		this.generator = generator;
		this.data = AssertionUtilities.assertNonnull(generator.apply(data));
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	@Override
	protected List<T> getData() { return data; }

	protected Function<@Nonnull ? super Iterable<? extends T>, @Nonnull ? extends List<T>> getGenerator() { return generator; }

	@SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
	@Override
	public FunctionalConcurrentPath<T> clone() throws CloneNotSupportedException {
		FunctionalConcurrentPath<T> result = (FunctionalConcurrentPath<T>) super.clone();
		DynamicUtilities.getUnsafe().putObjectVolatile(result, getDataFieldOffset(), result.generator.apply(result.data));
		// COMMENT no need to clone the generator
		return result;
	}

	protected static long getDataFieldOffset() {
		return DATA_FIELD_OFFSET;
	}
}
