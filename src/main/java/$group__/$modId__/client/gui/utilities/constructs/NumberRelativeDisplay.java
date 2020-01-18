package $group__.$modId__.client.gui.utilities.constructs;

import $group__.$modId__.utilities.constructs.classes.concrete.NumberRelative;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.meta.When;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictEquals.isEquals;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.helpers.Miscellaneous.Casts.castUnchecked;
import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.variables.Constants.GROUP;
import static $group__.$modId__.utilities.variables.References.Client.getResolution;

@SideOnly(Side.CLIENT)
public abstract class NumberRelativeDisplay<T extends NumberRelativeDisplay<T>> extends NumberRelative<T> {
	/* SECTION variables */

	protected final Consumer<? extends T> updater;
	protected final AtomicBoolean update;


	/* SECTION constructors */

	public NumberRelativeDisplay(Number value, Consumer<? extends T> updater, @Nullable Number offset, AtomicBoolean update) {
		super(value, null, offset);
		this.updater = updater;
		this.update = update;

		updater.accept(castUnchecked(this));
	}

	public NumberRelativeDisplay(Number value, Consumer<? extends T> updater, @Nullable Number offset) { this(value, updater, offset, new AtomicBoolean(true)); }

	public NumberRelativeDisplay(NumberRelativeDisplay<? extends T> c) { this(c.getValue(), c.getUpdater(), c.getOffset(), c.getUpdate()); }


	/* SECTION getters & setters */

	public Consumer<? extends T> getUpdater() { return updater; }

	public AtomicBoolean getUpdate() { return update; }


	/** {@inheritDoc} */
	@Nonnull
	@Override
	public Number getParent() {
		if (getUpdate().get()) getUpdater().accept(castUnchecked(this));
		assert super.getParent() != null;
		return super.getParent();
	}

	/** {@inheritDoc} */
	@Override
	public void setParent(@Nullable Number parent) { throw rejectUnsupportedOperation(); }


	/* SECTION methods */

	/** {@inheritDoc} */
	@Override
	public String toString() { return getToStringString(this, super.toString(),
			new Object[]{"updater", getUpdater()},
			new Object[]{"update", getUpdate()}); }

	/** {@inheritDoc} */
	@Override
	public int hashCode() { return getHashCode(this, super.hashCode(), getUpdater(), getUpdate()); }

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object o) { return isEquals(this, o, super.equals(o),
			t -> getUpdater().equals(t.getUpdater()),
			t -> getUpdate().equals(t.getUpdate())); }


	/* SECTION static classes */

	public static class X<T extends X<T>> extends NumberRelativeDisplay<T> {
		/* SECTION constructors */

		public X(Number value, @Nullable Number offset, AtomicBoolean update) { super(value, t -> t.parent = getResolution().getScaledWidth_double(), offset, update); }

		public X(Number value, @Nullable Number offset) {
			this(value, offset, new AtomicBoolean(true));
		}

		public X(Number value, AtomicBoolean update) { this(value, null, update); }

		public X(Number value) { this(value, new AtomicBoolean(true)); }


		/* SECTION methods */

		/** {@inheritDoc} */
		@Override
		public T toImmutable() { return castUnchecked((Object) new Immutable<>(this)); }


		/* SECTION static classes */

		@javax.annotation.concurrent.Immutable
		public static class Immutable<T extends Immutable<T>> extends X<T> {
			/* SECTION constructors */

			public Immutable(Number value, @Nullable Number offset, AtomicBoolean update) { super(value, offset, update); }

			public Immutable(Number value, @Nullable Number offset) { this(value, offset, new AtomicBoolean(true)); }

			public Immutable(Number value) { this(value, null); }

			public Immutable(X<?> c) { this(c.getValue(), c.getOffset(), c.getUpdate()); }


			/* SECTION getters & setters */

			/** {@inheritDoc} */
			@Override
			public void setValue(Number value) { throw rejectUnsupportedOperation();}

			/** {@inheritDoc} */
			@Override
			public void setOffset(@Nullable Number offset) { throw rejectUnsupportedOperation();}


			/* SECTION methods */

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


	public static class Y<T extends Y<T>> extends NumberRelativeDisplay<T> {
		/* SECTION constructors */

		public Y(Number value, @Nullable Number offset, AtomicBoolean update) { super(value, t -> t.parent = getResolution().getScaledHeight_double(), offset, update); }

		public Y(Number value, @Nullable Number offset) { this(value, offset, new AtomicBoolean(true)); }

		public Y(Number value, AtomicBoolean update) { this(value, null, update); }

		public Y(Number value) { this(value, new AtomicBoolean(true)); }


		/* SECTION methods */

		/** {@inheritDoc} */
		@Override
		public T toImmutable() { return castUnchecked((Object) new Immutable<>(this)); }


		/* SECTION static classes */

		@javax.annotation.concurrent.Immutable
		public static class Immutable<T extends Immutable<T>> extends Y<T> {
			/* SECTION constructors */

			public Immutable(Number value, @Nullable Number offset, AtomicBoolean update) { super(value, offset, update); }

			public Immutable(Number value, @Nullable Number offset) { this(value, offset, new AtomicBoolean(true)); }

			public Immutable(Number value) { this(value, null); }

			public Immutable(X<?> c) { this(c.getValue(), c.getOffset(), c.getUpdate()); }


			/* SECTION getters & setters */

			/** {@inheritDoc} */
			@Override
			public void setValue(Number value) { throw rejectUnsupportedOperation();}

			/** {@inheritDoc} */
			@Override
			public void setOffset(@Nullable Number offset) { throw rejectUnsupportedOperation();}


			/* SECTION methods */

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
}
