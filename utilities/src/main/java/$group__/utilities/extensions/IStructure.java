package $group__.utilities.extensions;

import $group__.annotations.InterfaceIntersection;
import $group__.traits.IStruct;
import $group__.utilities.concurrent.IImmutablizable;

@InterfaceIntersection
public interface IStructure<T extends IStructure<T, T>, I extends IImmutablizable<I>> extends IStruct, IStrictObject,
		IImmutablizable<I> { /* MARK empty */}
