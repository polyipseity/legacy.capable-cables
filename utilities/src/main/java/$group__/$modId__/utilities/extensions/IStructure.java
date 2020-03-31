package $group__.$modId__.utilities.extensions;

import $group__.$modId__.annotations.InterfaceIntersection;
import $group__.$modId__.utilities.concurrent.IImmutablizable;
import $group__.$modId__.traits.IStruct;

@InterfaceIntersection
public interface IStructure<T extends IStructure<T, T>, I extends IImmutablizable<I>> extends IStruct, IStrictObject, IImmutablizable<I> { /* MARK empty */ }
