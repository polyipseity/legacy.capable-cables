package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;

import javax.annotation.Nullable;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.function.Predicate;

public class WeakReferenceValueEquals<T>
		extends OptionalWeakReference<T> {
	public WeakReferenceValueEquals(@Nullable T referent) { super(referent); }

	public WeakReferenceValueEquals(@Nullable T referent, @Nullable ReferenceQueue<? super T> queue) { super(referent, queue); }

	@Override
	public int hashCode() { return Objects.hashCode(get()); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(@Nullable Object obj) {
		return CastUtilities.castChecked(WeakReference.class, obj)
				.map(Reference<Object>::get)
				.filter(Predicate.isEqual(get()))
				.isPresent();
	}
}
