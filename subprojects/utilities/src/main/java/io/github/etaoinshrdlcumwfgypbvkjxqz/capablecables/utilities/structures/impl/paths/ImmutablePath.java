package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.paths;

import com.google.common.collect.ImmutableList;

import java.util.List;

public final class ImmutablePath<T>
		extends AbstractPath<T> {
	private final List<T> data;

	private ImmutablePath(Iterable<? extends T> data) {
		this.data = ImmutableList.copyOf(data);
	}

	public static <T> ImmutablePath<T> of(Iterable<? extends T> data) {
		return new ImmutablePath<>(data);
	}

	@Override
	public ImmutablePath<T> clone() throws CloneNotSupportedException {
		return (ImmutablePath<T>) super.clone();
	}

	@Override
	protected List<T> getData() { return data; }
}
