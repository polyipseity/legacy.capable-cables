package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;

import java.util.Optional;

public class UIParserCheckedException
		extends Exception {
	private static final long serialVersionUID = -1163347391429396259L;

	public UIParserCheckedException() {}

	public UIParserCheckedException(@Nullable CharSequence message) {
		super(Optional.ofNullable(message).map(CharSequence::toString).orElse(null));
	}

	public UIParserCheckedException(@Nullable CharSequence message, @Nullable Throwable cause) {
		super(Optional.ofNullable(message).map(CharSequence::toString).orElse(null), cause);
	}

	public UIParserCheckedException(@Nullable Throwable cause) {
		super(cause);
	}
}
