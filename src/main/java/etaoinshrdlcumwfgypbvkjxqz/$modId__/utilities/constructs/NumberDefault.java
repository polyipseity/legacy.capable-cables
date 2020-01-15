package etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs;

import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Miscellaneous.Casts.castUnchecked;
import static etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.helpers.Throwables.unexpectedThrowable;

public abstract class NumberDefault<N extends NumberDefault<N>> extends Number implements IStructure<N>, INumberOperable<N> {
	/* SECTION methods */

	/** {@inheritDoc} */
	@Override
	public String toString() { return super.toString(); }

	/** {@inheritDoc} */
	@Override
	public int hashCode() { return super.hashCode(); }

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object o) { return super.equals(o); }

	/** {@inheritDoc} */
	@Override
	public N clone() { try { return castUnchecked(super.clone()); } catch (CloneNotSupportedException | ClassCastException e) { throw unexpectedThrowable(e); } }
}
