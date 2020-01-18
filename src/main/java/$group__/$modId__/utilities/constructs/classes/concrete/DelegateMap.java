package $group__.$modId__.utilities.constructs.classes.concrete;

import $group__.$modId__.utilities.constructs.interfaces.IStructure;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import $group__.$modId__.utilities.helpers.Throwables;
import com.google.common.collect.ImmutableMap;

import javax.annotation.meta.When;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictEquals.isEquals;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.ICloneable.tryClone;
import static $group__.$modId__.utilities.helpers.Miscellaneous.Casts.castUnchecked;
import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.variables.Constants.GROUP;

public class DelegateMap<K, V, T extends DelegateMap<K, V, T>> extends AbstractMap<K, V> implements IStructure<T> {
	/* SECTION variables */

	protected Map<K, V> delegate;


	/* SECTION constructors */

	protected DelegateMap(Map<K, V> delegate) { this.delegate = delegate; }

	public DelegateMap(DelegateMap<K, V, ?> c) { this(c.getDelegate()); }


	/* SECTION getters & setters */

	protected Map<K, V> getDelegate() { return delegate; }

	protected void setDelegate(Map<K, V> delegate) { this.delegate = delegate; }


	/* SECTION methods */

	/** {@inheritDoc} */
	@Override
	public Set<Entry<K, V>> entrySet() { return getDelegate().entrySet(); }

	/** {@inheritDoc} */
	@Override
	public V put(K key, V value) { return getDelegate().put(key, value); }


	/** {@inheritDoc} */
	@Override
	@OverridingStatus(group = GROUP, when = When.NEVER)
	public T toImmutable() { return castUnchecked(new Immutable<>(this)); }


	/** {@inheritDoc} */
	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public String toString() { return getToStringString(this, super.toString(),
			new Object[]{"delegate", getDelegate()}); }

	/** {@inheritDoc} */
	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public boolean equals(Object o) { return isEquals(this, o, super.equals(o),
			t -> getDelegate().equals(t.getDelegate())); }

	/** {@inheritDoc} */
	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public int hashCode() { return getHashCode(this, super.hashCode(), getDelegate()); }

	/** {@inheritDoc} */
	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T clone() {
		T r;
		try { r = castUnchecked(super.clone()); } catch (CloneNotSupportedException e) { throw Throwables.unexpectedThrowable(e); }
		r.delegate = tryClone(delegate);
		return r;
	}


	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<K, V, T extends Immutable<K, V, T>> extends DelegateMap<K, V, T> {
		/* SECTION constructors */

		protected Immutable(Map<K, V> delegate) { super(ImmutableMap.copyOf(delegate)); }

		public Immutable(DelegateMap<K, V, ?> c) { this(c.getDelegate()); }


		/* SECTION getters & setters */

		/** {@inheritDoc} */
		@Override
		protected void setDelegate(Map<K, V> delegate) { throw rejectUnsupportedOperation(); }


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
