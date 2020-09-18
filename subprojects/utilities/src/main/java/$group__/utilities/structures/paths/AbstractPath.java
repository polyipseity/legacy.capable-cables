package $group__.utilities.structures.paths;

import $group__.utilities.AssertionUtilities;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.List;

public abstract class AbstractPath<T>
		implements IPath<T> {
	@Override
	public int size() { return getData().size(); }

	@Override
	public T getAt(int depth) { return AssertionUtilities.assertNonnull(getData().get(Math.floorMod(depth, size()))); }

	@Override
	public List<T> asList() { return ImmutableList.copyOf(getData()); }

	@Override
	public void subPath(Iterable<? extends T> elements) { Iterables.addAll(getData(), elements); }

	@Override
	public void parentPath(int amount) {
		List<T> data = getData();
		int size = data.size();
		data.subList(size - amount, size).clear();
	}

	@Override
	public abstract AbstractPath<T> copy();

	protected abstract List<T> getData();
}
