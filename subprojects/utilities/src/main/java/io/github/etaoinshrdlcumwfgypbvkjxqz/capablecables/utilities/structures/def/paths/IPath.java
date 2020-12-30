package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.paths;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.ICloneable;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public interface IPath<T>
		extends ICloneable {
	default boolean isRoot() { return size() == 1; }

	int size();

	default Optional<? extends T> getPathRoot() { return getAt(0); }

	Optional<? extends T> getAt(int depth);

	default Optional<? extends T> getPathEnd() { return getAt(-1); }

	List<T> asList();

	void subPath(Iterator<? extends T> elements);

	void parentPath(int amount)
			throws EmptyPathException;

	boolean isEmpty();

	@Override
	IPath<T> clone() throws CloneNotSupportedException;
}
