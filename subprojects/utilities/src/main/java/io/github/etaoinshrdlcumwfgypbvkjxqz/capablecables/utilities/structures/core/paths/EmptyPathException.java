package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.paths;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;

import java.util.Optional;

public class EmptyPathException
		extends IllegalStateException {
	private static final long serialVersionUID = 760193064178512586L;

	public EmptyPathException() {}

	public EmptyPathException(@Nullable CharSequence message) {
		super(Optional.ofNullable(message).map(CharSequence::toString).orElse(null));
	}

	public static void checkSize(int size)
			throws EmptyPathException {
		if (size < 0)
			throw new EmptyPathException();
	}
}
