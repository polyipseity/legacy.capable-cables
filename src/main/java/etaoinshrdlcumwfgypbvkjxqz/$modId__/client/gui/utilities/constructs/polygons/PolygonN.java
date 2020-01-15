package etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs.polygons;

import com.google.common.collect.ImmutableList;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.client.gui.utilities.constructs.XY;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.ICloneable;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IImmutablizable;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.OverridingStatus;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.meta.When;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IStrictEquals.isEquals;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IStrictHashCode.getHashCode;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.IStrictToString.getToStringString;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Miscellaneous.Casts.castUnchecked;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Throwables.unexpectedThrowable;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.variables.Constants.GROUP;

@SideOnly(Side.CLIENT)
public class PolygonN<N extends Number, T extends PolygonN<N, T>> extends AbstractList<XY<N, ?>> implements ICloneable<T>, IImmutablizable<T> {
	/* SECTION variables */

	protected List<XY<N, ?>> list;


	/* SECTION constructors */

	public PolygonN(List<XY<N, ?>> l) { this.list = l; }

	@SuppressWarnings("varargs")
	@SafeVarargs
	public PolygonN(XY<N, ?>... e) { this(Arrays.asList(e)); }

	public PolygonN(PolygonN<N, ?> c) { this(c.getList()); }


	/* SECTION getters & setters */

	public List<XY<N, ?>> getList() { return list; }

	public void setList(List<XY<N, ?>> list) { this.list = list; }


	/* SECTION methods */

	/** {@inheritDoc} */
	@Override
	public T toImmutable() { return castUnchecked(new Immutable<>(this)); }

	/** {@inheritDoc} */
	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public boolean isImmutable() { return false; }


	/** {@inheritDoc} */
	@Override
	public XY<N, ?> get(int index) { return getList().get(index); }

	/** {@inheritDoc} */
	@Override
	public int size() { return getList().size(); }

	/** {@inheritDoc} */
	@Override
	public XY<N, ?> set(int index, XY<N, ?> element) { return getList().set(index, element); }

	/** {@inheritDoc} */
	@Override
	public void add(int index, XY<N, ?> element) { getList().add(index, element); }

	/** {@inheritDoc} */
	@Override
	public boolean remove(Object o) { return getList().remove(o); }


	/** {@inheritDoc} */
	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public String toString() { return getToStringString(this, super.toString(),
				new Object[]{"list", getList()}); }

	/** {@inheritDoc} */
	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public int hashCode() { return getHashCode(this, super.hashCode(), getList()); }

	/** {@inheritDoc} */
	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public boolean equals(Object o) { return isEquals(this, o, super.equals(o),
			t -> getList().equals(t.getList())); }

	/** {@inheritDoc} */
	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T clone() {
		T r;
		try { r = castUnchecked(super.clone()); } catch (CloneNotSupportedException | ClassCastException e) { throw unexpectedThrowable(e); }
		r.list = list instanceof ImmutableList ? ImmutableList.copyOf(list) : new ArrayList<>(list);
		return r;
	}


	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, T extends Immutable<N, T>> extends PolygonN<N, T> {
		/* SECTION constructors */

		public Immutable(List<XY<N, ?>> l) { super(ImmutableList.copyOf(l)); }

		@SuppressWarnings("varargs")
		@SafeVarargs
		public Immutable(XY<N, ?>... e) { this(Arrays.asList(e)); }

		public Immutable(PolygonN<N, ?> c) { this(c.getList()); }


		/* SECTION getters & setters */

		/** {@inheritDoc} */
		@Override
		public void setList(List<XY<N, ?>> list) { throw rejectUnsupportedOperation(); }


		/* SECTION methods */

		/** {@inheritDoc} */
		@Override
		public T toImmutable() { return castUnchecked(this); }

		/** {@inheritDoc} */
		@Override
		public boolean isImmutable() { return true; }
	}
}
