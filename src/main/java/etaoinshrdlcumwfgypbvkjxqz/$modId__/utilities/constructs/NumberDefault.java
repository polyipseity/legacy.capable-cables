package etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs;

import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Miscellaneous.Casts.castUnchecked;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Throwables.unexpectedThrowable;

public abstract class NumberDefault<T extends NumberDefault<T>> extends Number implements IStructure<T>, INumberOperable<T> {
	/* SECTION methods */

	/** {@inheritDoc} */
	@SuppressWarnings("EmptyMethod")
	@Override
	public String toString() { return super.toString(); }

	/** {@inheritDoc} */
	@Override
	public T clone() { try { return castUnchecked(super.clone()); } catch (CloneNotSupportedException | ClassCastException e) { throw unexpectedThrowable(e); } }
}
