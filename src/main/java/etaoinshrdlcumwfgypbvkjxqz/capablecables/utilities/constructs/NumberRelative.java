package etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.constructs;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.Primitives.Numbers;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.stream.Collectors;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.Throwables.unexpectedThrowable;

public class NumberRelative<N extends NumberRelative<N>> extends Number implements ICloneable<N>, INumberOperable<N>, IImmutablizable<N> {
	protected Number value;
	@Nullable
	protected Number parent;
	@Nullable
	protected Number offset;


	public NumberRelative(Number value, @Nullable Number parent, @Nullable Number offset) {
		this.value = value;
		this.parent = parent;
		this.offset = offset;
	}

	public NumberRelative(Number value, @Nullable Number parent) { this(value, parent, null); }

	public NumberRelative(Number value) { this(value, null); }


	public void setValue(Number value) { this.value = value; }

	public Number getValue() { return value; }

	public void setParent(@Nullable Number parent) { this.parent = parent; }

	@Nullable
	public Number getParent() { return parent; }

	public void setOffset(@Nullable Number offset) { this.offset = offset; }

	@Nullable
	public Number getOffset() { return offset; }


	/** {@inheritDoc} */
	@Override
	public int intValue() { return (int) doubleValue(); }

	/** {@inheritDoc} */
	@Override
	public long longValue() { return (long) doubleValue(); }

	/** {@inheritDoc} */
	@Override
	public float floatValue() { return (float) doubleValue(); }

	/** {@inheritDoc} */
	@Override
	public double doubleValue() { return (parent == null ? 1D : parent.doubleValue()) * value.doubleValue() + (offset == null ? 0D : offset.doubleValue()); }


	/** {@inheritDoc} */
	@Override
	public int compareTo(N o) {
		boolean greater = doubleValue() > o.doubleValue();
		boolean lesser = doubleValue() < o.doubleValue();
		return greater ? 1 : lesser ? -1 : 0;
	}


	/** {@inheritDoc} */
	@Override
	public N sum(Number... o) {
		N r = clone();
		r.value = Numbers.sum(value, Arrays.stream(o).map(t -> parent == null ? t.doubleValue() : t.doubleValue() / parent.doubleValue()).collect(Collectors.toList()));
		r.offset = offset == null ? Numbers.sum(Arrays.stream(o).filter(t -> t instanceof NumberRelative<?> && ((NumberRelative<?>) t).offset != null).collect(Collectors.toList()), true) : Numbers.sum(offset, Arrays.stream(o).filter(t -> t instanceof NumberRelative<?> && ((NumberRelative<?>) t).offset != null).collect(Collectors.toList()));
		return r;
	}

	/** {@inheritDoc} */
	@Override
	public N minus(Number o) {
		N r = clone();
		r.value = Numbers.minus(value, parent == null ? o.doubleValue() : o.doubleValue() / parent.doubleValue());
		if (o instanceof NumberRelative<?>) {
			NumberRelative<?> oTemp = (NumberRelative<?>) o;
			if (oTemp.offset != null) r.offset = offset == null ? oTemp.offset : Numbers.minus(offset, oTemp.offset);
		}
		return r;
	}


	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public N clone() {
		N r;
		try { r = (N) super.clone(); } catch (CloneNotSupportedException | ClassCastException ex) { throw unexpectedThrowable(ex); }
		if (value instanceof ICloneable<?>) r.value = ((ICloneable<Number>) value).clone();
		if (parent != null && parent instanceof ICloneable<?>) r.parent = ((ICloneable<Number>) parent).clone();
		if (offset != null && offset instanceof ICloneable<?>) r.offset = ((ICloneable<Number>) offset).clone();
		return r;
	}


	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public N toImmutable() {
		return (N) new Immutable<>(this);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isImmutable() { return false; }


	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends NumberRelative<N>> extends NumberRelative<N> {
		public Immutable(Number value, @Nullable Number parent, @Nullable Number offset) { super(value, parent, offset); }
		public Immutable(Number value, @Nullable Number parent) { this(value, parent, null); }
		public Immutable(Number value) { this(value, null); }


		/** {@inheritDoc} */
		@Override
		public void setValue(Number value) { throw rejectUnsupportedOperation(); }

		/** {@inheritDoc} */
		@Override
		public void setParent(@Nullable Number parent) { throw rejectUnsupportedOperation(); }

		/** {@inheritDoc} */
		@Override
		public void setOffset(@Nullable Number offset) { throw rejectUnsupportedOperation(); }


		/** {@inheritDoc} */
		@SuppressWarnings("unchecked")
		@Override
		public N toImmutable() { return (N) this; }

		/** {@inheritDoc} */
		@Override
		public boolean isImmutable() { return true; }
	}
}
