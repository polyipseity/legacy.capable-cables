package etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs;

import etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.NumberRelative;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IStrictEquals.isEquals;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IStrictHashCode.getHashCode;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IStrictToString.getToStringString;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Miscellaneous.Casts.castUnchecked;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.variables.References.Client.getResolution;

@SideOnly(Side.CLIENT)
public abstract class NumberRelativeDisplay<N extends NumberRelativeDisplay<N>> extends NumberRelative<N> {
	/* SECTION variables */

	protected final Consumer<? extends N> updater;
	protected final AtomicBoolean update;


	/* SECTION constructors */

	public NumberRelativeDisplay(Number value, Consumer<? extends N> updater, @Nullable Number offset, AtomicBoolean update) {
		super(value, null, offset);
		this.updater = updater;
		this.update = update;

		updater.accept(castUnchecked(this));
	}

	public NumberRelativeDisplay(Number value, Consumer<? extends N> updater, @Nullable Number offset) { this(value, updater, offset, new AtomicBoolean(true)); }

	public NumberRelativeDisplay(NumberRelativeDisplay<? extends N> c) { this(c.getValue(), c.getUpdater(), c.getOffset(), c.getUpdate()); }


	/* SECTION getters & setters */

	public Consumer<? extends N> getUpdater() { return updater; }

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

	public static class X<N extends X<N>> extends NumberRelativeDisplay<N> {
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
		public N toImmutable() { return castUnchecked((Object) new Immutable<>(this)); }


		/* SECTION static classes */

		@javax.annotation.concurrent.Immutable
		public static class Immutable<N extends Immutable<N>> extends X<N> {
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
			public N toImmutable() { return castUnchecked(this); }

			/** {@inheritDoc} */
			@Override
			public boolean isImmutable() { return true; }
		}
	}


	public static class Y<N extends Y<N>> extends NumberRelativeDisplay<N> {
		/* SECTION constructors */

		public Y(Number value, @Nullable Number offset, AtomicBoolean update) { super(value, t -> t.parent = getResolution().getScaledHeight_double(), offset, update); }

		public Y(Number value, @Nullable Number offset) { this(value, offset, new AtomicBoolean(true)); }

		public Y(Number value, AtomicBoolean update) { this(value, null, update); }

		public Y(Number value) { this(value, new AtomicBoolean(true)); }


		/* SECTION methods */

		/** {@inheritDoc} */
		@Override
		public N toImmutable() { return castUnchecked((Object) new Immutable<>(this)); }


		/* SECTION static classes */

		@javax.annotation.concurrent.Immutable
		public static class Immutable<N extends Immutable<N>> extends Y<N> {
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
			public N toImmutable() { return castUnchecked(this); }

			/** {@inheritDoc} */
			@Override
			public boolean isImmutable() { return true; }
		}
	}
}
