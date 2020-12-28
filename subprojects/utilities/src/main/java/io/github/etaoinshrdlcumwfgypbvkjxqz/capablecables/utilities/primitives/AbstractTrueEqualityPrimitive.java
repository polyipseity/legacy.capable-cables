package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.primitives;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AbstractDelegatingObject;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ObjectUtilities;

public abstract class AbstractTrueEqualityPrimitive<T>
		extends AbstractDelegatingObject<T> {
	public AbstractTrueEqualityPrimitive(T delegate) {
		super(delegate);
	}

	@Override
	public abstract int hashCode();

	@Override
	public abstract boolean equals(@Nullable Object obj);

	@Override
	public String toString() {
		return ObjectUtilities.toStringImpl(this, getObjectVariableMapView());
	}
}
