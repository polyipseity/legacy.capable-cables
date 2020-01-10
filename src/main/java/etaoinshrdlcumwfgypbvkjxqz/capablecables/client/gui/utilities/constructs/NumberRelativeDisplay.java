package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.constructs.NumberRelative;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.variables.References.Client.RESOLUTION;

@SideOnly(Side.CLIENT)
public abstract class NumberRelativeDisplay<N extends NumberRelativeDisplay<N>> extends NumberRelative<N> {
	protected static final List<WeakReference<? extends NumberRelativeDisplay<?>>> UPDATERS = new ArrayList<>();

	protected final Consumer<N> updater;


	@SuppressWarnings("unchecked")
	public NumberRelativeDisplay(Number value, Consumer<N> updater, @Nullable Number offset) {
		super(value, null, offset);
		updater.accept((N) this);
		this.updater = updater;

		UPDATERS.add(new WeakReference<>(this));
	}


	@SubscribeEvent
	public static <N extends NumberRelativeDisplay<N>> void initGui(GuiScreenEvent.InitGuiEvent e) {
		UPDATERS.removeIf(t -> t.get() == null);
		UPDATERS.forEach(t -> {
			@SuppressWarnings("unchecked") N tv = (N) t.get();
			if (tv != null) tv.updater.accept(tv);
		});
	}


	/** {@inheritDoc} */
	@Override
	public void setParent(@Nullable Number parent) { throw rejectUnsupportedOperation();}



	public static class X<N extends X<N>> extends NumberRelativeDisplay<N> {
		public X(Number value, @Nullable Number offset) { super(value, t -> t.parent = RESOLUTION.getScaledWidth_double(), offset); }

		public X(Number value) { this(value, null); }


		/** {@inheritDoc} */
		@SuppressWarnings("unchecked")
		@Override
		public N toImmutable() { return (N) new Immutable<>(this); }


		@javax.annotation.concurrent.Immutable
		public static class Immutable<N extends Immutable<N>> extends X<N> {
			public Immutable(Number value, @Nullable Number offset) { super(value, offset); }

			public Immutable(Number value) { super(value); }

			public Immutable(X<?> c) { super(c.value, c.offset); }


			/** {@inheritDoc} */
			@Override
			public void setValue(Number value) { throw rejectUnsupportedOperation();}

			/** {@inheritDoc} */
			@Override
			public void setOffset(@Nullable Number offset) { throw rejectUnsupportedOperation();}


			/** {@inheritDoc} */
			@SuppressWarnings("unchecked")
			@Override
			public N toImmutable() { return (N) this; }

			/** {@inheritDoc} */
			@Override
			public boolean isImmutable() { return true; }
		}
	}


	public static class Y<N extends Y<N>> extends NumberRelativeDisplay<N> {
		public Y(Number value, @Nullable Number offset) { super(value, t -> t.parent = RESOLUTION.getScaledHeight_double(), offset); }

		public Y(Number value) { this(value, null); }


		/** {@inheritDoc} */
		@SuppressWarnings("unchecked")
		@Override
		public N toImmutable() { return (N) new Immutable<>(this); }


		@javax.annotation.concurrent.Immutable
		public static class Immutable<N extends Immutable<N>> extends Y<N> {
			public Immutable(Number value, @Nullable Number offset) { super(value, offset); }

			public Immutable(Number value) { super(value); }

			public Immutable(Y<?> c) { super(c.value, c.offset); }


			/** {@inheritDoc} */
			@Override
			public void setValue(Number value) { throw rejectUnsupportedOperation();}

			/** {@inheritDoc} */
			@Override
			public void setOffset(@Nullable Number offset) { throw rejectUnsupportedOperation();}


			/** {@inheritDoc} */
			@SuppressWarnings("unchecked")
			@Override
			public N toImmutable() { return (N) this; }

			/** {@inheritDoc} */
			@Override
			public boolean isImmutable() { return true; }
		}
	}
}
