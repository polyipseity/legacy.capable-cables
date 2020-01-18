package $group__.$modId__.utilities.constructs.interfaces.basic;

import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;

import javax.annotation.meta.When;

import static $group__.$modId__.utilities.variables.Constants.GROUP;

public interface IStrictEquality extends IStrictHashCode, IStrictEquals {
	/* SECTION methods */

	/** {@inheritDoc} */
	@Override
	@OverridingStatus(group = GROUP, when = When.ALWAYS)
	int hashCode();

	/** {@inheritDoc} */
	@Override
	@OverridingStatus(group = GROUP, when = When.ALWAYS)
	boolean equals(Object o);
}
