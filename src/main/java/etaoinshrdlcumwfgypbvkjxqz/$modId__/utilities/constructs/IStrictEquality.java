package etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs;

import javax.annotation.meta.When;

import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.variables.Constants.GROUP;

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
