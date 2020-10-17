package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.logging;

import org.slf4j.helpers.MessageFormatter;

import javax.annotation.Nullable;
import java.util.Optional;

public enum FormattingUtilities {
	;

	public static String formatSimpleParameterized(@Nullable CharSequence format, Object... arguments) {
		return MessageFormatter.arrayFormat(Optional.ofNullable(format).map(CharSequence::toString).orElse(null),
				arguments).getMessage();
	}
}
