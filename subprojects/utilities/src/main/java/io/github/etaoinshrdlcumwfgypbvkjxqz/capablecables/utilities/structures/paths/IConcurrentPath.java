package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.paths;

public interface IConcurrentPath<T> extends IPath<T> {
	@Override
	IConcurrentPath<T> getParentPath()
			throws EmptyPathException;
}
