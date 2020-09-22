package $group__.ui.core.parsers;

public class UIParserCheckedException
		extends Exception {
	private static final long serialVersionUID = -1163347391429396259L;

	public UIParserCheckedException() {}

	public UIParserCheckedException(String message) { super(message); }

	public UIParserCheckedException(String message, Throwable cause) { super(message, cause); }

	public UIParserCheckedException(Throwable cause) { super(cause); }
}
