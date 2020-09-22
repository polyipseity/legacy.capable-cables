package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references;

import javax.annotation.Nullable;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Optional;

public class OptionalWeakReference<T>
		extends WeakReference<T> {
	public OptionalWeakReference(@Nullable T referent) { super(referent); }

	public OptionalWeakReference(@Nullable T referent, @Nullable ReferenceQueue<? super T> q) { super(referent, q); }

	public Optional<? extends T> getOptional() { return Optional.ofNullable(get()); }
}
