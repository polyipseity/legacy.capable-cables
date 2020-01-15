package etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs;

public interface IStructure<T extends IStructure<T>> extends IStrictToString, IStrictEquality, ICloneable<T>, IImmutablizable<T> {
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
	T clone();
}
