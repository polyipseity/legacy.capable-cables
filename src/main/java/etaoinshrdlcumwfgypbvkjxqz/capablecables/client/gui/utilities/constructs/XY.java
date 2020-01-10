package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.constructs.ICloneable;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.constructs.IImmutablizable;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.Primitives.Numbers;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.Throwables.unexpectedThrowable;

@SideOnly(Side.CLIENT)
public class XY<N extends Number, M extends XY<N, M>> implements ICloneable<M>, IImmutablizable<M> {
	protected N x;
	protected N y;

	public XY(N x, N y) {
		this.x = x;
		this.y = y;
	}

	public void setX(N x) { this.x = x; }

	public N getX() { return x; }

	public void setY(N y) { this.y = y; }

	public N getY() { return y; }


	@SuppressWarnings("varargs")
	@SafeVarargs
	public final XY<N, ?> sum(XY<N, ?>... o) { return new XY<>(Numbers.sum(x, extractX(o)), Numbers.sum(y, extractY(o))); }

	@SuppressWarnings("varargs")
	@SafeVarargs
	public final XY<N, ?> sumX(XY<N, ?>... o) { return new XY<>(Numbers.sum(x, extractX(o)), y); }

	@SuppressWarnings("varargs")
	@SafeVarargs
	public final XY<N, ?> sumY(XY<N, ?>... o) { return new XY<>(x, Numbers.sum(y, extractY(o))); }

	public XY<N, ?> minus(XY<N, ?> o) { return new XY<>(Numbers.minus(x, o.x), Numbers.minus(y, o.y)); }

	public XY<N, ?> minusX(XY<N, ?> o) { return new XY<>(Numbers.minus(x, o.x), y); }

	public XY<N, ?> minusY(XY<N, ?> o) { return new XY<>(x, Numbers.minus(y, o.y)); }

	@SuppressWarnings("varargs")
	@SafeVarargs
	public final XY<N, ?> max(XY<N, ?>... o) { return new XY<>(Numbers.max(x, extractX(o)), Numbers.max(y, extractY(o))); }

	@SuppressWarnings("varargs")
	@SafeVarargs
	public final XY<N, ?> min(XY<N, ?>... o) { return new XY<>(Numbers.min(x, extractX(o)), Numbers.min(y, extractY(o))); }


	@SuppressWarnings("varargs")
	@SafeVarargs
	public static <N extends Number> List<N> extractX(XY<N, ?>... o) { return Arrays.stream(o).map(XY::getX).collect(Collectors.toList()); }

	@SuppressWarnings("varargs")
	@SafeVarargs
	public static <N extends Number> List<N> extractY(XY<N, ?>... o) { return Arrays.stream(o).map(XY::getY).collect(Collectors.toList()); }


	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public M clone() {
		try {
			return (M) super.clone();
		} catch (CloneNotSupportedException | ClassCastException ex) { throw unexpectedThrowable(ex); }
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof XY)) return false;
		XY<?, ?> xy = (XY<?, ?>) o;
		return x.equals(xy.x) &&
				y.equals(xy.y);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() { return Objects.hash(x, y); }


	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public M toImmutable() { return (M) new Immutable<>(this); }

	/** {@inheritDoc} */
	@Override
	public boolean isImmutable() { return false; }


	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, M extends Immutable<N, M>> extends XY<N, M> {
		public Immutable(N x, N y) { super(x, y); }

		public Immutable(XY<N, ?> c) { this(c.x, c.y); }

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setX(N x) { throw rejectUnsupportedOperation(); }

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setY(N y) { throw rejectUnsupportedOperation(); }


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
