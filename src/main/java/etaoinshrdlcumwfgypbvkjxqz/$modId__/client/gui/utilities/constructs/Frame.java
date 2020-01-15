package etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs;

import etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IStructure;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.OverridingStatus;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Throwables;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.meta.When;

import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IStrictEquals.isEquals;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IStrictHashCode.getHashCode;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IStrictToString.getToStringString;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Miscellaneous.Casts.castUnchecked;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Throwables.unexpectedThrowable;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.variables.Constants.GROUP;

@SideOnly(Side.CLIENT)
public class Frame<N extends Number, M extends Frame<N, M>> implements IStructure<M> {
	/* SECTION variables */

	protected XY<N, ?> tl;
	protected XY<N, ?> br;


	/* SECTION constructors */

	public Frame(XY<N, ?> tl, XY<N, ?> br) {
		this.tl = tl;
		this.br = br;
	}

	public Frame(N top, N right, N bottom, N left) { this(new XY<>(left, top), new XY<>(right, bottom)); }

	public Frame(Frame<N, ?> c) { this(c.getTopLeft(), c.getBottomRight()); }


	/* SECTION getters & setters */

	public void setTopLeft(XY<N, ?> tl) { this.tl = tl; }

	public XY<N, ?> getTopLeft() { return tl; }

	public void setBottomRight(XY<N, ?> br) { this.br = br; }

	public XY<N, ?> getBottomRight() { return br; }

	public void setTop(N t) { getTopLeft().setY(t); }

	public N top() { return getTopLeft().getY(); }

	public void setRight(N r) { getBottomRight().setX(r); }

	public N right() { return getBottomRight().getX(); }

	public void setBottom(N b) { getBottomRight().setY(b); }

	public N bottom() { return getBottomRight().getY(); }

	public void setLeft(N l) { getTopLeft().setX(l); }

	public N left() { return getTopLeft().getX(); }


	/* SECTION methods */

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public M toImmutable() { return castUnchecked(new Immutable<>(this)); }

	/** {@inheritDoc} */
	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public boolean isImmutable() { return false; }


	/** {@inheritDoc} */
	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public String toString() { return getToStringString(this, super.toString(),
				new Object[]{"tl", getTopLeft()},
				new Object[]{"br", getBottomRight()}); }

	/** {@inheritDoc} */
	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public int hashCode() { return getHashCode(this, super.hashCode(), getTopLeft(), getBottomRight()); }

	/** {@inheritDoc} */
	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public boolean equals(Object o) { return isEquals(this, o, super.equals(o),
				t -> getTopLeft().equals(t.getTopLeft()),
				t -> getBottomRight().equals(t.getBottomRight())); }

	/** {@inheritDoc} */
	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public M clone() {
		M r;
		try { r = castUnchecked(super.clone()); } catch (CloneNotSupportedException | ClassCastException e) { throw unexpectedThrowable(e); }
		r.tl = tl.clone();
		r.br = br.clone();
		return r;
	}


	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, M extends Immutable<N, M>> extends Frame<N, M> {
		/* SECTION constructors */

		public Immutable(XY<N, ?> tl, XY<N, ?> br) { super(tl.toImmutable(), br.toImmutable()); }

		public Immutable(N top, N right, N bottom, N left) { this(new XY<>(left, top), new XY<>(right, bottom)); }

		public Immutable(Frame<N, ?> c) { this(c.getTopLeft(), c.getBottomRight()); }


		/* SECTION getters & setters */

		/** {@inheritDoc} */
		@Override
		public void setTopLeft(XY<N, ?> tl) { throw Throwables.rejectUnsupportedOperation(); }

		/** {@inheritDoc} */
		@Override
		public void setBottomRight(XY<N, ?> br) { throw Throwables.rejectUnsupportedOperation(); }

		/** {@inheritDoc} */
		@Override
		public void setTop(N t) { throw Throwables.rejectUnsupportedOperation(); }

		/** {@inheritDoc} */
		@Override
		public void setRight(N r) { throw Throwables.rejectUnsupportedOperation(); }

		/** {@inheritDoc} */
		@Override
		public void setBottom(N b) { throw Throwables.rejectUnsupportedOperation(); }

		/** {@inheritDoc} */
		@Override
		public void setLeft(N l) { throw Throwables.rejectUnsupportedOperation(); }


		/* SECTION methods */

		/**
		 * {@inheritDoc}
		 */
		@Override
		public M toImmutable() { return castUnchecked(this); }

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isImmutable() { return true; }
	}
}
