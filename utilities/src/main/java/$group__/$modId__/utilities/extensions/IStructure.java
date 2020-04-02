package $group__.$modId__.utilities.extensions;

import $group__.$modId__.annotations.InterfaceIntersection;
import $group__.$modId__.traits.IStruct;
import $group__.$modId__.utilities.concurrent.IImmutablizable;

@InterfaceIntersection
public interface IStructure<T extends IStructure<T, T>, I extends IImmutablizable<I>> extends IStruct, IStrictObject,
		IImmutablizable<I> { /* MARK empty */}
