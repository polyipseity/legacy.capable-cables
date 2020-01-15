package etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs;

import etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.*;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Primitives.Numbers;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.meta.When;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IStrictEquals.isEquals;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IStrictHashCode.getHashCode;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Miscellaneous.Casts.castUnchecked;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Throwables.unexpectedThrowable;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.variables.Constants.GROUP;

@SideOnly(Side.CLIENT)
public class XY<N extends Number, T extends XY<N, T>> implements IStructure<T>, IOperable<XY<N, ?>, XY<N, ?>> {
	/* SECTION variables */
	
	protected N x;
	protected N y;
	
	
	/* SECTION constructors */

	public XY(N x, N y) {
		this.x = x;
		this.y = y;
	}
	
	public XY(XY<N, ?> c) { this(c.getX(), c.getY()); }
	
	
	/* SECTION getters & setters */
	
	public N getX() { return x; }
	
	public void setX(N x) { this.x = x; }
	
	public N getY() { return y; }

	public void setY(N y) { this.y = y; }
	
	
	/* SECTION methods */

	@SuppressWarnings("varargs")
	@SafeVarargs
	public final XY<N, ?> sum(XY<N, ?>... o) { return new XY<>(Numbers.sum(getX(), extractX(o)), Numbers.sum(getY(), extractY(o))); }

	@SuppressWarnings("varargs")
	@SafeVarargs
	public final XY<N, ?> sumX(XY<N, ?>... o) { return new XY<>(Numbers.sum(getX(), extractX(o)), getY()); }

	@SuppressWarnings("varargs")
	@SafeVarargs
	public final XY<N, ?> sumY(XY<N, ?>... o) { return new XY<>(getX(), Numbers.sum(getY(), extractY(o))); }

	public final XY<N, ?> minus(XY<N, ?> o) { return new XY<>(Numbers.minus(getX(), o.getX()), Numbers.minus(getY(), o.getY())); }

	public final XY<N, ?> minusX(XY<N, ?> o) { return new XY<>(Numbers.minus(getX(), o.getX()), getY()); }

	public final XY<N, ?> minusY(XY<N, ?> o) { return new XY<>(getX(), Numbers.minus(getY(), o.getY())); }

	@SuppressWarnings("varargs")
	@SafeVarargs
	public final XY<N, ?> max(XY<N, ?>... o) { return new XY<>(Numbers.max(getX(), extractX(o)), Numbers.max(getY(), extractY(o))); }

	@SuppressWarnings("varargs")
	@SafeVarargs
	public final XY<N, ?> min(XY<N, ?>... o) { return new XY<>(Numbers.min(getX(), extractX(o)), Numbers.min(getY(), extractY(o))); }


	/** {@inheritDoc} */
	public T toImmutable() { return castUnchecked(new Immutable<>(this)); }

	/** {@inheritDoc} */
	@Override
	public boolean isImmutable() { return false; }


	/** {@inheritDoc} */
	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public String toString() { return IStrictToString.getToStringString(this, super.toString(),
			new Object[]{"x", getX()},
			new Object[]{"y", getY()}); }

	/** {@inheritDoc} */
	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public int hashCode() { return getHashCode(this, super.hashCode(), getX(), getY()); }

	/** {@inheritDoc} */
	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public boolean equals(Object o) { return isEquals(this, o, super.equals(o),
			t -> getX().equals(t.getX()),
			t -> getY().equals(t.getY())); }

	/** {@inheritDoc} */
	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T clone() {
		T r;
		try { r = castUnchecked(super.clone()); } catch (CloneNotSupportedException | ClassCastException e) { throw unexpectedThrowable(e); }
		if (x instanceof ICloneable<?>) r.x = castUnchecked(x, (ICloneable<N>) null).clone();
		if (y instanceof ICloneable<?>) r.y = castUnchecked(y, (ICloneable<N>) null).clone();
		return r;
	}

	
	/* SECTION static methods */

	@SuppressWarnings("varargs")
	@SafeVarargs
	public static <N extends Number> List<N> extractX(XY<N, ?>... o) { return Arrays.stream(o).map(XY::getX).collect(Collectors.toList()); }

	@SuppressWarnings("varargs")
	@SafeVarargs
	public static <N extends Number> List<N> extractY(XY<N, ?>... o) { return Arrays.stream(o).map(XY::getY).collect(Collectors.toList()); }

	
	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, T extends Immutable<N, T>> extends XY<N, T> {
		/* SECTION constructors */

		public Immutable(N x, N y) { super(x, y); }

		public Immutable(XY<N, ?> c) { this(c.getX(), c.getY()); }


		/* SECTION getters & setters */

		/** {@inheritDoc} */
		@Override
		public void setX(N x) { throw rejectUnsupportedOperation(); }

		/** {@inheritDoc} */
		@Override
		public void setY(N y) { throw rejectUnsupportedOperation(); }


		/* SECTION methods */

		/** {@inheritDoc} */
		@Override
		public T toImmutable() { return castUnchecked(this); }

		/** {@inheritDoc} */
		@Override
		public boolean isImmutable() { return true; }
	}
}
