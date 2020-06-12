package $group__.client.gui.coordinates;

import $group__.annotations.OverridingStatus;
import $group__.logging.ILogging;
import $group__.logging.ILoggingUser;
import $group__.traits.IOperable;
import $group__.utilities.Constants;
import $group__.utilities.builders.BuilderStructure;
import $group__.utilities.concurrent.IMutatorImmutablizable;
import $group__.utilities.concurrent.IMutatorUser;
import $group__.utilities.extensions.ICloneable;
import $group__.utilities.extensions.IStructure;
import $group__.utilities.helpers.specific.Numbers;
import $group__.utilities.helpers.specific.Throwables;
import com.google.common.collect.Streams;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.meta.When;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static $group__.utilities.concurrent.IMutator.trySetNonnull;
import static $group__.utilities.extensions.IStrictEquals.areEqual;
import static $group__.utilities.extensions.IStrictHashCode.getHashCode;
import static $group__.utilities.extensions.IStrictToString.getToStringString;
import static $group__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;

@SideOnly(Side.CLIENT)
public class XY<T extends XY<T, N>, N extends Number> implements IStructure<T, T>, ICloneable<T>, IOperable<T, XY<?,
		N>>, IMutatorUser<IMutatorImmutablizable<?, ?>>, ILoggingUser<ILogging<Logger>, Logger> {
	/* SECTION variables */

	protected N x;
	protected N y;

	protected IMutatorImmutablizable<?, ?> mutator;
	protected ILogging<Logger> logging;


	/* SECTION constructors */

	public XY(N x, N y, IMutatorImmutablizable<?, ?> mutator, ILogging<Logger> logging) {
		this.mutator = trySetNonnull(mutator, mutator, true);
		this.logging = trySetNonnull(getMutator(), logging, true);
		this.x = trySetNonnull(getMutator(), x, true);
		this.y = trySetNonnull(getMutator(), y, true);
	}

	public XY(XY<?, ? extends N> copy) { this(copy, copy.getMutator()); }


	protected XY(XY<?, ? extends N> copy, IMutatorImmutablizable<?, ?> mutator) {
		this(copy.getX(), copy.getY(),
				mutator, copy.getLogging());
	}


	/* SECTION static methods */

	public static <T extends BuilderStructure<T, V>, V extends XY<V, N>, N extends Number> BuilderStructure<T, V> newBuilderXY(N x, N y) { return new BuilderStructure<>(t -> castUncheckedUnboxedNonnull(new XY<>(x, y, t.mutator, t.logging))); }


	@SuppressWarnings("varargs")
	@SafeVarargs
	public static <N extends Number> List<N> extractXs(XY<?, N>... o) { return extractXs(Arrays.asList(o)); }

	@SuppressWarnings({"UnstableApiUsage", "rawtypes", "RedundantSuppression"})
	public static <N extends Number> List<N> extractXs(Iterable<? extends XY<?, N>> o) { return Streams.stream(o).map(XY::getX).collect(Collectors.toList()); }

	@SuppressWarnings("varargs")
	@SafeVarargs
	public static <N extends Number> List<N> extractYs(XY<?, N>... o) { return extractYs(Arrays.asList(o)); }

	@SuppressWarnings({"UnstableApiUsage", "rawtypes", "RedundantSuppression"})
	public static <N extends Number> List<N> extractYs(Iterable<? extends XY<?, N>> o) { return Streams.stream(o).map(XY::getY).collect(Collectors.toList()); }


	/* SECTION getters & setters */

	public N getX() { return x; }

	public void setX(N x) { this.x = x; }

	public N getY() { return y; }

	public void setY(N y) { this.y = y; }

	@Override
	public IMutatorImmutablizable<?, ?> getMutator() { return mutator; }

	@Override
	public boolean trySetMutator(IMutatorImmutablizable<?, ?> mutator) {
		return trySet(t -> this.mutator = t,
				mutator);
	}

	@Override
	public ILogging<Logger> getLogging() { return logging; }

	@Override
	public boolean trySetLogging(ILogging<Logger> logging) { return trySet(t -> this.logging = t, logging); }


	/* SECTION methods */

	@Override
	@OverridingStatus(group = Constants.PACKAGE, when = When.MAYBE)
	public T sum(Iterable<? extends XY<?, N>> o) {
		T r = copy();
		r.x = Numbers.sum(r.getX(), extractXs(o), getLogger()).orElseThrow(Throwables::rethrowCaughtThrowableStatic);
		r.y = Numbers.sum(r.getY(), extractYs(o), getLogger()).orElseThrow(Throwables::rethrowCaughtThrowableStatic);
		return r;
	}

	@Override
	@OverridingStatus(group = Constants.PACKAGE, when = When.MAYBE)
	public T max(Iterable<? extends XY<?, N>> o) {
		T r = copy();
		r.x = Numbers.max(r.getX(), extractXs(o), getLogger()).orElseThrow(Throwables::rethrowCaughtThrowableStatic);
		r.y = Numbers.max(r.getY(), extractYs(o), getLogger()).orElseThrow(Throwables::rethrowCaughtThrowableStatic);
		return r;
	}

	@Override
	@OverridingStatus(group = Constants.PACKAGE, when = When.MAYBE)
	public T min(Iterable<? extends XY<?, N>> o) {
		T r = copy();
		r.x = Numbers.min(r.getX(), extractXs(o), getLogger()).orElseThrow(Throwables::rethrowCaughtThrowableStatic);
		r.y = Numbers.min(r.getY(), extractYs(o), getLogger()).orElseThrow(Throwables::rethrowCaughtThrowableStatic);
		return r;
	}

	@Override
	@OverridingStatus(group = Constants.PACKAGE, when = When.MAYBE)
	public T negate() {
		T r = copy();
		r.x = Numbers.negate(r.getX(), getLogger()).orElseThrow(Throwables::rethrowCaughtThrowableStatic);
		r.y = Numbers.negate(r.getY(), getLogger()).orElseThrow(Throwables::rethrowCaughtThrowableStatic);
		return r;
	}

	@SuppressWarnings("varargs")
	@SafeVarargs
	public final T sumX(XY<?, N>... o) { return sumX(Arrays.asList(o)); }

	@OverridingStatus(group = Constants.PACKAGE, when = When.MAYBE)
	public T sumX(Iterable<? extends XY<?, N>> o) {
		T r = copy();
		r.x = Numbers.sum(r.getX(), extractXs(o), getLogger()).orElseThrow(Throwables::rethrowCaughtThrowableStatic);
		return r;
	}

	@SuppressWarnings("varargs")
	@SafeVarargs
	public final T sumY(XY<?, N>... o) { return sumY(Arrays.asList(o)); }

	@OverridingStatus(group = Constants.PACKAGE, when = When.MAYBE)
	public T sumY(Iterable<? extends XY<?, N>> o) {
		T r = copy();
		r.y = Numbers.sum(r.getY(), extractYs(o), getLogger()).orElseThrow(Throwables::rethrowCaughtThrowableStatic);
		return r;
	}

	@SuppressWarnings("varargs")
	@SafeVarargs
	public final T maxX(XY<?, N>... o) { return maxX(Arrays.asList(o)); }

	@OverridingStatus(group = Constants.PACKAGE, when = When.MAYBE)
	public T maxX(Iterable<? extends XY<?, N>> o) {
		T r = copy();
		r.x = Numbers.max(r.getX(), extractXs(o), getLogger()).orElseThrow(Throwables::rethrowCaughtThrowableStatic);
		return r;
	}

	@SuppressWarnings("varargs")
	@SafeVarargs
	public final T maxY(XY<?, N>... o) { return maxY(Arrays.asList(o)); }

	@OverridingStatus(group = Constants.PACKAGE, when = When.MAYBE)
	public T maxY(Iterable<? extends XY<?, N>> o) {
		T r = copy();
		r.y = Numbers.max(r.getY(), extractYs(o), getLogger()).orElseThrow(Throwables::rethrowCaughtThrowableStatic);
		return r;
	}

	@SuppressWarnings("varargs")
	@SafeVarargs
	public final T minX(XY<?, N>... o) { return minX(Arrays.asList(o)); }

	@OverridingStatus(group = Constants.PACKAGE, when = When.MAYBE)
	public T minX(Iterable<? extends XY<?, N>> o) {
		T r = copy();
		r.x = Numbers.min(getX(), extractXs(o), getLogger()).orElseThrow(Throwables::rethrowCaughtThrowableStatic);
		return r;
	}

	@SuppressWarnings("varargs")
	@SafeVarargs
	public final T minY(XY<?, N>... o) { return minY(Arrays.asList(o)); }

	@OverridingStatus(group = Constants.PACKAGE, when = When.MAYBE)
	public T minY(Iterable<? extends XY<?, N>> o) {
		T r = copy();
		r.y = Numbers.min(r.getY(), extractYs(o), getLogger()).orElseThrow(Throwables::rethrowCaughtThrowableStatic);
		return r;
	}


	@Override
	public T toImmutable() {
		return castUncheckedUnboxedNonnull(isImmutable() ? this : new XY<>(this,
				IMutatorImmutablizable.of(getMutator().toImmutable())));
	}

	@Override
	public boolean isImmutable() { return getMutator().isImmutable(); }


	@Override
	@OverridingStatus(group = Constants.PACKAGE, when = When.MAYBE)
	public int hashCode() { return getHashCode(this, super::hashCode); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	@OverridingStatus(group = Constants.PACKAGE, when = When.MAYBE)
	public boolean equals(Object o) { return areEqual(this, o, super::equals); }

	@SuppressWarnings("Convert2MethodRef")
	@Override
	@OverridingMethodsMustInvokeSuper
	@OverridingStatus(group = Constants.PACKAGE, when = When.MAYBE)
	public T clone() { return ICloneable.clone(() -> super.clone(), getLogger()); }

	@Override
	@OverridingStatus(group = Constants.PACKAGE, when = When.MAYBE)
	public String toString() { return getToStringString(this, super.toString()); }
}
