package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.ITuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.IUnion;

import javax.annotation.Nullable;
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

	@Override
	public int hashCode() {
		return ObjectUtilities.hashCodeImpl(this, StaticHolder.getObjectVariableMap().values());
	}

	public static <L, R> ImmutableUnion<L, R> left(L left) {
		return of(left, null);
	}

	public static <L, R> ImmutableUnion<L, R> choice(Object object, Class<L> leftClazz, Class<R> rightClazz)
			throws IllegalArgumentException {
		if (leftClazz.isInstance(object))
			return left(leftClazz.cast(object));
		else if (rightClazz.isInstance(object))
			return right(rightClazz.cast(object));
		throw new IllegalArgumentException();
	}

	public static <L, R> ImmutableUnion<L, R> right(R right) {
		return of(null, right);
	}

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(Object o) {
		return ObjectUtilities.equalsImpl(this, o, CastUtilities.<Class<ITuple2<?, ?>>>castUnchecked(IUnion.class), true, StaticHolder.getObjectVariableMap().values());
	}

	@Override
	public String toString() {
		return ObjectUtilities.toStringImpl(this, StaticHolder.getObjectVariableMap());
	}

	private static <L, R> ImmutableUnion<L, R> of(@Nullable L left, @Nullable R right) {
		return new ImmutableUnion<>(left, right);
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
	public <T> T map(Function<? super L, ? extends T> leftMapper, Function<? super R, ? extends T> rightMapper) {
		@Nullable T leftResult = getLeft().map(leftMapper).orElse(null);
		@Nullable T rightResult = getRight().map(rightMapper).orElse(null);
		IUnion.assertEither(leftResult, rightResult);
		return Optional.ofNullable(rightResult).orElseGet(() -> AssertionUtilities.assertNonnull(leftResult));
	}

	@Override
	public <L2, R2> IUnion<L2, R2> mapBoth(Function<? super L, ? extends L2> leftMapper, Function<? super R, ? extends R2> rightMapper) throws IllegalArgumentException {
		return map(
				left -> left(AssertionUtilities.assertNonnull(leftMapper.apply(left))),
				right -> right(AssertionUtilities.assertNonnull(rightMapper.apply(right)))
		);
	}

	@Override
	public void accept(Consumer<? super L> leftConsumer, Consumer<R> rightConsumer) {
		// COMMENT at least one of them is present
		getLeft().ifPresent(leftConsumer);
		getRight().ifPresent(rightConsumer);
	}

	@Override
	public IUnion<R, L> swap() {
		return map(ImmutableUnion::right, ImmutableUnion::left);
	}
}
