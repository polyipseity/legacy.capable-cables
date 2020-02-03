package $group__.$modId__.utilities.constructs.classes;

import $group__.$modId__.utilities.constructs.interfaces.IStructureCloneable;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
import $group__.$modId__.utilities.constructs.interfaces.basic.IOperable;
import $group__.$modId__.utilities.constructs.interfaces.extensions.ICloneable;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.meta.When;

import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictEquals.isEqual;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictHashCode.getHashCode;
import static $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictToString.getToStringString;
import static $group__.$modId__.utilities.variables.Constants.GROUP;

public abstract class NumberDefault<T extends NumberDefault<T>> extends Number implements IStructureCloneable<T>, IOperable.INumberOperable<T> {
	/* SECTION static variables */

	private static final long serialVersionUID = 982068990995544271L;


	/* SECTION methods */

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
	public T clone() { return ICloneable.clone(() -> super.clone()); }
}
