package $group__.logging;

import $group__.annotations.OverridingStatus;
import $group__.utilities.Constants;
import $group__.utilities.extensions.IStructure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import static $group__.utilities.extensions.IStrictEquals.areEqual;
import static $group__.utilities.extensions.IStrictHashCode.getHashCode;
import static $group__.utilities.extensions.IStrictToString.getToStringString;
import static $group__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.utilities.helpers.PreconditionsExtension.requireRunOnceOnly;

@Immutable
public final class LoggingNull<T> implements ILogging<T>, IStructure<LoggingNull<T>, LoggingNull<T>> {
	/* SECTION static variables */

	public static final LoggingNull<?> INSTANCE = new LoggingNull<>();

	private static final Logger LOGGER = LogManager.getLogger(LoggingNull.class);


	/* SECTION constructors */

	protected LoggingNull() { requireRunOnceOnly(LOGGER); }

	@SuppressWarnings("unused")
	protected LoggingNull(LoggingNull<?> copy) { this(); }


	/* SECTION static methods */

	public static <T> LoggingNull<T> getInstance() { return castUncheckedUnboxedNonnull(INSTANCE); }


	/* SECTION methods */

	@Nullable
	@Override
	public T getLogger() { return null; }

	@Override
	public boolean trySetLogger(@Nullable T logger) { return false; }


	@Override
	@OverridingStatus(group = Constants.PACKAGE)
	@Deprecated
	public final LoggingNull<T> toImmutable() { return this; }

	@Override
	@OverridingStatus(group = Constants.PACKAGE)
	public final boolean isImmutable() { return true; }


	@Override
	public final String toString() { return getToStringString(this, super.toString()); }

	@Override
	public final int hashCode() { return getHashCode(this, super::hashCode); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public final boolean equals(Object obj) { return areEqual(this, obj, super::equals); }
}
