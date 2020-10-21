package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples;

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

	public static <L, R> ImmutableTuple2<L, R> of(L left, R right) { return new ImmutableTuple2<>(left, right); }

	@Override
	public L getLeft() {
		return left;
	}

	@Override
	public R getRight() {
		return right;
	}
}
