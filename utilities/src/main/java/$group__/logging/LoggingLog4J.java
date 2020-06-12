package $group__.logging;

import $group__.annotations.OverridingStatus;
import $group__.utilities.Constants;
import $group__.utilities.concurrent.IMutatorImmutablizable;
import $group__.utilities.concurrent.IMutatorUser;
import $group__.utilities.extensions.ICloneable;
import $group__.utilities.extensions.IStructure;
import org.apache.logging.log4j.Logger;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.meta.When;

import static $group__.utilities.concurrent.IMutator.trySetNonnull;
import static $group__.utilities.extensions.IStrictEquals.areEqual;
import static $group__.utilities.extensions.IStrictHashCode.getHashCode;
import static $group__.utilities.extensions.IStrictToString.getToStringString;
import static $group__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;

public class LoggingLog4J<T extends LoggingLog4J<T, L>, L extends Logger> implements ILogging<L>,
		IMutatorUser<IMutatorImmutablizable<?, ?>>, IStructure<T, T>, ICloneable<T> {
	/* SECTION variables */

	protected IMutatorImmutablizable<?, ?> mutator;
	protected L logger;


	/* SECTION constructors */

	public LoggingLog4J(L logger, IMutatorImmutablizable<?, ?> mutator) {
		this.mutator = trySetNonnull(mutator, mutator, true);
		this.logger = trySetNonnull(getMutator(), logger, true);
	}

	public LoggingLog4J(LoggingLog4J<?, L> copy) { this(copy, copy.getMutator()); }


	protected LoggingLog4J(LoggingLog4J<?, L> copy, IMutatorImmutablizable<?, ?> mutator) {
		this(copy.getLogger(),
				mutator);
	}


	/* SECTION getters & setters */

	@Nonnull
	@Override
	public L getLogger() { return logger; }

	@Override
	public boolean trySetLogger(@Nullable @CheckForNull L logger) {
		return logger != null && trySet(t -> this.logger =
				t, logger);
	}

	@Nonnull
	@Override
	public IMutatorImmutablizable<?, ?> getMutator() { return mutator; }

	@Override
	public boolean trySetMutator(IMutatorImmutablizable<?, ?> mutator) {
		return trySet(t -> this.mutator = t,
				mutator);
	}


	/* SECTION methods */

	@Override
	public T toImmutable() {
		return castUncheckedUnboxedNonnull(isImmutable() ? this : new LoggingLog4J<>(this,
				IMutatorImmutablizable.of(getMutator().toImmutable())));
	}

	@Override
	public boolean isImmutable() { return false; }


	@Override
	public final String toString() { return getToStringString(this, super.toString()); }

	@Override
	public final int hashCode() { return getHashCode(this, super::hashCode); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public final boolean equals(Object obj) { return areEqual(this, obj, super::equals); }

	@SuppressWarnings("Convert2MethodRef")
	@Override
	@OverridingMethodsMustInvokeSuper
	@OverridingStatus(group = Constants.PACKAGE, when = When.MAYBE)
	public T clone() { return ICloneable.clone(() -> super.clone(), getLogger()); }
}
