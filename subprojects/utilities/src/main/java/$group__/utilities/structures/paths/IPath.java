package $group__.utilities.structures.paths;

import $group__.utilities.interfaces.ICopyable;

import java.util.List;

public interface IPath<T> extends ICopyable {
	int size();

	default boolean isRoot() { return size() == 1; }

	default T getPathRoot() { return getAt(0); }

	T getAt(int depth);

	default T getPathEnd() { return getAt(-1); }

	List<T> asList();

	IPath<T> getParentPath()
			throws EmptyPathException;

	void subPath(Iterable<? extends T> elements);

	void parentPath(int amount);

	@Override
	IPath<T> copy();
}
