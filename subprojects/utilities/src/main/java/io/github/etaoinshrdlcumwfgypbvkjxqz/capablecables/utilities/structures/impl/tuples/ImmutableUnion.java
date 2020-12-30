package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.tuples.ITuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.tuples.IUnion;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public final class ImmutableUnion<L, R>
		extends AbstractTuple
		implements IUnion<L, R> {
	@Nullable
	private final L left;
	@Nullable
	private final R right;

	private ImmutableUnion(@Nullable L left, @Nullable R right) {
		super(Optional.ofNullable(left), Optional.ofNullable(right));
		IUnion.assertEither(left, right);
		this.left = left;
		this.right = right;
	}

	public static <L, R> ImmutableUnion<L, R> of(Object object, Class<L> leftClazz, Class<R> rightClazz)
			throws IllegalArgumentException {
		if (leftClazz.isInstance(object))
			return ofLeft(leftClazz.cast(object));
		else if (rightClazz.isInstance(object))
			return ofRight(rightClazz.cast(object));
		throw new IllegalArgumentException();
	}

	public static <L, R> ImmutableUnion<L, R> ofLeft(L left) {
		return of(left, null);
	}

	public static <L, R> ImmutableUnion<L, R> ofRight(R right) {
		return of(null, right);
	}

	private static <L, R> ImmutableUnion<L, R> of(@Nullable L left, @Nullable R right) {
		return new ImmutableUnion<>(left, right);
	}

	@Override
	public int hashCode() {
		return ObjectUtilities.hashCodeImpl(this, StaticHolder.getObjectVariableMap().values().iterator());
	}

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(@Nullable Object o) {
		return ObjectUtilities.equalsImpl(this, o, CastUtilities.<Class<ITuple2<?, ?>>>castUnchecked(IUnion.class), true, StaticHolder.getObjectVariableMap().values().iterator());
	}

	@Override
	public String toString() {
		return ObjectUtilities.toStringImpl(this, StaticHolder.getObjectVariableMap());
	}

	@Override
	public <L2, R2> IUnion<L2, R2> mapBoth(Function<@Nonnull ? super L, @Nonnull ? extends L2> leftMapper, Function<@Nonnull ? super R, @Nonnull ? extends R2> rightMapper) throws IllegalArgumentException {
		return map(
				left -> ofLeft(leftMapper.apply(left)),
				right -> ofRight(rightMapper.apply(right))
		);
	}

	@Override
	public Optional<? extends L> getLeft() {
		return Optional.ofNullable(left);
	}

	@Override
	public Optional<? extends R> getRight() {
		return Optional.ofNullable(right);
	}

	@Override
	public <T> T map(Function<@Nonnull ? super L, @Nonnull ? extends T> leftMapper, Function<@Nonnull ? super R, @Nonnull ? extends T> rightMapper) {
		@Nullable T leftResult = getLeft().map(leftMapper).orElse(null);
		@Nullable T rightResult = getRight().map(rightMapper).orElse(null);
		IUnion.assertEither(leftResult, rightResult);
		return Optional.ofNullable(rightResult).orElse(leftResult);
	}

	@Override
	public IUnion<R, L> swap() {
		return map(ImmutableUnion::ofRight, ImmutableUnion::ofLeft);
	}

	@Override
	public IUnion<L, R> accept(Consumer<@Nonnull ? super L> leftConsumer, Consumer<@Nonnull ? super R> rightConsumer) {
		// COMMENT one and only one of them is present
		getLeft().ifPresent(leftConsumer);
		getRight().ifPresent(rightConsumer);
		return this;
	}
}
