package etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs.polygons;

import com.google.common.collect.ImmutableList;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs.XY;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.constructs.ICloneable;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.constructs.IImmutablizable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.helpers.Throwables.unexpectedThrowable;

@SideOnly(Side.CLIENT)
public class PolygonN<N extends Number, M extends PolygonN<N, M>> extends AbstractList<XY<N, ?>> implements ICloneable<M>, IImmutablizable<M> {
	protected List<XY<N, ?>> list;

	@SuppressWarnings("varargs")
	@SafeVarargs
	public PolygonN(XY<N, ?>... e) { this.list = Arrays.asList(e); }

	public PolygonN(List<XY<N, ?>> l) { this.list = l; }

	public void setList(List<XY<N, ?>> list) { this.list = list; }

	public List<XY<N, ?>> getList() { return list; }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public XY<N, ?> get(int index) { return list.get(index); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() { return list.size(); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public XY<N, ?> set(int index, XY<N, ?> element) { return list.set(index, element); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(int index, XY<N, ?> element) { list.add(index, element); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(Object o) { return list.remove(o); }

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
		r.list = list instanceof ImmutableList ? ImmutableList.copyOf(list) : new ArrayList<>(list);
		return r;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PolygonN)) return false;
		if (!super.equals(o)) return false;
		PolygonN<?, ?> polygonN = (PolygonN<?, ?>) o;
		return list.equals(polygonN.list);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() { return Objects.hash(super.hashCode(), list); }

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public M toImmutable() { return (M) new Immutable<>(this); }

	/** {@inheritDoc} */
	@Override
	public boolean isImmutable() { return false; }


	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, M extends Immutable<N, M>> extends PolygonN<N, M> {
		@SuppressWarnings("varargs")
		@SafeVarargs
		public Immutable(XY<N, ?>... e) { super(ImmutableList.copyOf(e)); }

		public Immutable(List<XY<N, ?>> l) { super(ImmutableList.copyOf(l)); }

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setList(List<XY<N, ?>> list) { throw rejectUnsupportedOperation(); }


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
