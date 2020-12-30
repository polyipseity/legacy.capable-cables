package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.ITuple2;

public final class ImmutableTuple2<L, R>
		extends AbstractTuple
		implements ITuple2<L, R> {
	private final L left;
	private final R right;

	private ImmutableTuple2(L left, R right) {
		super(left, right);
		this.left = left;
		this.right = right;
	}

	@Override
	public int hashCode() {
		return ObjectUtilities.hashCodeImpl(this, StaticHolder.getObjectVariableMap().values().iterator());
	}

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(@Nullable Object o) {
		return ObjectUtilities.equalsImpl(this, o, CastUtilities.<Class<ITuple2<?, ?>>>castUnchecked(ITuple2.class), true, StaticHolder.getObjectVariableMap().values().iterator());
	}

	public static <L, R> ImmutableTuple2<L, R> of(L left, R right) { return new ImmutableTuple2<>(left, right); }

	@Override
	public String toString() {
		return ObjectUtilities.toStringImpl(this, StaticHolder.getObjectVariableMap());
	}

	@Override
	public L getLeft() {
		return left;
	}

	@Override
	public R getRight() {
		return right;
	}

	@Override
	public ITuple2<R, L> swap() {
		return of(getRight(), getLeft());
	}


}
