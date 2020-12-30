package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.optionals.impl;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.optionals.def.ICompositeOptionalValues;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

public final class Optional2<V1, V2>
		extends AbstractCompositeOptional<Optional2<V1, V2>, Optional2.Values<V1, V2>> {
	private static final Optional2<?, ?> EMPTY = new Optional2<>();
	private final Values<V1, V2> values;

	private Optional2(V1 value1, V2 value2) {
		this.values = new Values<>(value1, value2);
	}

	private Optional2() {
		this.values = new Values<>(null, null);
	}

	public static <V1, V2> Optional2<V1, V2> of(Supplier<@Nullable ? extends V1> value1Supplier,
	                                            Supplier<@Nullable ? extends V2> value2Supplier) {
		@Nullable V1 value1;
		@Nullable V2 value2;
		return (value1 = value1Supplier.get()) == null || (value2 = value2Supplier.get()) == null
				? getEmpty()
				: new Optional2<>(value1, value2);
	}

	@SuppressWarnings("unchecked")
	public static <V1, V2> Optional2<V1, V2> getEmpty() { return (Optional2<V1, V2>) EMPTY; }

	@Override
	protected Optional2<V1, V2> getThis() { return this; }

	@Override
	protected Optional2<V1, V2> getStaticEmpty() { return getEmpty(); }

	@Override
	protected Values<V1, V2> getValues() { return values; }

	/* SECTION specialized */

	public void ifPresent(BiConsumer<@Nonnull ? super V1, @Nonnull ? super V2> consumer) {
		ifPresent(values -> consumer.accept(values.getValue1Nonnull(), values.getValue2Nonnull()));
	}

	public Optional2<V1, V2> filter(BiPredicate<@Nonnull ? super V1, @Nonnull ? super V2> predicate) {
		return filter(values -> predicate.test(values.getValue1Nonnull(), values.getValue2Nonnull()));
	}

	public <R> Optional<R> map(BiFunction<@Nonnull ? super V1, @Nonnull ? super V2, @Nullable ? extends R> mapper) {
		return map(values -> mapper.apply(values.getValue1Nonnull(), values.getValue2Nonnull()));
	}

	public <R> Optional<R> flatMap(BiFunction<@Nonnull ? super V1, @Nonnull ? super V2, @Nonnull ? extends Optional<? extends R>> mapper) {
		return flatMap(values -> mapper.apply(values.getValue1Nonnull(), values.getValue2Nonnull()));
	}

	/* SECTION END specialized */

	public static final class Values<V1, V2>
			implements ICompositeOptionalValues {
		@Nullable
		private final V1 value1;
		@Nullable
		private final V2 value2;

		private Values(@Nullable V1 value1, @Nullable V2 value2) {
			this.value1 = value1;
			this.value2 = value2;
		}

		public V1 getValue1Nonnull() { return AssertionUtilities.assertNonnull(getValue1()); }

		@Nullable
		protected V1 getValue1() { return value1; }

		public V2 getValue2Nonnull() { return AssertionUtilities.assertNonnull(getValue2()); }

		@Nullable
		protected V2 getValue2() { return value2; }

		@Override
		public Iterator<? extends Supplier<@Nullable ?>> getSuppliers() {
			return ImmutableList.<Supplier<@Nullable ?>>of(this::getValue1, this::getValue2).iterator();
		}
	}
}
