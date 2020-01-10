package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.constructs.ICloneable;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.constructs.IImmutablizable;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.Throwables;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Objects;
import java.util.function.Function;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.Throwables.unexpectedThrowable;

@SideOnly(Side.CLIENT)
public class Frame<N extends Number, M extends Frame<N, M>> implements ICloneable<M>, IImmutablizable<M> {
	protected XY<N, ?> tl;
	protected XY<N, ?> br;

	public Frame(XY<N, ?> tl, XY<N, ?> br) {
		this.tl = tl;
		this.br = br;
	}

	public Frame(N top, N right, N bottom, N left) {
		tl = new XY<>(left, top);
		br = new XY<>(right, bottom);
	}


	public void setTopLeft(XY<N, ?> tl) { this.tl = tl; }

	public XY<N, ?> getTopLeft() { return tl; }

	public void setBottomRight(XY<N, ?> br) { this.br = br; }

	public XY<N, ?> getBottomRight() { return br; }

	public void setTop(N t) { tl.setY(t); }

	public N top() { return tl.getY(); }

	public void setRight(N r) { br.setX(r); }

	public N right() { return br.getX(); }

	public void setBottom(N b) { br.setY(b); }

	public N bottom() { return br.getY(); }

	public void setLeft(N l) { tl.setX(l); }

	public N left() { return tl.getX(); }


	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public M clone() {
		M r;
		try { r = (M) super.clone(); } catch (CloneNotSupportedException | ClassCastException ex) {
			throw unexpectedThrowable(ex);
		}
		r.tl = tl.clone();
		r.br = br.clone();
		return r;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Frame)) return false;
		Frame<?, ?> frame = (Frame<?, ?>) o;
		return tl.equals(frame.tl) &&
				br.equals(frame.br);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() { return Objects.hash(tl, br); }

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public M toImmutable() { return (M) new Immutable<>(this); }

	/** {@inheritDoc} */
	@Override
	public boolean isImmutable() { return false; }


	public static <N extends Number, M extends Frame<N, M>> Frame<N, M> getNone(Function<Number, N> n) {
		N nr = n.apply(0);
		return new Frame<>(nr, nr, nr, nr);
	}


	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, M extends Immutable<N, M>> extends Frame<N, M> {
		public Immutable(XY<N, ?> tl, XY<N, ?> br) { super(tl.toImmutable(), br.toImmutable()); }

		public Immutable(N top, N right, N bottom, N left) { this(new XY<>(left, top), new XY<>(right, bottom)); }

		public Immutable(Frame<N, ?> c) { this(c.tl, c.br); }


		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setTopLeft(XY<N, ?> tl) { throw Throwables.rejectUnsupportedOperation(); }

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setBottomRight(XY<N, ?> br) { throw Throwables.rejectUnsupportedOperation(); }

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setTop(N t) { throw Throwables.rejectUnsupportedOperation(); }

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setRight(N r) { throw Throwables.rejectUnsupportedOperation(); }

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setBottom(N b) { throw Throwables.rejectUnsupportedOperation(); }

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setLeft(N l) { throw Throwables.rejectUnsupportedOperation(); }


		/**
		 * {@inheritDoc}
		 */
		@SuppressWarnings("unchecked")
		@Override
		public M toImmutable() { return (M) this; }

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isImmutable() { return true; }
	}
}
