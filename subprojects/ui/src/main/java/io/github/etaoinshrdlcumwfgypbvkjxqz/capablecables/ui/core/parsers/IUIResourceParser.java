package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.interfaces.IHasGenericClass;

public interface IUIResourceParser<T, R> extends IHasGenericClass<T> {
	void parse(R resource)
			throws UIParserCheckedException, UIParserUncheckedException;

	void reset();

	T construct()
			throws UIParserCheckedException, UIParserUncheckedException;
}
