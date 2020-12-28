package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.logging;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import org.slf4j.helpers.MessageFormatter;

import java.util.Optional;

public enum FormattingUtilities {
	;

	public static String formatSimpleParameterized(@Nullable CharSequence format, Object... arguments) {
		return MessageFormatter.arrayFormat(Optional.ofNullable(format).map(CharSequence::toString).orElse(null),
				arguments).getMessage();
	}
}
