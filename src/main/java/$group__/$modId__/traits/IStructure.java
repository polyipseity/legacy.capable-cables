package $group__.$modId__.traits;

import $group__.$modId__.traits.basic.IImmutablizable;
import $group__.$modId__.traits.basic.IStruct;
import $group__.$modId__.traits.extensions.IStrictEquality;
import $group__.$modId__.traits.extensions.IStrictToString;

public interface IStructure<T extends IStructure<T>> extends IStruct, IStrictToString, IStrictEquality, IImmutablizable<T> { /* MARK empty */}
