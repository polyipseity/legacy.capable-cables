package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.naming;

public class DuplicateNameException
		extends IllegalStateException {
	private static final long serialVersionUID = 8045556356079069945L;

	public DuplicateNameException() {}

	public DuplicateNameException(String s) {
		super(s);
	}

	public DuplicateNameException(String message, Throwable cause) {
		super(message, cause);
	}

	public DuplicateNameException(Throwable cause) {
		super(cause);
	}
}
