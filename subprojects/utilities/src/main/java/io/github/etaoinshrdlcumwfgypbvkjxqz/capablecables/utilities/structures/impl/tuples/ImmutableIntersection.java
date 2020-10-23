package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.IIntersection;

import javax.annotation.Nullable;
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
		return ObjectUtilities.hashCodeImpl(this, StaticHolder.getObjectVariablesMap().values());
	}

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(Object o) {
		return ObjectUtilities.equalsImpl(this, o, CastUtilities.<Class<IIntersection<?, ?>>>castUnchecked(IIntersection.class), true, StaticHolder.getObjectVariablesMap().values());
	}

	@Override
	public String toString() {
		return ObjectUtilities.toStringImpl(this, StaticHolder.getObjectVariablesMap());
	}
}
