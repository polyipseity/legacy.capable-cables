package $group__.utilities.references;

import $group__.utilities.CastUtilities;

import javax.annotation.Nullable;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.function.Predicate;

public class WeakReferenceValueEquals<T>
		extends WeakReference<T> {
	public WeakReferenceValueEquals(T referent) { super(referent); }

	public WeakReferenceValueEquals(T referent, ReferenceQueue<? super T> queue) { super(referent, queue); }

	@Override
	public int hashCode() { return Objects.hashCode(get()); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(@Nullable Object obj) {
		return CastUtilities.castChecked(WeakReference.class, obj)
				.map(Reference::get)
				.filter(Predicate.isEqual(get()))
				.isPresent();
	}
}
