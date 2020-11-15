package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.IIntersection;

import java.util.Optional;

public final class ImmutableIntersection<L, R>
		extends AbstractTuple
		implements IIntersection<L, R> {
	private final L left;
	private final R right;

	@SuppressWarnings("unchecked")
	private ImmutableIntersection(Object object, @Nullable Class<L> leftClazz, @Nullable Class<R> rightClazz)
			throws ClassCastException {
		super(object, object);
		// COMMENT throws ClassCastException automatically
		this.left = Optional.ofNullable(leftClazz).map(clazz -> clazz.cast(object)).orElseGet(() -> (L) object);
		this.right = Optional.ofNullable(rightClazz).map(clazz -> clazz.cast(object)).orElseGet(() -> (R) object);
	}

	@SuppressWarnings("ObjectEquality")
	public static <L, R> ImmutableIntersection<L, R> of(L objectAsLeft, R objectAsRight) {
		assert objectAsLeft == objectAsRight;
		return of(objectAsLeft);
	}

	private static <L, R> ImmutableIntersection<L, R> of(Object object) {
		return of(object, null, null);
	}

	public static <L, R> ImmutableIntersection<L, R> of(Object object, @Nullable Class<L> leftClazz, @Nullable Class<R> rightClazz) {
		return new ImmutableIntersection<>(object, leftClazz, rightClazz);
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
	public int hashCode() {
		return ObjectUtilities.hashCodeImpl(this, StaticHolder.getObjectVariableMap().values());
	}

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(@Nullable Object o) {
		return ObjectUtilities.equalsImpl(this, o, CastUtilities.<Class<IIntersection<?, ?>>>castUnchecked(IIntersection.class), true, StaticHolder.getObjectVariableMap().values());
	}

	@Override
	public String toString() {
		return ObjectUtilities.toStringImpl(this, StaticHolder.getObjectVariableMap());
	}

	@Override
	public IIntersection<R, L> swap() {
		return of(getRight(), getLeft());
	}
}
