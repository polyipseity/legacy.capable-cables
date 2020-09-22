package $group__.ui.core.parsers;

public class UIParserUncheckedException
		extends RuntimeException {
	private static final long serialVersionUID = 4812798507277009748L;

	public UIParserUncheckedException() {}

	public UIParserUncheckedException(String message) { super(message); }

	public UIParserUncheckedException(String message, Throwable cause) { super(message, cause); }

	public UIParserUncheckedException(Throwable cause) { super(cause); }
}
