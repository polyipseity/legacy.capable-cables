package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.paths;

import java.util.Collection;

public class EmptyPathException
		extends RuntimeException {
	private static final long serialVersionUID = 760193064178512586L;

	public EmptyPathException() {}

	public EmptyPathException(String message) { super(message); }

	public static void checkPathData(Collection<?> data)
			throws EmptyPathException {
		if (data.size() <= 1)
			throw new EmptyPathException();
	}
}
