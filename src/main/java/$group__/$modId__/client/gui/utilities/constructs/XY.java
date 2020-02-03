package $group__.$modId__.client.gui.utilities.constructs;

import $group__.$modId__.utilities.constructs.interfaces.IStructureCloneable;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import $group__.$modId__.utilities.constructs.interfaces.basic.IDirty;
import $group__.$modId__.utilities.constructs.interfaces.basic.IOperable;
import $group__.$modId__.utilities.constructs.interfaces.extensions.ICloneable;
import $group__.$modId__.utilities.helpers.Numbers;
import $group__.$modId__.utilities.variables.Globals;
import com.google.common.collect.Streams;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.meta.When;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static $group__.$modId__.utilities.constructs.interfaces.basic.IImmutablizable.tryToImmutableUnboxedNonnull;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictEquals.isEqual;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;
import static $group__.$modId__.utilities.variables.Constants.GROUP;

@SideOnly(Side.CLIENT)
public class XY<N extends Number, T extends XY<N, T>> implements IStructureCloneable<T>, IOperable<T, XY<N, ?>>, IDirty {
	/* SECTION variables */

	protected N x;
	protected N y;
	protected long dirtiness;


	/* SECTION constructors */

	public XY(XY<? extends N, ?> copy) {
		this(copy.getX(), copy.getY());
		this.dirtiness = copy.dirtiness;
	}

	public XY(N x, N y) {
		this.x = x;
		this.y = y;
	}


	/* SECTION static methods */

	@SuppressWarnings("varargs")
	@SafeVarargs
	public static <N extends Number> List<N> extractXs(XY<N, ?>... o) { return extractXs(Arrays.asList(o)); }

	@SuppressWarnings({"UnstableApiUsage", "rawtypes", "RedundantSuppression"})
	public static <N extends Number> List<N> extractXs(Iterable<? extends XY<N, ?>> o) { return Streams.stream(o).map(XY::getX).collect(Collectors.toList()); }

	@SuppressWarnings("varargs")
	@SafeVarargs
	public static <N extends Number> List<N> extractYs(XY<N, ?>... o) { return extractYs(Arrays.asList(o)); }

	@SuppressWarnings({"UnstableApiUsage", "rawtypes", "RedundantSuppression"})
	public static <N extends Number> List<N> extractYs(Iterable<? extends XY<N, ?>> o) { return Streams.stream(o).map(XY::getY).collect(Collectors.toList()); }


	/* SECTION getters & setters */

	public N getX() { return x; }

	public void setX(N x) { this.x = x; }

	public N getY() { return y; }

	public void setY(N y) { this.y = y; }


	/* SECTION methods */

	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T sum(Iterable<? extends XY<N, ?>> o) {
		T r = copy();
		r.x = Numbers.sum(r.getX(), extractXs(o)).orElseThrow(Globals::rethrowCaughtThrowableStatic);
		r.y = Numbers.sum(r.getY(), extractYs(o)).orElseThrow(Globals::rethrowCaughtThrowableStatic);
		return r;
	}

	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T max(Iterable<? extends XY<N, ?>> o) {
		T r = copy();
		r.x = Numbers.max(r.getX(), extractXs(o)).orElseThrow(Globals::rethrowCaughtThrowableStatic);
		r.y = Numbers.max(r.getY(), extractYs(o)).orElseThrow(Globals::rethrowCaughtThrowableStatic);
		return r;
	}

	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T min(Iterable<? extends XY<N, ?>> o) {
		T r = copy();
		r.x = Numbers.min(r.getX(), extractXs(o)).orElseThrow(Globals::rethrowCaughtThrowableStatic);
		r.y = Numbers.min(r.getY(), extractYs(o)).orElseThrow(Globals::rethrowCaughtThrowableStatic);
		return r;
	}

	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T negate() {
		T r = copy();
		r.x = Numbers.negate(r.getX()).orElseThrow(Globals::rethrowCaughtThrowableStatic);
		r.y = Numbers.negate(r.getY()).orElseThrow(Globals::rethrowCaughtThrowableStatic);
		return r;
	}

	@SuppressWarnings("varargs")
	@SafeVarargs
	public final T sumX(XY<N, ?>... o) { return sumX(Arrays.asList(o)); }

	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T sumX(Iterable<? extends XY<N, ?>> o) {
		T r = copy();
		r.x = Numbers.sum(r.getX(), extractXs(o)).orElseThrow(Globals::rethrowCaughtThrowableStatic);
		return r;
	}

	@SuppressWarnings("varargs")
	@SafeVarargs
	public final T sumY(XY<N, ?>... o) { return sumY(Arrays.asList(o)); }

	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T sumY(Iterable<? extends XY<N, ?>> o) {
		T r = copy();
		r.y = Numbers.sum(r.getY(), extractYs(o)).orElseThrow(Globals::rethrowCaughtThrowableStatic);
		return r;
	}

	@SuppressWarnings("varargs")
	@SafeVarargs
	public final T maxX(XY<N, ?>... o) { return maxX(Arrays.asList(o)); }

	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T maxX(Iterable<? extends XY<N, ?>> o) {
		T r = copy();
		r.x = Numbers.max(r.getX(), extractXs(o)).orElseThrow(Globals::rethrowCaughtThrowableStatic);
		return r;
	}

	@SuppressWarnings("varargs")
	@SafeVarargs
	public final T maxY(XY<N, ?>... o) { return maxY(Arrays.asList(o)); }

	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T maxY(Iterable<? extends XY<N, ?>> o) {
		T r = copy();
		r.y = Numbers.max(r.getY(), extractYs(o)).orElseThrow(Globals::rethrowCaughtThrowableStatic);
		return r;
	}

	@SuppressWarnings("varargs")
	@SafeVarargs
	public final T minX(XY<N, ?>... o) { return minX(Arrays.asList(o)); }

	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T minX(Iterable<? extends XY<N, ?>> o) {
		T r = copy();
		r.x = Numbers.min(getX(), extractXs(o)).orElseThrow(Globals::rethrowCaughtThrowableStatic);
		return r;
	}

	@SuppressWarnings("varargs")
	@SafeVarargs
	public final T minY(XY<N, ?>... o) { return minY(Arrays.asList(o)); }

	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T minY(Iterable<? extends XY<N, ?>> o) {
		T r = copy();
		r.y = Numbers.min(r.getY(), extractYs(o)).orElseThrow(Globals::rethrowCaughtThrowableStatic);
		return r;
	}

	@Override
	public T toImmutable() { return castUncheckedUnboxedNonnull(new Immutable<>(this)); }

	@Override
	public boolean isImmutable() { return false; }


	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public int hashCode() { return getHashCode(this, super::hashCode); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public boolean equals(Object o) { return isEqual(this, o, super::equals); }

	@SuppressWarnings("Convert2MethodRef")
	@Override
	@OverridingMethodsMustInvokeSuper
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T clone() { return ICloneable.clone(() -> super.clone()); }

	@Override
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public String toString() { return getToStringString(this, super.toString()); }


	/* SECTION static classes */

	@javax.annotation.concurrent.Immutable
	public static class Immutable<N extends Number, T extends Immutable<N, T>> extends XY<N, T> {
		/* SECTION constructors */

		public Immutable(XY<N, ?> copy) { this(copy.getX(), copy.getY()); }

		public Immutable(N x, N y) { super(tryToImmutableUnboxedNonnull(x), tryToImmutableUnboxedNonnull(y)); }


		/* SECTION getters & setters */

		@Override
		@Deprecated
		public void setX(N x) { throw rejectUnsupportedOperation(); }

		@Override
		@Deprecated
		public void setY(N y) { throw rejectUnsupportedOperation(); }


		/* SECTION methods */

		@Override
		@OverridingStatus(group = GROUP)
		public final T toImmutable() { return castUncheckedUnboxedNonnull(this); }

		@Override
		@OverridingStatus(group = GROUP)
		public final boolean isImmutable() { return true; }
	}
}
