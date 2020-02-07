package $group__.$modId__.client.gui.polygons;

import $group__.$modId__.annotations.OverridingStatus;
import $group__.$modId__.client.gui.coordinates.XY;
import com.google.common.collect.ImmutableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static $group__.$modId__.traits.basic.IImmutablizable.tryToImmutableUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.specific.Throwables.rejectArguments;
import static $group__.$modId__.utilities.helpers.specific.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.variables.Constants.GROUP;

@SideOnly(Side.CLIENT)
public class Polygon4<N extends Number, T extends Polygon4<N, T>> extends PolygonN<N, T> {
	/* SECTION constructors */

	public Polygon4(XY<N, ?> a, XY<N, ?> b, XY<N, ?> c, XY<N, ?> d) { super(, a, b, c, d); }

	public Polygon4(Polygon4<N, ?> copy) { this(copy.getVertexes()); }

	public Polygon4(List<XY<N, ?>> list) {
		super(list, );
		if (list.size() != 4) throw rejectArguments(list);
	}


	/* SECTION getters & setters */

	@Override
	public XY<N, ?> set(int index, XY<N, ?> element) {
		XY<N, ?> r = super.set(index, element);
		markDirty(LOGGER);
		return r;
	}

	public XY<N, ?> a() { return get(0); }

	public void setA(XY<N, ?> a) { set(0, a); }

	public XY<N, ?> b() { return get(1); }

	public void setB(XY<N, ?> b) { set(0, b); }

	public XY<N, ?> c() { return get(2); }

	public void setC(XY<N, ?> c) { set(0, c); }

	public XY<N, ?> d() { return get(3); }

	public void setD(XY<N, ?> d) { set(0, d); }

	@Override
	@Deprecated
	public void setVertexes(List<XY<N, ?>> vertexes) { throw rejectUnsupportedOperation(); }


	/* SECTION methods */

	@Override
	public T toImmutable() { return castUncheckedUnboxedNonnull((Object) new Immutable<>(this)); }


	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, T extends Immutable<N, T>> extends Polygon4<N, T> {
		/* SECTION constructors */

		public Immutable(XY<N, ?> a, XY<N, ?> b, XY<N, ?> c, XY<N, ?> d) { this(ImmutableList.of(a, b, c, d)); }

		public Immutable(List<XY<N, ?>> list) { super(tryToImmutableUnboxedNonnull(list, )); }

		public Immutable(Polygon4<N, ?> copy) { this(copy.getVertexes()); }


		/* SECTION getters & setters */

		@Override
		@Deprecated
		public void setLogger(Logger logger) { throw rejectUnsupportedOperation(); }

		@Override
		@Deprecated
		public XY<N, ?> set(int index, XY<N, ?> element) { throw rejectUnsupportedOperation(); }

		@Override
		@Deprecated
		public void setA(XY<N, ?> a) { throw rejectUnsupportedOperation(); }

		@Override
		@Deprecated
		public void setB(XY<N, ?> b) { throw rejectUnsupportedOperation(); }

		@Override
		@Deprecated
		public void setC(XY<N, ?> c) { throw rejectUnsupportedOperation(); }

		@Override
		@Deprecated
		public void setD(XY<N, ?> d) { throw rejectUnsupportedOperation(); }


		/* SECTION methods */

		@Override
		@OverridingStatus(group = GROUP)
		public final T toImmutable() { return castUncheckedUnboxedNonnull(this); }

		@Override
		@OverridingStatus(group = GROUP)
		public final boolean isImmutable() { return true; }
	}
}
