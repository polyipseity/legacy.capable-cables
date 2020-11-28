package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Objects;

public class IdentityWeakReference<T>
		extends OptionalWeakReference<T> {
	private static final IdentityWeakReference<?> NULL_REFERENCE = new IdentityWeakReference<>(null, null);

	protected IdentityWeakReference(@Nullable T referent, @Nullable ReferenceQueue<? super T> queue) { super(referent, queue); }

	public static <T> IdentityWeakReference<T> of(@Nullable T referent) {
		return of(referent, null);
	}

	public static <T> IdentityWeakReference<T> of(@Nullable T referent, @Nullable ReferenceQueue<? super T> queue) {
		if (referent == null && queue == null)
			return getNullReference();
		return new IdentityWeakReference<>(referent, queue);
	}

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

	@SuppressWarnings("unchecked")
	public static <T> IdentityWeakReference<T> getNullReference() {
		return (IdentityWeakReference<T>) NULL_REFERENCE; // COMMENT safe, produces 'null'
	}
}
