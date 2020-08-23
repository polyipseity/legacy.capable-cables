package $group__.utilities.references;

import javax.annotation.Nullable;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Objects;

public class WeakReferenceValueEquals<T>
		extends WeakReference<T> {
	public WeakReferenceValueEquals(T referent) { super(referent); }

	public WeakReferenceValueEquals(T referent, ReferenceQueue<? super T> queue) { super(referent, queue); }

	@Override
	public int hashCode() { return Objects.hashCode(get()); }

	@Override
	public boolean equals(@Nullable Object obj) {
		if (obj instanceof WeakReference)
			return Objects.equals(get(), ((WeakReference<?>) obj).get());
		return false;
	}
}
