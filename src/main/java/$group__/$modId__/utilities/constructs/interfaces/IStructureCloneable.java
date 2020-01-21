package $group__.$modId__.utilities.constructs.interfaces;

import $group__.$modId__.utilities.constructs.interfaces.extensions.ICloneable;

public interface IStructureCloneable<T extends IStructureCloneable<T>> extends IStructure<T>, ICloneable<T> {
	/* SECTION methods */

	@Override
	String toString();

	@Override
	int hashCode();

	@Override
	boolean equals(Object o);

	@SuppressWarnings("override")
	T clone();
}
