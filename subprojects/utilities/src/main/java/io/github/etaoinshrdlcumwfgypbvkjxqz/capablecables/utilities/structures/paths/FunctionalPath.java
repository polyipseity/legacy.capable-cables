package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.paths;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;

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

	@Override
	public FunctionalPath<T> copy() { return new FunctionalPath<>(getData(), getGenerator()); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	@Override
	protected List<T> getData() { return data; }
}
