package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl.fields;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ComparableUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ConstantValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.fields.IBindingField;

import java.util.function.Supplier;

public class RangedBindingField<T extends Comparable<T>>
		extends DelegatingBindingField<T> {
	private final Supplier<@Nullable ? extends T> lowerBoundSupplier;
	private final Supplier<@Nullable ? extends T> upperBoundSupplier;

	protected RangedBindingField(IBindingField<T> delegate,
	                             Supplier<@Nullable ? extends T> lowerBoundSupplier,
	                             Supplier<@Nullable ? extends T> upperBoundSupplier) {
		super(delegate);
		this.lowerBoundSupplier = lowerBoundSupplier;
		this.upperBoundSupplier = upperBoundSupplier;
	}

	public static <T extends Comparable<T>> RangedBindingField<T> of(IBindingField<T> delegate,
	                                                                 T lowerBound,
	                                                                 T upperBound) {
		assert ComparableUtilities.lessThanOrEqualTo(lowerBound, upperBound);
		return of(delegate, ConstantValue.of(lowerBound), ConstantValue.of(upperBound));
	}

	public static <T extends Comparable<T>> RangedBindingField<T> of(IBindingField<T> delegate,
	                                                                 Supplier<@Nullable ? extends T> lowerBoundSupplier,
	                                                                 Supplier<@Nullable ? extends T> upperBoundSupplier) {
		// COMMENT note: we will not check for the changes in the return values of the suppliers
		return new RangedBindingField<>(delegate, lowerBoundSupplier, upperBoundSupplier);
	}

	@Override
	public void setValue(T value) {
		@Nullable T lowerBound = getLowerBoundSupplier().get();
		if (lowerBound != null && ComparableUtilities.lessThan(value, lowerBound))
			value = lowerBound;
		else {
			@Nullable T upperBound = getUpperBoundSupplier().get();
			if (upperBound != null && ComparableUtilities.greaterThan(value, upperBound))
				value = upperBound;
		}
		super.setValue(value);
	}

	protected Supplier<@Nullable ? extends T> getLowerBoundSupplier() {
		return lowerBoundSupplier;
	}

	protected Supplier<@Nullable ? extends T> getUpperBoundSupplier() {
		return upperBoundSupplier;
	}
}
