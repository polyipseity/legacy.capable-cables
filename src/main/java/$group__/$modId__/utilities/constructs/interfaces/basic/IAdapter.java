package $group__.$modId__.utilities.constructs.interfaces.basic;

import $group__.$modId__.utilities.constructs.interfaces.IStructureCloneable;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.meta.When;
import java.util.Objects;

import static $group__.$modId__.utilities.constructs.interfaces.basic.IImmutablizable.tryToImmutable;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IImmutablizable.tryToImmutableNonnull;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictEquals.isEquals;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.ICloneable.tryClone;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.ICloneable.tryCloneNonnull;
import static $group__.$modId__.utilities.helpers.Casts.castUnchecked;
import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.helpers.Throwables.unexpected;
import static $group__.$modId__.utilities.variables.Constants.GROUP;

public interface IAdapter<V, T extends IAdapter<V, T>> extends IStructureCloneable<T> {
	/* SECTION getters & setters */

	@Nullable
	V get();

	void set(V value);


	/* SECTION static classes */

	class Impl<V, T extends Impl<V, T>> implements IAdapter<V, T> {
		/* SECTION variables */

		@Nullable
		protected V value;


		/* SECTION constructors */

		public Impl(@Nullable V value) { this.value = value; }

		public Impl(IAdapter<? extends V, ?> copy) { this(copy.get()); }


		/* SECTION methods */

		@Override
		@Nullable
		public V get() { return value; }

		@Override
		public void set(@Nullable V value) { this.value = value; }


		@Override
		public T toImmutable() { return castUnchecked(new Immutable<>(this)); }

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
			try { r = castUnchecked(super.clone()); } catch (CloneNotSupportedException e) { throw unexpected(e); }
			r.value = tryClone(value);
			return r;
		}


		/* SECTION static classes */

		@javax.annotation.concurrent.Immutable
		public static class Immutable<V, T extends Immutable<V, T>> extends Impl<V, T> {
			/* SECTION constructors */

			public Immutable(@Nullable V value) { super(tryToImmutable(value)); }

			public Immutable(IAdapter<? extends V, ?> copy) { this(copy.get()); }


			/* SECTION methods */

			@Override
			public void set(@Nullable V value) { throw rejectUnsupportedOperation(); }


			@Override
			@OverridingStatus(group = GROUP, when = When.NEVER)
			public final T toImmutable() { return castUnchecked(this); }

			@Override
			@OverridingStatus(group = GROUP, when = When.NEVER)
			public final boolean isImmutable() { return true; }
		}
	}


	interface INonnull<V, T extends INonnull<V, T>> extends IAdapter<V, T> {
		/* SECTION getters & setters */

		@Nonnull
		@Override
		V get();


		/* SECTION static classes */

		class Impl<V, T extends Impl<V, T>> implements INonnull<V, T> {
			/* SECTION variables */

			protected V value;


			/* SECTION constructors */

			public Impl(V value) { this.value = value; }

			public Impl(IAdapter.INonnull<? extends V, ?> copy) { this(copy.get()); }


			/* SECTION methods */

			@Nonnull
			@Override
			public V get() { return value; }

			@Override
			public void set(V value) { this.value = value; }


			@Override
			public T toImmutable() { return castUnchecked(new IAdapter.Impl.Immutable<>(this)); }

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
				try { r = castUnchecked(super.clone()); } catch (CloneNotSupportedException e) { throw unexpected(e); }
				r.value = tryCloneNonnull(value);
				return r;
			}


			/* SECTION static classes */

			@javax.annotation.concurrent.Immutable
			public static class Immutable<V, T extends Immutable<V, T>> extends INonnull.Impl<V, T> {
				/* SECTION constructors */

				public Immutable(V value) { super(tryToImmutableNonnull(value)); }

				public Immutable(IAdapter.INonnull<? extends V, ?> copy) { this(copy.get()); }


				/* SECTION methods */

				@Override
				public void set(V value) { throw rejectUnsupportedOperation(); }


				@Override
				@OverridingStatus(group = GROUP, when = When.NEVER)
				public final T toImmutable() { return castUnchecked(this); }

				@Override
				@OverridingStatus(group = GROUP, when = When.NEVER)
				public final boolean isImmutable() { return true; }
			}
		}
	}
}
