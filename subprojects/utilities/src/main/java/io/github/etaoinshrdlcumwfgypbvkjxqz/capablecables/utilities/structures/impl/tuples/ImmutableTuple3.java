package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.tuples.ITuple3;

public final class ImmutableTuple3<L, M, R>
		extends AbstractTuple
		implements ITuple3<L, M, R> {
	private final L left;
	private final M middle;
	private final R right;

	private ImmutableTuple3(L left, M middle, R right) {
		super(left, middle, right);
		this.left = left;
		this.middle = middle;
		this.right = right;
	}

	public static <L, M, R> ImmutableTuple3<L, M, R> of(L left, M middle, R right) {
		return new ImmutableTuple3<>(left, middle, right);
	}

	@Override
	public L getLeft() {
		return left;
	}

	@Override
	public M getMiddle() {
		return middle;
	}

	@Override
	public R getRight() {
		return right;
	}

	@Override
	public int hashCode() {
		return ObjectUtilities.hashCodeImpl(this, StaticHolder.getObjectVariableMap().values().iterator());
	}

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(@Nullable Object o) {
		return ObjectUtilities.equalsImpl(this, o, CastUtilities.<Class<ITuple3<?, ?, ?>>>castUnchecked(ITuple3.class), true, StaticHolder.getObjectVariableMap().values().iterator());
	}

	@Override
	public String toString() {
		return ObjectUtilities.toStringImpl(this, StaticHolder.getObjectVariableMap());
	}
}
