package $group__.$modId__.traits.basic;

import $group__.$modId__.annotations.OverridingStatus;
import $group__.$modId__.traits.IStructureCloneable;
import $group__.$modId__.traits.extensions.ICloneable;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.concurrent.Immutable;
import javax.annotation.meta.When;
import java.util.Optional;

import static $group__.$modId__.traits.basic.IImmutablizable.tryToImmutableUnboxed;
import static $group__.$modId__.traits.basic.IImmutablizable.tryToImmutableUnboxedNonnull;
import static $group__.$modId__.traits.extensions.IStrictEquals.isEqual;
import static $group__.$modId__.traits.extensions.IStrictHashCode.getHashCode;
import static $group__.$modId__.traits.extensions.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.specific.Optionals.unboxOptional;
import static $group__.$modId__.utilities.helpers.specific.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.variables.Constants.GROUP;

public interface IAdapter<V, T extends IAdapter<V, T>> extends IStructureCloneable<T> {
	/* SECTION getters & setters */

	V get();

	void set(V value);


	/* SECTION static classes */

	@Immutable
	interface IImmutable<V, T extends IImmutable<V, T>> extends IAdapter<V, T> {
		/* SECTION getters & setters */

		@Override
		@Deprecated
		default void set(V value) { throw rejectUnsupportedOperation(); }


		/* SECTION methods */

		@Override
		@OverridingStatus(group = GROUP, when = When.MAYBE)
		default T toImmutable() { return castUncheckedUnboxedNonnull(this); }

		@Override
		default boolean isImmutable() { return true; }


		/* SECTION static classes */

		@Immutable
		class Impl<V, T extends Impl<V, T>> extends IAdapter.Impl<V, T> implements IImmutable<V, T> {
			/* SECTION constructors */

			public Impl(IAdapter<? extends V, ?> copy) { this(copy.get()); }

			public Impl(V value) { super(tryToImmutableUnboxedNonnull(value, )); }


			/* SECTION getters & setters */

			@SuppressWarnings("deprecation")
			@Override
			@Deprecated
			public void set(V value) { IImmutable.super.set(value); }


			/* SECTION methods */

			@Override
			@OverridingStatus(group = GROUP)
			public final T toImmutable() { return IImmutable.super.toImmutable(); }

			@Override
			@OverridingStatus(group = GROUP)
			public final boolean isImmutable() { return IImmutable.super.isImmutable(); }
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
			/* SECTION getters & setters */

			@Override
			@Deprecated
			default void set(Optional<? extends V> value) { throw rejectUnsupportedOperation(); }

			@Override
			@Deprecated
			default void setUnboxed(@Nullable V value) { throw rejectUnsupportedOperation(); }


			/* SECTION methods */

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

				public Impl(@Nullable V value) { super(tryToImmutableUnboxed(value, )); }


				/* SECTION getters & setters */

				@SuppressWarnings("deprecation")
				@Override
				@Deprecated
				public void set(Optional<? extends V> value) { IOptional.IImmutable.super.set(value); }

				@SuppressWarnings("deprecation")
				@Override
				@Deprecated
				public void setUnboxed(@Nullable V value) { IOptional.IImmutable.super.setUnboxed(value); }


				/* SECTION methods */

				@Override
				@OverridingStatus(group = GROUP)
				public final T toImmutable() { return IOptional.IImmutable.super.toImmutable(); }

				@Override
				@OverridingStatus(group = GROUP)
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


		/* SECTION getters & setters */

		@Override
		public V get() { return value; }

		@Override
		public void set(V value) { this.value = value; }


		/* SECTION methods */

		@Override
		public T toImmutable() { return castUncheckedUnboxedNonnull(new IImmutable.Impl<>(this)); }

		@Override
		public boolean isImmutable() { return false; }


		@Override
		@OverridingStatus(group = GROUP)
		public final int hashCode() { return getHashCode(this, super::hashCode); }

		@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
		@Override
		@OverridingStatus(group = GROUP)
		public final boolean equals(Object o) { return isEqual(this, o, super::equals); }

		@SuppressWarnings("Convert2MethodRef")
		@Override
		@OverridingMethodsMustInvokeSuper
		@OverridingStatus(group = GROUP, when = When.MAYBE)
		public T clone() { return ICloneable.clone(() -> super.clone(), LOGGER); }

		@Override
		@OverridingStatus(group = GROUP)
		public final String toString() { return getToStringString(this, super.toString()); }
	}
}
