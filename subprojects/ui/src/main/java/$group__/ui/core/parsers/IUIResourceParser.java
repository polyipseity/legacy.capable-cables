package $group__.ui.core.parsers;

import $group__.utilities.interfaces.IHasGenericClass;

public interface IUIResourceParser<T, R> extends IHasGenericClass<T> {
	void parse(R resource)
			throws Throwable;

	void reset();

	T construct();
}
