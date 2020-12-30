package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.tuples.IIntersection;

import java.util.Optional;
import java.util.function.BiFunction;

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

	@Override
	public <T> T map(BiFunction<@Nonnull ? super L, @Nonnull ? super R, @Nonnull ? extends T> mapper) {
		return mapper.apply(getLeft(), getRight());
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
	public IIntersection<R, L> swap() {
		return of(getRight(), getLeft());
	}

	@SuppressWarnings("ObjectEquality")
	public static <L, R> ImmutableIntersection<L, R> of(L objectAsLeft, R objectAsRight) {
		if (objectAsLeft != objectAsRight)
			throw new IllegalArgumentException();
		return of(objectAsLeft);
	}

	private static <L, R> ImmutableIntersection<L, R> of(Object object) {
		return of(object, null, null);
	}

	public static <L, R> ImmutableIntersection<L, R> of(Object object, @Nullable Class<L> leftClazz, @Nullable Class<R> rightClazz) {
		return new ImmutableIntersection<>(object, leftClazz, rightClazz);
	}

	@Override
	public int hashCode() {
		return ObjectUtilities.hashCodeImpl(this, StaticHolder.getObjectVariableMap().values().iterator());
	}

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(@Nullable Object o) {
		return ObjectUtilities.equalsImpl(this, o, CastUtilities.<Class<IIntersection<?, ?>>>castUnchecked(IIntersection.class), true, StaticHolder.getObjectVariableMap().values().iterator());
	}

	@Override
	public String toString() {
		return ObjectUtilities.toStringImpl(this, StaticHolder.getObjectVariableMap());
	}
}
