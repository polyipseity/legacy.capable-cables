package etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs;

public interface IStructure<S extends IStructure<S>> extends IStrictToString, IStrictEquality, ICloneable<S>, IImmutablizable<S> {
	/* SECTION methods */

	/** {@inheritDoc} */
	@Override
	String toString();

	/** {@inheritDoc} */
	@Override
	int hashCode();

	/** {@inheritDoc} */
	@Override
	boolean equals(Object o);

	/** {@inheritDoc} */
	S clone();
}
