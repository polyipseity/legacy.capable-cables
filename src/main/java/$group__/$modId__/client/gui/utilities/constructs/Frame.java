package $group__.$modId__.client.gui.utilities.constructs;

import $group__.$modId__.utilities.constructs.interfaces.IStructureCloneable;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import $group__.$modId__.utilities.helpers.Throwables;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.meta.When;

import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictEquals.isEquals;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.constructs.interfaces.basic.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Throwables.unexpected;
import static $group__.$modId__.utilities.variables.Constants.GROUP;

@SideOnly(Side.CLIENT)
public class Frame<N extends Number, T extends Frame<N, T>> implements IStructureCloneable<T> {
	/* SECTION variables */

	protected XY<N, ?> tl;
	protected XY<N, ?> br;


	/* SECTION constructors */

	public Frame(XY<N, ?> tl, XY<N, ?> br) {
		this.tl = tl;
		this.br = br;
	}

	public Frame(N top, N right, N bottom, N left) { this(new XY<>(left, top), new XY<>(right, bottom)); }

	public Frame(Frame<N, ?> copy) { this(copy.getTopLeft(), copy.getBottomRight()); }


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
	@Override
	public T toImmutable() { return castUncheckedUnboxedNonnull(new Immutable<>(this)); }

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
	public T clone() {
		T r;
		try { r = castUncheckedUnboxedNonnull(super.clone()); } catch (CloneNotSupportedException e) { throw unexpected(e); }
		r.tl = tl.clone();
		r.br = br.clone();
		return r;
	}


	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, T extends Immutable<N, T>> extends Frame<N, T> {
		/* SECTION constructors */

		public Immutable(XY<N, ?> tl, XY<N, ?> br) { super(tl.toImmutable(), br.toImmutable()); }

		public Immutable(N top, N right, N bottom, N left) { this(new XY.Immutable<>(left, top), new XY.Immutable<>(right, bottom)); }

		public Immutable(Frame<N, ?> copy) { this(copy.getTopLeft(), copy.getBottomRight()); }


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
		@OverridingStatus(group = GROUP, when = When.NEVER)
		public final T toImmutable() { return castUncheckedUnboxedNonnull(this); }

		/**
		 * {@inheritDoc}
		 */
		@Override
		@OverridingStatus(group = GROUP, when = When.NEVER)
		public final boolean isImmutable() { return true; }
	}
}
