package $group__.utilities;

import org.slf4j.helpers.MessageFormatter;

public enum FormattingUtilities {
	;

	public static String formatSimpleParameterized(String format, Object... arguments) { return MessageFormatter.arrayFormat(format, arguments).getMessage(); }
}
