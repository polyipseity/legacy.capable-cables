package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Objects;

public class IdentityWeakReference<T>
		extends OptionalWeakReference<T> {
	public IdentityWeakReference(@Nullable T referent) { super(referent); }

	public IdentityWeakReference(@Nullable T referent, @Nullable ReferenceQueue<? super T> queue) { super(referent, queue); }

	@Override
	public int hashCode() { return Objects.hashCode(get()); }

	@SuppressWarnings({"ObjectEquality", "EqualsWhichDoesntCheckParameterClass"})
	@Override
	public boolean equals(@Nullable Object obj) {
		return CastUtilities.castChecked(WeakReference.class, obj)
				.map(Reference<Object>::get)
				.filter(oc -> get() == oc)
				.isPresent();
	}
}
