package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.naming;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;

import java.util.Optional;

public class DuplicateNameException
		extends IllegalStateException {
	private static final long serialVersionUID = 8045556356079069945L;

	public DuplicateNameException() {}

	public DuplicateNameException(@Nullable CharSequence message) {
		super(Optional.ofNullable(message).map(CharSequence::toString).orElse(null));
	}

	public DuplicateNameException(@Nullable CharSequence message, @Nullable Throwable cause) {
		super(Optional.ofNullable(message).map(CharSequence::toString).orElse(null), cause);
	}

	public DuplicateNameException(@Nullable Throwable cause) {
		super(cause);
	}
}
