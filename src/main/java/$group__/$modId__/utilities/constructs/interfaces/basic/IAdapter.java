package $group__.$modId__.utilities.constructs.interfaces.basic;

import $group__.$modId__.utilities.constructs.interfaces.IStructureCloneable;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.annotation.meta.When;
import java.util.Objects;
import java.util.Optional;

import static $group__.$modId__.utilities.constructs.interfaces.basic.IImmutablizable.tryToImmutableUnboxed;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IImmutablizable.tryToImmutableUnboxedNonnull;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.ICloneable.tryCloneUnboxedNonnull;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictEquals.isEqual;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictToString.getToStringString;
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

	@Immutable
	interface IImmutable<V, T extends IImmutable<V, T>> extends IAdapter<V, T> {
		/* SECTION methods */

		@Override
		default void set(V value) { throw rejectUnsupportedOperation(); }


		@Override
		@OverridingStatus(group = GROUP)
		default T toImmutable() { return castUncheckedUnboxedNonnull(this); }

		@Override
		@OverridingStatus(group = GROUP)
		default boolean isImmutable() { return true; }


		/* SECTION static classes */

		@Immutable
		class Impl<V, T extends Impl<V, T>> extends IAdapter.Impl<V, T> implements IImmutable<V, T> {
			/* SECTION constructors */

			public Impl(IAdapter<? extends V, ?> copy) { this(copy.get()); }

			public Impl(V value) { super(tryToImmutableUnboxedNonnull(value)); }


			/* SECTION methods */

			@Override
			@OverridingStatus(group = GROUP, when = When.NEVER)
			public final T toImmutable() { return IImmutable.super.toImmutable(); }

			@Override
			@OverridingStatus(group = GROUP, when = When.NEVER)
			public final boolean isImmutable() { return IImmutable.super.isImmutable(); }

			@Override
			public void set(V value) { IImmutable.super.set(value); }
		}
	}

	interface IOptional<V, T extends IOptional<V, T>> extends IAdapter<Optional<? extends V>, T> {
		/* SECTION getters & setters */

		@Nullable
		default V getUnboxed() { return unboxOptional(get()); }

		default void setUnboxed(@Nullable V value) { set(Optional.ofNullable(value)); }


		/* SECTION static classes */

		@Immutable
		interface IImmutable<V, T extends IImmutable<V, T>> extends IOptional<V, T> {
			/* SECTION methods */

			@Override
			default void set(Optional<? extends V> value) { throw rejectUnsupportedOperation(); }

			@Override
			default void setUnboxed(@Nullable V value) { throw rejectUnsupportedOperation(); }


			@Override
			@OverridingStatus(group = GROUP)
			default T toImmutable() { return castUncheckedUnboxedNonnull(this); }

			@Override
			@OverridingStatus(group = GROUP)
			default boolean isImmutable() { return true; }


			/* SECTION static classes */

			@Immutable
			class Impl<V, T extends Impl<V, T>> extends IOptional.Impl<V, T> implements IOptional.IImmutable<V, T> {
				/* SECTION constructors */

				public Impl(IOptional<? extends V, ?> copy) { this(copy.getUnboxed()); }

				public Impl(@Nullable V value) { super(tryToImmutableUnboxed(value)); }


				/* SECTION methods */

				@Override
				public void set(Optional<? extends V> value) { IOptional.IImmutable.super.set(value); }

				@Override
				public void setUnboxed(@Nullable V value) { IOptional.IImmutable.super.setUnboxed(value); }


				@Override
				@OverridingStatus(group = GROUP, when = When.NEVER)
				public final T toImmutable() { return IOptional.IImmutable.super.toImmutable(); }

				@Override
				@OverridingStatus(group = GROUP, when = When.NEVER)
				public final boolean isImmutable() { return IOptional.IImmutable.super.isImmutable(); }
			}
		}

		class Impl<V, T extends Impl<V, T>> extends IAdapter.Impl<Optional<? extends V>, T> implements IOptional<V, T> {
			/* SECTION constructors */

			public Impl(IOptional<? extends V, ?> copy) { this(copy.getUnboxed()); }

			public Impl(@Nullable V value) { super(Optional.ofNullable(value)); }


			/* SECTION methods */

			@Override
			public T toImmutable() { return castUncheckedUnboxedNonnull(new IOptional.IImmutable.Impl<>(this)); }

			@Override
			public boolean isImmutable() { return false; }
		}
	}

	class Impl<V, T extends Impl<V, T>> implements IAdapter<V, T> {
		/* SECTION variables */

		protected V value;


		/* SECTION constructors */

		public Impl(IAdapter<? extends V, ?> copy) { this(copy.get()); }

		public Impl(V value) { this.value = value; }


		/* SECTION methods */

		@Override
		public T toImmutable() { return castUncheckedUnboxedNonnull(new IImmutable.Impl<>(this)); }

		@Override
		public boolean isImmutable() { return false; }

		@Override
		@OverridingStatus(group = GROUP, when = When.MAYBE)
		public int hashCode() {
			return isImmutable() ? getHashCode(this, super.hashCode(), get()) : getHashCode(this, super.hashCode());
		}

		@Override
		@OverridingStatus(group = GROUP, when = When.MAYBE)
		public boolean equals(Object o) {
			return isImmutable() ? isEqual(this, o, super.equals(o),
					t -> Objects.equals(get(), t.get())) : isEqual(this, o, super.equals(o));
		}

		@Override
		@OverridingStatus(group = GROUP, when = When.MAYBE)
		public T clone() {
			T r;
			try { r = castUncheckedUnboxedNonnull(super.clone()); } catch (CloneNotSupportedException e) {
				throw unexpected(e);
			}
			r.value = tryCloneUnboxedNonnull(value);
			return r;
		}

		@Override
		@OverridingStatus(group = GROUP, when = When.MAYBE)
		public String toString() {
			return getToStringString(this, super.toString(),
					new Object[]{"value", get()});
		}

		@Override
		public V get() { return value; }

		@Override
		public void set(V value) { this.value = value; }
	}
}
