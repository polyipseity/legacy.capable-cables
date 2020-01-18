package $group__.$modId__.utilities.constructs.classes.concrete;

import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import com.google.common.collect.ImmutableMap;

import javax.annotation.meta.When;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.Map;
import java.util.Set;

import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictEquals.isEquals;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.ICloneable.tryClone;
import static $group__.$modId__.utilities.helpers.Miscellaneous.Casts.castUnchecked;
import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.variables.Constants.GROUP;

public class ReferenceMap<K, V extends Reference<?>, T extends ReferenceMap<K, V, T>> extends DelegateMap<K, V, T> {
	/* SECTION variables */

	protected ReferenceQueue<?> queue;


	/* SECTION constructors */

	public ReferenceMap(/* REMARKS mutable */ Map<K, V> delegate, ReferenceQueue<?> queue) {
		super(delegate);
		this.queue = queue;
	}

	public ReferenceMap(ReferenceMap<K, V, ?> c) { this(c.getDelegate(), c.getQueue()); }


	/* SECTION getters & setters */

	public ReferenceQueue<?> getQueue() { return queue; }

	protected void setQueue(ReferenceQueue<?> queue) { this.queue = queue; }


	/* SECTION methods */

	/** {@inheritDoc} */
	@Override
	public Set<Entry<K, V>> entrySet() {
		Set<Entry<K, V>> r = super.entrySet();

		Reference<?> p = queue.poll();
		while (p != null) p = queue.poll();

		r.removeIf(t -> t.getValue().isEnqueued());
		return r;
	}

	/** {@inheritDoc} */
	@SuppressWarnings("ResultOfMethodCallIgnored")
	@Override
	public V put(K key, V value) {
		entrySet();
		return super.put(key, value);
	}


	/** {@inheritDoc} */
	@Override
	@OverridingStatus(group = GROUP, when = When.NEVER)
	public T toImmutable() { return castUnchecked((Object) new Immutable<>(this)); }


	/** {@inheritDoc} */
	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public String toString() { return getToStringString(this, super.toString(),
			new Object[]{"queue", getQueue()}); }

	/** {@inheritDoc} */
	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public boolean equals(Object o) { return isEquals(this, o, super.equals(o),
			t -> getQueue().equals(t.getQueue())); }

	/** {@inheritDoc} */
	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public int hashCode() { return getHashCode(this, super.hashCode(), getQueue()); }

	/** {@inheritDoc} */
	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T clone() {
		T r = super.clone();
		r.queue = tryClone(queue);
		return r;
	}


	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<K, V extends Reference<?>, T extends Immutable<K, V, T>> extends ReferenceMap<K, V, T> {
		/* SECTION constructors */

		public Immutable(/* REMARKS mutable */ Map<K, V> delegate, ReferenceQueue<?> queue) { super(delegate, queue); }

		public Immutable(ReferenceMap<K, V, ?> c) { this(c.getDelegate(), c.getQueue()); }


		/* SECTION getters & setters */

		/** {@inheritDoc} */
		@Override
		protected ImmutableMap<K, V> getDelegate() { return ImmutableMap.copyOf(super.getDelegate()); }

		/** {@inheritDoc} */
		@Override
		protected void setDelegate(Map<K, V> delegate) { throw rejectUnsupportedOperation(); }

		/** {@inheritDoc} */
		@Override
		protected void setQueue(ReferenceQueue<?> queue) { throw rejectUnsupportedOperation(); }


		/* SECTION methods */

		/** {@inheritDoc} */
		@Override
		public V put(K key, V value) { throw rejectUnsupportedOperation(); }


		/** {@inheritDoc} */
		@Override
		@OverridingStatus(group = GROUP, when = When.NEVER)
		public final T toImmutable() { return castUnchecked(this); }

		/** {@inheritDoc} */
		@Override
		@OverridingStatus(group = GROUP, when = When.NEVER)
		public final boolean isImmutable() { return true; }
	}
}
