package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;

import java.util.Optional;

public class UIParserUncheckedException
		extends RuntimeException {
	private static final long serialVersionUID = 4812798507277009748L;

	public UIParserUncheckedException() {}

	public UIParserUncheckedException(@Nullable CharSequence message) {
		super(Optional.ofNullable(message).map(CharSequence::toString).orElse(null));
	}

	public UIParserUncheckedException(@Nullable CharSequence message, @Nullable Throwable cause) {
		super(Optional.ofNullable(message).map(CharSequence::toString).orElse(null), cause);
	}

	public UIParserUncheckedException(@Nullable Throwable cause) {
		super(cause);
	}
}
