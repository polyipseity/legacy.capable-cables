package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.logging;

import org.slf4j.helpers.MessageFormatter;

import javax.annotation.Nullable;

public enum FormattingUtilities {
	;

	public static String formatSimpleParameterized(@Nullable String format, Object... arguments) { return MessageFormatter.arrayFormat(format, arguments).getMessage(); }
}
