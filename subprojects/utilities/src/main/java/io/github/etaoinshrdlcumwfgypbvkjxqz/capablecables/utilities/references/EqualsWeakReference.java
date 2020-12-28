package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.function.Predicate;

public class EqualsWeakReference<T>
		extends OptionalWeakReference<T> {
	private static final EqualsWeakReference<?> NULL_REFERENCE = new EqualsWeakReference<>(null, null);

	protected EqualsWeakReference(@Nullable T referent, @Nullable ReferenceQueue<? super T> queue) { super(referent, queue); }

	public static <T> EqualsWeakReference<T> of(@Nullable T referent) {
		return of(referent, null);
	}

	public static <T> EqualsWeakReference<T> of(@Nullable T referent, @Nullable ReferenceQueue<? super T> queue) {
		if (referent == null && queue == null)
			return getNullReference();
		return new EqualsWeakReference<>(referent, queue);
	}

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

	@SuppressWarnings("unchecked")
	public static <T> EqualsWeakReference<T> getNullReference() {
		return (EqualsWeakReference<T>) NULL_REFERENCE; // COMMENT safe, produces 'null'
	}
}
