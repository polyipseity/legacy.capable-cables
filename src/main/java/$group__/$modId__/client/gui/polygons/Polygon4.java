package $group__.$modId__.client.gui.polygons;

import $group__.$modId__.client.gui.coordinates.XY;
import $group__.$modId__.concurrent.IMutatorImmutablizable;
import $group__.$modId__.logging.ILogging;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.specific.Throwables.rejectArguments;

@SideOnly(Side.CLIENT)
public class Polygon4<T extends Polygon4<T, N, L>, N extends Number, L extends List<XY<?, N>>> extends PolygonN<T, N, L> {
	/* SECTION constructors */

	public Polygon4(L vertexes, IMutatorImmutablizable<?, ?> mutator, ILogging<Logger> logging) {
		super(vertexes, mutator, logging);
		if (vertexes.size() != 4) throw rejectArguments(vertexes);
	}

	public Polygon4(Polygon4<?, N, L> copy) { this(copy, copy.getMutator()); }


	protected Polygon4(Polygon4<?, N, L> copy, IMutatorImmutablizable<?, ?> mutator) { this(copy.getVertexes(), mutator, copy.getLogging()); }


	/* SECTION getters & setters */

	public XY<?, N> a() { return get(0); }

	public void setA(XY<?, N> a) { set(0, a); }

	public XY<?, N> b() { return get(1); }

	public void setB(XY<?, N> b) { set(0, b); }

	public XY<?, N> c() { return get(2); }

	public void setC(XY<?, N> c) { set(0, c); }

	public XY<?, N> d() { return get(3); }

	public void setD(XY<?, N> d) { set(0, d); }

	@Override
	public boolean trySetVertexes(L vertexes) { return vertexes.size() == 4 && super.trySetVertexes(vertexes); }


	/* SECTION methods */

	@Override
	public T toImmutable() { return castUncheckedUnboxedNonnull(isImmutable() ? this : new Polygon4<>(this, IMutatorImmutablizable.of(getMutator().toImmutable()))); }
}
