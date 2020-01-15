package etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.meta.When;

import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.variables.Constants.GROUP;

public interface ICloneable<C> extends Cloneable {
	/* SECTION methods */

	/** {@inheritDoc} */
	@OverridingMethodsMustInvokeSuper
	@OverridingStatus(group = GROUP, when = When.ALWAYS)
	C clone();


	@OverridingStatus(group = GROUP, when = When.NEVER)
	default C copy() { return clone(); }
}
