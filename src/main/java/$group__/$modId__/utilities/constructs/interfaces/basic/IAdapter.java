package $group__.$modId__.utilities.constructs.interfaces.basic;

import $group__.$modId__.utilities.constructs.interfaces.IStructureCloneable;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;

import javax.annotation.Nullable;
import javax.annotation.meta.When;
import java.util.Objects;
import java.util.Optional;

import static $group__.$modId__.utilities.constructs.interfaces.basic.IImmutablizable.tryToImmutableUnboxed;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IImmutablizable.tryToImmutableUnboxedNonnull;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictEquals.isEquals;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.ICloneable.tryCloneUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Optionals.unboxOptional;
import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.helpers.Throwables.unexpected;
import static $group__.$modId__.utilities.variables.Constants.GROUP;

public interface IAdapter<V, T extends IAdapter<V, T>> extends IStructureCloneable<T> {
	/* SECTION getters & setters */

	V get();

	void set(V value);


	/* SECTION static classes */

	class Impl<V, T extends Impl<V, T>> implements IAdapter<V, T> {
		/* SECTION variables */

		protected V value;


		/* SECTION constructors */

		public Impl(V value) { this.value = value; }

		public Impl(IAdapter<? extends V, ?> copy) { this(copy.get()); }


		/* SECTION methods */

		@Override
		public V get() { return value; }

		@Override
		public void set(V value) { this.value = value; }


		@Override
		public T toImmutable() { return castUncheckedUnboxedNonnull(new Immutable<>(this)); }

		@Override
		public boolean isImmutable() { return false; }


		@Override
		@OverridingStatus(group = GROUP, when = When.MAYBE)
		public String toString() { return getToStringString(this, super.toString(),
				new Object[]{"value", get()}); }

		@Override
		@OverridingStatus(group = GROUP, when = When.MAYBE)
		public int hashCode() { return getHashCode(this, super.hashCode(), get()); }

		@Override
		@OverridingStatus(group = GROUP, when = When.MAYBE)
		public boolean equals(Object o) { return isEquals(this, o, super.equals(o),
					t -> Objects.equals(get(), t.get())); }

		@Override
		@OverridingStatus(group = GROUP, when = When.MAYBE)
		public T clone() {
			T r;
			try { r = castUncheckedUnboxedNonnull(super.clone()); } catch (CloneNotSupportedException e) { throw unexpected(e); }
			r.value = tryCloneUnboxedNonnull(value);
			return r;
		}


		/* SECTION static classes */

		@javax.annotation.concurrent.Immutable
		public static class Immutable<V, T extends Immutable<V, T>> extends Impl<V, T> {
			/* SECTION constructors */

			public Immutable(V value) { super(tryToImmutableUnboxedNonnull(value)); }

			public Immutable(IAdapter<? extends V, ?> copy) { this(copy.get()); }


			/* SECTION methods */

			@Override
			public void set(V value) { throw rejectUnsupportedOperation(); }


			@Override
			@OverridingStatus(group = GROUP, when = When.NEVER)
			public final T toImmutable() { return castUncheckedUnboxedNonnull(this); }

			@Override
			@OverridingStatus(group = GROUP, when = When.NEVER)
			public final boolean isImmutable() { return true; }
		}
	}


	interface IOptional<V, T extends IOptional<V, T>> extends IAdapter<Optional<? extends V>, T> {
		/* SECTION getters & setters */

		@Nullable
		default V getUnboxed() { return unboxOptional(get()); }

		default void setUnboxed(@Nullable V value) { set(Optional.ofNullable(value)); }


		/* SECTION static classes */

		class Impl<V, T extends Impl<V, T>> extends IAdapter.Impl<Optional<? extends V>, T> implements IOptional<V, T> {
			/* SECTION constructors */

			public Impl(@Nullable V value) { super(Optional.ofNullable(value)); }

			public Impl(IAdapter.IOptional<? extends V, ?> copy) { this(copy.getUnboxed()); }

			public Impl(IAdapter<? extends V, ?> copy) { this(copy.get()); }


			/* SECTION methods */

			@Override
			public T toImmutable() { return castUncheckedUnboxedNonnull(new IAdapter.Impl.Immutable<>(this)); }

			@Override
			public boolean isImmutable() { return false; }


			/* SECTION static classes */

			@javax.annotation.concurrent.Immutable
			public static class Immutable<V, T extends Immutable<V, T>> extends IOptional.Impl<V, T> {
				/* SECTION constructors */

				public Immutable(@Nullable V value) { super(tryToImmutableUnboxed(value)); }

				public Immutable(IAdapter.IOptional<? extends V, ?> copy) { this(copy.getUnboxed()); }

				public Immutable(IAdapter<? extends V, ?> copy) { this(copy.get()); }


				/* SECTION methods */

				@Override
				public void set(Optional<? extends V> value) { throw rejectUnsupportedOperation(); }

				@Override
				public void setUnboxed(@Nullable V value) { throw rejectUnsupportedOperation(); }


				@Override
				@OverridingStatus(group = GROUP, when = When.NEVER)
				public final T toImmutable() { return castUncheckedUnboxedNonnull(this); }

				@Override
				@OverridingStatus(group = GROUP, when = When.NEVER)
				public final boolean isImmutable() { return true; }
			}
		}
	}
}
