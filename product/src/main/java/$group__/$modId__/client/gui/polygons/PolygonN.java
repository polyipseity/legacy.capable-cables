package $group__.$modId__.client.gui.polygons;

import $group__.$modId__.annotations.OverridingStatus;
import $group__.$modId__.client.gui.coordinates.XY;
import $group__.$modId__.logging.ILogging;
import $group__.$modId__.logging.ILoggingUser;
import $group__.$modId__.utilities.concurrent.IMutatorImmutablizable;
import $group__.$modId__.utilities.concurrent.IMutatorUser;
import $group__.$modId__.utilities.extensions.ICloneable;
import $group__.$modId__.utilities.extensions.IStructure;
import $group__.$modId__.utilities.extensions.delegated.IListDelegated;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.meta.When;
import java.util.List;
import java.util.Optional;

import static $group__.$modId__.utilities.Constants.PACKAGE;
import static $group__.$modId__.utilities.concurrent.IMutator.trySetNonnull;
import static $group__.$modId__.utilities.extensions.IStrictEquals.areEqual;
import static $group__.$modId__.utilities.extensions.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.extensions.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.specific.Throwables.rejectUnsupportedOperationIf;

@SideOnly(Side.CLIENT)
public class PolygonN<T extends PolygonN<T, N, L>, N extends Number, L extends List<XY<?, N>>> implements IStructure<T, T>, ICloneable<T>, IListDelegated<L, XY<?, N>>, IMutatorUser<IMutatorImmutablizable<?, ?>>, ILoggingUser<ILogging<Logger>, Logger> {
	/* SECTION variables */

	protected L vertexes;

	protected IMutatorImmutablizable<?, ?> mutator;
	protected ILogging<Logger> logging;


	/* SECTION constructors */

	public PolygonN(L vertexes, IMutatorImmutablizable<?, ?> mutator, ILogging<Logger> logging) {
		this.mutator = trySetNonnull(mutator, mutator, true);
		this.logging = trySetNonnull(getMutator(), logging, true);
		this.vertexes = trySetNonnull(getMutator(), vertexes, true);
	}

	public PolygonN(PolygonN<?, N, L> copy) { this(copy, copy.getMutator()); }


	protected PolygonN(PolygonN<?, N, ? extends L> copy, IMutatorImmutablizable<?, ?> mutator) { this(copy.getVertexes(), mutator, copy.getLogging()); }


	/* SECTION getters & setters */

	public L getVertexes() { return vertexes; }

	public void setVertexes(L vertexes) throws UnsupportedOperationException { rejectUnsupportedOperationIf(!trySetVertexes(vertexes)); }

	public boolean trySetVertexes(L vertexes) { return trySet(t -> this.vertexes = t, vertexes); }

	public Optional<L> tryGetVertexes() { return Optional.of(getVertexes()); }

	@Override
	public IMutatorImmutablizable<?, ?> getMutator() { return mutator; }

	@Override
	public boolean trySetMutator(IMutatorImmutablizable<?, ?> mutator) { return trySet(t -> this.mutator = t,
			mutator); }

	@Override
	public ILogging<Logger> getLogging() { return logging; }

	@Override
	public boolean trySetLogging(ILogging<Logger> logging) { return trySet(t -> this.logging = t, logging); }


	@Override
	@Deprecated
	public L getList() { return getVertexes(); }

	@Override
	@Deprecated
	public void setList(L list) throws UnsupportedOperationException { setVertexes(list); }


	/* SECTION methods */


	@Override
	public T toImmutable() { return castUncheckedUnboxedNonnull(isImmutable() ? this : new PolygonN<>(this,
			IMutatorImmutablizable.of(getMutator().toImmutable()))); }

	@Override
	public boolean isImmutable() { return getMutator().isImmutable(); }


	@Override
	@OverridingStatus(group = PACKAGE)
	public final int hashCode() { return getHashCode(this, super::hashCode); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	@OverridingStatus(group = PACKAGE)
	public final boolean equals(Object o) { return areEqual(this, o, super::equals); }

	@SuppressWarnings("Convert2MethodRef")
	@Override
	@OverridingMethodsMustInvokeSuper
	@OverridingStatus(group = PACKAGE, when = When.MAYBE)
	public T clone() { return ICloneable.clone(() -> super.clone(), getLogger()); }

	@Override
	@OverridingStatus(group = PACKAGE)
	public final String toString() { return getToStringString(this, super.toString()); }
}
