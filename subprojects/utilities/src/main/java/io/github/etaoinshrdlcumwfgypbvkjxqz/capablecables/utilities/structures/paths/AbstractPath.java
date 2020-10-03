package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.paths;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.paths.EmptyPathException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.paths.IPath;

import java.util.List;
import java.util.Optional;

public abstract class AbstractPath<T>
		implements IPath<T> {
	@Override
	public int size() { return getData().size(); }

	@Override
	public Optional<? extends T> getAt(int depth) {
		if (isEmpty())
			return Optional.empty();
		return Optional.of(AssertionUtilities.assertNonnull(getData().get(Math.floorMod(depth, size()))));
	}

	@Override
	public void parentPath(int amount)
			throws EmptyPathException {
		int size = size();
		int newSize = size - amount;
		EmptyPathException.checkSize(newSize);
		getData().subList(newSize, size).clear();
	}

	@Override
	public List<T> asList() { return ImmutableList.copyOf(getData()); }

	@Override
	public void subPath(Iterable<? extends T> elements) { Iterables.addAll(getData(), elements); }

	@Override
	public boolean isEmpty() { return size() == 0; }

	@Override
	public abstract AbstractPath<T> copy();

	protected abstract List<T> getData();
}
