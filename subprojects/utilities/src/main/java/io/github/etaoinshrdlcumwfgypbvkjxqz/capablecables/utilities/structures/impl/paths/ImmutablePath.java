package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.paths;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class ImmutablePath<T>
		extends AbstractPath<T> {
	private final List<T> data;

	public ImmutablePath(Iterable<? extends T> data) {
		this.data = ImmutableList.copyOf(data);
	}

	@Override
	public ImmutablePath<T> copy() { return new ImmutablePath<>(getData()); }

	@Override
	protected List<T> getData() { return data; }
}
