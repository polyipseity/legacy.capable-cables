package $group__.$modId__.client.gui.utilities.constructs;

import $group__.$modId__.utilities.constructs.classes.concrete.NumberRelative;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

import static $group__.$modId__.utilities.constructs.interfaces.basic.IImmutablizable.tryToImmutableUnboxed;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IImmutablizable.tryToImmutableUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Optionals.unboxOptional;
import static $group__.$modId__.utilities.helpers.Throwables.rejectArguments;
import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.variables.Constants.GROUP;
import static $group__.$modId__.utilities.variables.Globals.Client.getResolution;
import static $group__.$modId__.utilities.variables.Globals.Client.registerPreInitGuiListener;

@SideOnly(Side.CLIENT)
public abstract class NumberRelativeDisplay<T extends NumberRelativeDisplay<T>> extends NumberRelative<T> {
	/* SECTION static variables */

	private static final long serialVersionUID = 1046506854640455032L;


	/* SECTION variables */

	protected BiConsumer<? super GuiScreenEvent.InitGuiEvent.Pre, ? super T> updater;
	protected AtomicBoolean update;


	/* SECTION constructors */

	public NumberRelativeDisplay(Number value, Number parent, @Nullable Number offset, BiConsumer<? super GuiScreenEvent.InitGuiEvent.Pre, ? super T> updater) { this(value, parent, offset, updater, new AtomicBoolean(true)); }

	public NumberRelativeDisplay(Number value, Number parent, @Nullable Number offset, BiConsumer<? super GuiScreenEvent.InitGuiEvent.Pre, ? super T> updater, AtomicBoolean update) {
		super(value, parent, offset);
		this.updater = updater;
		this.update = update;
		initializeInstance(this);
	}

	public NumberRelativeDisplay(NumberRelativeDisplay<T> copy) { this(copy.getValue(), copy.getParent().orElseThrow(() -> rejectArguments(copy)), unboxOptional(copy.getOffset()), copy.getUpdater(), copy.getUpdate()); }


	/* SECTION static methods */

	private static <T extends NumberRelativeDisplay<?>> T initializeInstance(T t) {
		registerPreInitGuiListener(t, castUncheckedUnboxedNonnull(t.getUpdater()));
		return t;
	}


	/* SECTION getters & setters */

	public BiConsumer<? super GuiScreenEvent.InitGuiEvent.Pre, ? super T> getUpdater() { return updater; }

	public void setUpdater(BiConsumer<? super GuiScreenEvent.InitGuiEvent.Pre, ? super T> updater) {
		registerPreInitGuiListener(this, castUncheckedUnboxedNonnull(this.updater = updater));
		markDirty();
	}

	public AtomicBoolean getUpdate() { return update; }

	public void setUpdate(AtomicBoolean update) {
		this.update = update;
		markDirty();
	}

	@Override
	@Deprecated
	public void setParent(@Nullable Number parent) { throw rejectUnsupportedOperation(); }


	/* SECTION methods */

	@Override
	@OverridingMethodsMustInvokeSuper
	public T clone() { return initializeInstance(super.clone()); }


	/* SECTION static classes */

	public static class X<T extends X<T>> extends NumberRelativeDisplay<T> {
		/* SECTION static variables */

		private static final long serialVersionUID = 4079465691262621348L;


		/* SECTION constructors */

		public X(Number value, @Nullable Number offset) { this(value, offset, new AtomicBoolean(true)); }

		public X(Number value, @Nullable Number offset, AtomicBoolean update) { this(value, getResolution().getScaledWidth_double(), offset, update); }

		public X(Number value, Number parent, @Nullable Number offset, AtomicBoolean update) {
			super(value, parent, offset, (e, t) -> {
				if (t.getUpdate().get()) {
					t.parent = getResolution().getScaledWidth_double();
					t.markDirty();
				}
			}, update);
		}

		public X(Number value) { this(value, new AtomicBoolean(true)); }

		public X(Number value, AtomicBoolean update) { this(value, null, update); }

		public X(X<?> copy) { this(copy.getValue(), copy.getParent().orElseThrow(() -> rejectArguments(copy)), unboxOptional(copy.getOffset()), copy.getUpdate()); }


		/* SECTION methods */

		@Override
		public T toImmutable() { return castUncheckedUnboxedNonnull((Object) new Immutable<>(this)); }

		@Override
		public boolean isImmutable() { return false; }


		/* SECTION static classes */

		@javax.annotation.concurrent.Immutable
		public static class Immutable<T extends Immutable<T>> extends X<T> {
			/* SECTION static variables */

			private static final long serialVersionUID = -6713655815326599341L;


			/* SECTION constructors */

			public Immutable(Number value, @Nullable Number offset) { this(value, offset, new AtomicBoolean(true)); }

			public Immutable(Number value, @Nullable Number offset, AtomicBoolean update) { this(value, getResolution().getScaledWidth_double(), offset, update); }

			public Immutable(Number value, Number parent, @Nullable Number offset, AtomicBoolean update) { super(tryToImmutableUnboxedNonnull(value), tryToImmutableUnboxedNonnull(parent), tryToImmutableUnboxed(offset), tryToImmutableUnboxedNonnull(update)); }

			public Immutable(Number value) { this(value, new AtomicBoolean(true)); }

			public Immutable(Number value, AtomicBoolean update) { this(value, null, update); }

			public Immutable(X<?> copy) { this(copy.getValue(), copy.getParent().orElseThrow(() -> rejectArguments(copy)), unboxOptional(copy.getOffset()), copy.getUpdate()); }


			/* SECTION getters & setters */

			@Override
			@Deprecated
			public void setValue(Number value) { throw rejectUnsupportedOperation(); }

			@Override
			@Deprecated
			public void setOffset(@Nullable Number offset) { throw rejectUnsupportedOperation(); }

			@Override
			@Deprecated
			public void setUpdater(BiConsumer<? super GuiScreenEvent.InitGuiEvent.Pre, ? super T> updater) { throw rejectUnsupportedOperation(); }

			@Override
			@Deprecated
			public void setUpdate(AtomicBoolean update) { throw rejectUnsupportedOperation(); }


			/* SECTION methods */

			@Override
			@OverridingStatus(group = GROUP)
			public final T toImmutable() { return castUncheckedUnboxedNonnull(this); }

			@Override
			@OverridingStatus(group = GROUP)
			public final boolean isImmutable() { return true; }
		}
	}


	public static class Y<T extends Y<T>> extends NumberRelativeDisplay<T> {
		/* SECTION static variables */

		private static final long serialVersionUID = 838852564853210693L;


		/* SECTION constructors */

		public Y(Number value, @Nullable Number offset) { this(value, offset, new AtomicBoolean(true)); }

		public Y(Number value, @Nullable Number offset, AtomicBoolean update) { this(value, getResolution().getScaledHeight_double(), offset, update); }

		public Y(Number value, Number parent, @Nullable Number offset, AtomicBoolean update) {
			super(value, parent, offset, (e, t) -> {
				if (t.getUpdate().get()) {
					t.parent = getResolution().getScaledHeight_double();
					t.markDirty();
				}
			}, update);
		}

		public Y(Number value) { this(value, new AtomicBoolean(true)); }

		public Y(Number value, AtomicBoolean update) { this(value, null, update); }

		public Y(Y<?> copy) { this(copy.getValue(), copy.getParent().orElseThrow(() -> rejectArguments(copy)), unboxOptional(copy.getOffset()), copy.getUpdate()); }


		/* SECTION methods */

		@Override
		public T toImmutable() { return castUncheckedUnboxedNonnull((Object) new Immutable<>(this)); }

		@Override
		public boolean isImmutable() { return false; }


		/* SECTION static classes */

		@javax.annotation.concurrent.Immutable
		public static class Immutable<T extends Immutable<T>> extends Y<T> {
			/* SECTION static variables */

			private static final long serialVersionUID = 6434701388754540645L;


			/* SECTION constructors */

			public Immutable(Number value, @Nullable Number offset) { this(value, offset, new AtomicBoolean(true)); }

			public Immutable(Number value, @Nullable Number offset, AtomicBoolean update) { this(value, getResolution().getScaledWidth_double(), offset, update); }

			public Immutable(Number value, Number parent, @Nullable Number offset, AtomicBoolean update) { super(tryToImmutableUnboxedNonnull(value), tryToImmutableUnboxedNonnull(parent), tryToImmutableUnboxed(offset), tryToImmutableUnboxedNonnull(update)); }

			public Immutable(Number value) { this(value, new AtomicBoolean(true)); }

			public Immutable(Number value, AtomicBoolean update) { this(value, null, update); }

			public Immutable(Y<?> copy) { this(copy.getValue(), copy.getParent().orElseThrow(() -> rejectArguments(copy)), unboxOptional(copy.getOffset()), copy.getUpdate()); }


			/* SECTION getters & setters */

			@Override
			@Deprecated
			public void setValue(Number value) { throw rejectUnsupportedOperation(); }

			@Override
			@Deprecated
			public void setOffset(@Nullable Number offset) { throw rejectUnsupportedOperation(); }

			@Override
			@Deprecated
			public void setUpdater(BiConsumer<? super GuiScreenEvent.InitGuiEvent.Pre, ? super T> updater) { throw rejectUnsupportedOperation(); }

			@Override
			@Deprecated
			public void setUpdate(AtomicBoolean update) { throw rejectUnsupportedOperation(); }


			/* SECTION methods */

			@Override
			@OverridingStatus(group = GROUP)
			public final T toImmutable() { return castUncheckedUnboxedNonnull(this); }

			@Override
			@OverridingStatus(group = GROUP)
			public final boolean isImmutable() { return true; }
		}
	}
}
