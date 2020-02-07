package $group__.$modId__.utilities;

import $group__.$modId__.annotations.OverridingStatus;
import $group__.$modId__.traits.IStructureCloneable;
import $group__.$modId__.traits.basic.ILogging;
import $group__.$modId__.traits.basic.IOperable;
import $group__.$modId__.traits.extensions.ICloneable;
import org.apache.logging.log4j.Logger;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.meta.When;

import static $group__.$modId__.traits.extensions.IStrictEquals.isEqual;
import static $group__.$modId__.traits.extensions.IStrictHashCode.getHashCode;
import static $group__.$modId__.traits.extensions.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.variables.Constants.GROUP;

public abstract class NumberDefault<T extends NumberDefault<T>> extends Number implements IStructureCloneable<T>, IOperable.INumberOperable<T>, ILogging {
	/* SECTION static variables */

	private static final long serialVersionUID = 982068990995544271L;


	/* SECTION variables */

	protected Logger logger;


	/* SECTION constructors */

	protected NumberDefault(Logger logger) { this.logger = logger; }


	/* SECTION methods */

	@Override
	public Logger getLogger() { return logger; }

	@Override
	public void setLogger(Logger logger) { this.logger = logger; }


	@Override
	@OverridingStatus(group = GROUP)
	public final String toString() { return getToStringString(this, super.toString()); }

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	@OverridingStatus(group = GROUP)
	public final boolean equals(Object o) { return isEqual(this, o, super::equals); }

	@Override
	@OverridingStatus(group = GROUP)
	public final int hashCode() { return getHashCode(this, super::hashCode); }

	@SuppressWarnings("Convert2MethodRef")
	@Override
	@OverridingMethodsMustInvokeSuper
	@OverridingStatus(group = GROUP, when = When.MAYBE)
	public T clone() { return ICloneable.clone(() -> super.clone(), getLogger()); }
}
