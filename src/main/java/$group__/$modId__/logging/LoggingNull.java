package $group__.$modId__.logging;

import $group__.$modId__.annotations.OverridingStatus;
import $group__.$modId__.utilities.extensions.IStructure;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import static $group__.$modId__.utilities.extensions.IStrictEquals.areEqual;
import static $group__.$modId__.utilities.extensions.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.extensions.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.helpers.Casts.castUncheckedUnboxedNonnull;
import static $group__.$modId__.utilities.helpers.PreconditionsExtension.requireRunOnceOnly;
import static $group__.$modId__.utilities.variables.Constants.GROUP;
import static $group__.$modId__.utilities.variables.Globals.LOGGER;

@Immutable
public final class LoggingNull<T> implements ILogging<T>, IStructure<LoggingNull<T>, LoggingNull<T>> {
	/* SECTION static variables */

	public static final LoggingNull<?> INSTANCE = new LoggingNull<>();


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
	@OverridingStatus(group = GROUP)
	@Deprecated
	public final LoggingNull<T> toImmutable() { return this; }

	@Override
	@OverridingStatus(group = GROUP)
	public final boolean isImmutable() { return true; }


	@Override
	public final String toString() { return getToStringString(this, super.toString()); }

	@Override
	public final int hashCode() { return getHashCode(this, super::hashCode); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public final boolean equals(Object obj) { return areEqual(this, obj, super::equals); }
}
