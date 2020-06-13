package $group__.client.gui.coordinates;

import $group__.Globals;
import $group__.logging.ILogging;
import $group__.utilities.NumberRelative;
import $group__.utilities.builders.BuilderNumberRelative;
import $group__.utilities.concurrent.IMutatorImmutablizable;
import $group__.utilities.helpers.specific.Optionals;
import $group__.utilities.helpers.specific.Throwables;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

import static $group__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;

@SideOnly(Side.CLIENT)
public abstract class NumberRelativeDisplay<T extends NumberRelativeDisplay<T>> extends NumberRelative<T> {
	private static final long serialVersionUID = 1046506854640455032L;


	protected BiConsumer<? super GuiScreenEvent.InitGuiEvent.Pre, ? super T> updater;
	protected AtomicBoolean update;


	protected NumberRelativeDisplay(Number value, Number parent, @Nullable Number offset, BiConsumer<?
			super GuiScreenEvent.InitGuiEvent.Pre, ? super T> updater, IMutatorImmutablizable<?, ?> mutator,
	                                ILogging<Logger> logging) {
		this(value, parent, offset, updater,
				new AtomicBoolean(true), mutator, logging);
	}

	protected NumberRelativeDisplay(Number value, Number parent, @Nullable Number offset, BiConsumer<?
			super GuiScreenEvent.InitGuiEvent.Pre, ? super T> updater, AtomicBoolean update, IMutatorImmutablizable<?,
			?> mutator, ILogging<Logger> logging) {
		super(value, parent, offset, mutator, logging);
		this.updater = updater;
		this.update = update;
		initializeInstance(this);
	}

	public NumberRelativeDisplay(NumberRelativeDisplay<T> copy) { this(copy, copy.getMutator()); }

	protected NumberRelativeDisplay(NumberRelativeDisplay<T> copy, IMutatorImmutablizable<?, ?> mutator) { this(copy.getValue(), copy.tryGetParent().orElseThrow(() -> Throwables.rejectArguments(mutator, copy)), copy.getOffset(), copy.getUpdater(), copy.getUpdate(), mutator, copy.getLogging()); }


	protected static <T extends NumberRelativeDisplay<?>> T initializeInstance(final T thisObj) {
		Globals.Client.registerPreInitGuiListener(thisObj, castUncheckedUnboxedNonnull(thisObj.getUpdater()));
		return thisObj;
	}


	public BiConsumer<? super GuiScreenEvent.InitGuiEvent.Pre, ? super T> getUpdater() { return updater; }

	public void setUpdater(BiConsumer<? super GuiScreenEvent.InitGuiEvent.Pre, ? super T> updater) throws UnsupportedOperationException { Throwables.rejectUnsupportedOperationIf(!trySetUpdater(updater)); }

	public boolean trySetUpdater(BiConsumer<? super GuiScreenEvent.InitGuiEvent.Pre, ? super T> updater) { return trySet(t -> Globals.Client.registerPreInitGuiListener(this, castUncheckedUnboxedNonnull(this.updater = t)), updater); }

	public Optional<BiConsumer<? super GuiScreenEvent.InitGuiEvent.Pre, ? super T>> tryGetUpdater() { return Optional.of(getUpdater()); }

	public AtomicBoolean getUpdate() { return update; }

	public void setUpdate(AtomicBoolean update) throws UnsupportedOperationException { Throwables.rejectUnsupportedOperationIf(!trySetUpdate(update)); }

	public boolean trySetUpdate(AtomicBoolean update) { return trySet(t -> this.update = t, update); }

	public Optional<AtomicBoolean> tryGetUpdate() { return Optional.of(getUpdate()); }

	@Override
	@Deprecated
	public void setParent(@Nullable Number parent) throws UnsupportedOperationException { throw Throwables.rejectUnsupportedOperation(); }


	@Override
	@OverridingMethodsMustInvokeSuper
	public T clone() { return initializeInstance(super.clone()); }


	public static class X<T extends X<T>> extends NumberRelativeDisplay<T> {
		private static final long serialVersionUID = -7447050543787167053L;


		public X(Number value, @Nullable Number offset, IMutatorImmutablizable<?, ?> mutator,
		         ILogging<Logger> logging) { this(value, offset, new AtomicBoolean(true), mutator, logging); }

		public X(Number value, @Nullable Number offset, AtomicBoolean update, IMutatorImmutablizable<?, ?> mutator,
		         ILogging<Logger> logging) {
			this(value, Globals.Client.getResolution().getScaledWidth_double(), offset, update,
					mutator, logging);
		}

		public X(Number value, AtomicBoolean update, IMutatorImmutablizable<?, ?> mutator, ILogging<Logger> logging) { this(value, null, update, mutator, logging); }

		public X(Number value, IMutatorImmutablizable<?, ?> mutator, ILogging<Logger> logging) {
			this(value,
					new AtomicBoolean(true), mutator, logging);
		}

		public X(X<?> copy) { this(copy, copy.getMutator()); }


		protected X(Number value, Number parent, @Nullable Number offset, AtomicBoolean update,
		            IMutatorImmutablizable<?, ?> mutator, ILogging<Logger> logging) {
			super(value, parent, offset, (e, t) -> {
				if (t.getUpdate().get()) t.parent = Globals.Client.getResolution().getScaledWidth_double();
			}, update, mutator, logging);
		}

		protected X(X<?> copy, IMutatorImmutablizable<?, ?> mutator) {
			this(copy.getValue(),
					copy.tryGetParent().orElseThrow(() -> Throwables.rejectArguments(copy)), Optionals.unboxOptional(copy.tryGetOffset()),
					copy.getUpdate(), mutator, copy.getLogging());
		}


		public static <T extends BuilderNumberRelative<T, V>, V extends X<V>> BuilderNumberRelative<T, V> newBuilderX(Number value) { return newBuilderX(value, new AtomicBoolean(true)); }

		public static <T extends BuilderNumberRelative<T, V>, V extends X<V>> BuilderNumberRelative<T, V> newBuilderX(Number value, AtomicBoolean update) { return new BuilderNumberRelative<>(t -> castUncheckedUnboxedNonnull(new X<>(value, t.offset, update, t.mutator, t.logging))); }


		@Override
		public T toImmutable() {
			return castUncheckedUnboxedNonnull(isImmutable() ? this : new X<>(this,
					IMutatorImmutablizable.of(getMutator().toImmutable())));
		}
	}

	public static class Y<T extends Y<T>> extends NumberRelativeDisplay<T> {
		private static final long serialVersionUID = 4079465691262621348L;


		public Y(Number value, @Nullable Number offset, IMutatorImmutablizable<?, ?> mutator,
		         ILogging<Logger> logging) { this(value, offset, new AtomicBoolean(true), mutator, logging); }

		public Y(Number value, @Nullable Number offset, AtomicBoolean update, IMutatorImmutablizable<?, ?> mutator,
		         ILogging<Logger> logging) {
			this(value, Globals.Client.getResolution().getScaledWidth_double(), offset, update,
					mutator, logging);
		}

		public Y(Number value, AtomicBoolean update, IMutatorImmutablizable<?, ?> mutator, ILogging<Logger> logging) { this(value, null, update, mutator, logging); }

		public Y(Number value, IMutatorImmutablizable<?, ?> mutator, ILogging<Logger> logging) {
			this(value,
					new AtomicBoolean(true), mutator, logging);
		}

		public Y(Y<?> copy) { this(copy, copy.getMutator()); }


		protected Y(Number value, Number parent, @Nullable Number offset, AtomicBoolean update,
		            IMutatorImmutablizable<?, ?> mutator, ILogging<Logger> logging) {
			super(value, parent, offset, (e, t) -> {
				if (t.getUpdate().get()) t.parent = Globals.Client.getResolution().getScaledHeight_double();
			}, update, mutator, logging);
		}

		protected Y(Y<?> copy, IMutatorImmutablizable<?, ?> mutator) {
			this(copy.getValue(),
					copy.tryGetParent().orElseThrow(() -> Throwables.rejectArguments(copy)), Optionals.unboxOptional(copy.tryGetOffset()),
					copy.getUpdate(), mutator, copy.getLogging());
		}


		public static <T extends BuilderNumberRelative<T, V>, V extends Y<V>> BuilderNumberRelative<T, V> newBuilderY(Number value) { return newBuilderY(value, new AtomicBoolean(true)); }

		public static <T extends BuilderNumberRelative<T, V>, V extends Y<V>> BuilderNumberRelative<T, V> newBuilderY(Number value, AtomicBoolean update) { return new BuilderNumberRelative<>(t -> castUncheckedUnboxedNonnull(new Y<>(value, t.offset, update, t.mutator, t.logging))); }


		@Override
		public T toImmutable() {
			return castUncheckedUnboxedNonnull(isImmutable() ? this : new Y<>(this,
					IMutatorImmutablizable.of(getMutator().toImmutable())));
		}
	}
}
