package $group__.ui.core.parsers;

import $group__.utilities.interfaces.IHasGenericClass;

public interface IUIResourceParser<T, R> extends IHasGenericClass<T> {
	void parse(R resource)
			throws UIParserCheckedException, UIParserUncheckedException;

	void reset();

	T construct()
			throws UIParserCheckedException, UIParserUncheckedException;
}
