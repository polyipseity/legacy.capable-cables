package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.paths;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.DynamicUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;

import java.util.List;
import java.util.function.Function;

public class FunctionalPath<T>
		extends AbstractPath<T> {
	private final List<T> data;
	private final Function<? super Iterable<? extends T>, ? extends List<T>> generator;

	public FunctionalPath(Iterable<? extends T> data, Function<? super Iterable<? extends T>, ? extends List<T>> generator) {
		this.generator = generator;
		this.data = AssertionUtilities.assertNonnull(generator.apply(data));
	}

	protected Function<? super Iterable<? extends T>, ? extends List<T>> getGenerator() { return generator; }

	private static final long DATA_FIELD_OFFSET;

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	@Override
	protected List<T> getData() { return data; }

	static {
		try {
			DATA_FIELD_OFFSET = DynamicUtilities.getUnsafe().objectFieldOffset(FunctionalPath.class.getDeclaredField("data"));
		} catch (NoSuchFieldException e) {
			throw ThrowableUtilities.propagate(e);
		}
	}

	@SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
	@Override
	public FunctionalPath<T> clone() throws CloneNotSupportedException {
		FunctionalPath<T> result = (FunctionalPath<T>) super.clone();
		DynamicUtilities.getUnsafe().putObjectVolatile(result, getDataFieldOffset(), result.generator.apply(result.data));
		return result;
	}

	protected static long getDataFieldOffset() {
		return DATA_FIELD_OFFSET;
	}
}
