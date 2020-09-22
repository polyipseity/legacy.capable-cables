package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.paths;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class ImmutablePath<T>
		extends AbstractPath<T> {
	protected final List<T> data;

	public ImmutablePath(Iterable<? extends T> data)
			throws EmptyPathException {
		this.data = ImmutableList.copyOf(data);
		EmptyPathException.checkPathData(this.data);
	}

	@Override
	public ImmutablePath<T> getParentPath() throws EmptyPathException { return new ImmutablePath<>(getData().subList(0, getData().size() - 1)); }

	@Override
	public ImmutablePath<T> copy() { return new ImmutablePath<>(getData()); }

	@Override
	protected List<T> getData() { return data; }
}
