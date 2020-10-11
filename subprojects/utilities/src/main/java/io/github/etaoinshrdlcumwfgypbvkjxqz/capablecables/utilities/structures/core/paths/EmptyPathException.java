package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.paths;

public class EmptyPathException
		extends IllegalStateException {
	private static final long serialVersionUID = 760193064178512586L;

	public EmptyPathException() {}

	public EmptyPathException(String message) { super(message); }

	public static void checkSize(int size)
			throws EmptyPathException {
		if (size < 0)
			throw new EmptyPathException();
	}
}
