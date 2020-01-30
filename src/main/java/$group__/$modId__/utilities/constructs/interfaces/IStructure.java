package $group__.$modId__.utilities.constructs.interfaces;

import $group__.$modId__.utilities.constructs.interfaces.basic.IImmutablizable;
import $group__.$modId__.utilities.constructs.interfaces.basic.IStruct;
import $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictEquality;
import $group__.$modId__.utilities.constructs.interfaces.extensions.IStrictToString;

public interface IStructure<T extends IStructure<T>> extends IStruct, IStrictToString, IStrictEquality, IImmutablizable<T> { /* MARK empty */ }
