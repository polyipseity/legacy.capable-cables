package $group__.$modId__.utilities.constructs.classes;

import $group__.$modId__.utilities.constructs.interfaces.IStructure;
import $group__.$modId__.utilities.constructs.interfaces.basic.IOperable;

import static $group__.$modId__.utilities.helpers.Miscellaneous.Casts.castUnchecked;
import static $group__.$modId__.utilities.helpers.Throwables.unexpectedThrowable;

public abstract class NumberDefault<T extends NumberDefault<T>> extends Number implements IStructure<T>, IOperable.INumberOperable<T> {
	/* SECTION methods */

	/** {@inheritDoc} */
	@SuppressWarnings("EmptyMethod")
	@Override
	public String toString() { return super.toString(); }

	/** {@inheritDoc} */
	@Override
	public T clone() { try { return castUnchecked(super.clone()); } catch (CloneNotSupportedException e) { throw unexpectedThrowable(e); } }
}
