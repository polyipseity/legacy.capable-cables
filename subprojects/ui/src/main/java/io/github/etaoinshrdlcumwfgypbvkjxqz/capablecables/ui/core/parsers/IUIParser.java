package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers;

public interface IUIParser<T, R> {
	void parse(R resource)
			throws UIParserCheckedException, UIParserUncheckedException;

	void reset();

	T construct()
			throws UIParserCheckedException, UIParserUncheckedException;
}
