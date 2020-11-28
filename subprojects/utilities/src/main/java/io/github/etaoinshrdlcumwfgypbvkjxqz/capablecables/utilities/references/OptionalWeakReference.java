package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Optional;

public class OptionalWeakReference<T>
		extends WeakReference<T> {
	private static final OptionalWeakReference<?> NULL_REFERENCE = new OptionalWeakReference<>(null, null);

	protected OptionalWeakReference(@Nullable T referent, @Nullable ReferenceQueue<? super T> queue) { super(referent, queue); }

	public static <T> OptionalWeakReference<T> of(@Nullable T referent) {
		return of(referent, null);
	}

	public static <T> OptionalWeakReference<T> of(@Nullable T referent, @Nullable ReferenceQueue<? super T> queue) {
		if (referent == null && queue == null)
			return getNullReference();
		return new OptionalWeakReference<>(referent, queue);
	}

	public Optional<? extends T> getOptional() { return Optional.ofNullable(get()); }

	@SuppressWarnings("unchecked")
	public static <T> OptionalWeakReference<T> getNullReference() {
		return (OptionalWeakReference<T>) NULL_REFERENCE; // COMMENT safe, produces 'null'
	}
}
