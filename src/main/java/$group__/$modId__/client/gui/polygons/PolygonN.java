package $group__.$modId__.client.gui.polygons;

import $group__.$modId__.annotations.OverridingStatus;
import $group__.$modId__.client.gui.coordinates.XY;
import $group__.$modId__.traits.IListDelegated;
import $group__.$modId__.traits.basic.IDirty;
import $group__.$modId__.traits.basic.ILogging;
import $group__.$modId__.traits.extensions.ICloneable;
import com.google.common.collect.ImmutableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import javax.annotation.meta.When;
import java.util.Arrays;
import java.util.List;

import static $group__.$modId__.traits.basic.IImmutablizable.tryToImmutableUnboxedNonnull;
import static $group__.$modId__.traits.extensions.IStrictEquals.isEqual;
import static $group__.$modId__.traits.extensions.IStrictHashCode.getHashCode;
import static $group__.$modId__.traits.extensions.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.specific.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.variables.Constants.GROUP;

@SideOnly(Side.CLIENT)
public class PolygonN<N extends Number, T extends PolygonN<N, T>> implements IListDelegated<List<XY<N, ?>>, XY<N, ?>, T>, IDirty, ILogging {
	/* SECTION variables */

	protected List<XY<N, ?>> vertexes;
	protected long dirtiness;
	protected Logger logger;


	/* SECTION constructors */

	@SuppressWarnings("varargs")
	@SafeVarargs
	public PolygonN(Logger logger, XY<N, ?>... array) { this(Arrays.asList(array), logger); }

	public PolygonN(List<XY<N, ?>> vertexes, Logger logger) {
		this.vertexes = vertexes;
		this.logger = logger;
	}

	public PolygonN(PolygonN<N, ?> copy) {
		this(copy.getVertexes(), copy.getLogger());
		this.dirtiness = copy.dirtiness;
	}


	/* SECTION getters & setters */

	public List<XY<N, ?>> getVertexes() { return vertexes; }

	public void setVertexes(List<XY<N, ?>> vertexes) {
		this.vertexes = vertexes;
		markDirty(getLogger());
	}

	@Override
	public Logger getLogger() { return logger; }

	@Override
	public void setLogger(Logger logger) { this.logger = logger; }

	@Override
	@Deprecated
	public List<XY<N, ?>> getList() { return getVertexes(); }

	@Override
	@Deprecated
	public void setList(List<XY<N, ?>> list) { setVertexes(list); }


	/* SECTION methods */

	@Override
	public T toImmutable() { return castUncheckedUnboxedNonnull(new Immutable<>(this)); }

	@Override
	public boolean isImmutable() { return false; }


	@Override
	@OverridingStatus(group = GROUP)
	public final int hashCode() { return getHashCode(this, super::hashCode); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	@OverridingStatus(group = GROUP)
	public final boolean equals(Object o) { return isEqual(this, o, super::equals); }

	@SuppressWarnings("Convert2MethodRef")
	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T clone() { return ICloneable.clone(() -> super.clone(), getLogger()); }

	@Override
	@OverridingStatus(group = GROUP)
	public final String toString() { return getToStringString(this, super.toString()); }


	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, T extends Immutable<N, T>> extends PolygonN<N, T> implements IListDelegated.IImmutable<List<XY<N, ?>>, XY<N, ?>, T> {
		/* SECTION constructors */

		@SuppressWarnings("varargs")
		@SafeVarargs
		public Immutable(Logger logger, XY<N, ?>... array) { this(ImmutableList.copyOf(array), logger); }

		public Immutable(List<XY<N, ?>> list, Logger logger) { super(tryToImmutableUnboxedNonnull(list, logger), logger); }

		public Immutable(PolygonN<N, ?> copy) { this(copy.getVertexes(), copy.getLogger()); }


		/* SECTION getters & setters */

		@Override
		@Deprecated
		public void setLogger(Logger logger) { throw rejectUnsupportedOperation(); }

		@Override
		@Deprecated
		public void setVertexes(List<XY<N, ?>> vertexes) { throw rejectUnsupportedOperation(); }


		/* SECTION methods */

		@Override
		@OverridingStatus(group = GROUP)
		public final T toImmutable() { return castUncheckedUnboxedNonnull(this); }

		@Override
		@OverridingStatus(group = GROUP)
		public final boolean isImmutable() { return true; }
	}
}
