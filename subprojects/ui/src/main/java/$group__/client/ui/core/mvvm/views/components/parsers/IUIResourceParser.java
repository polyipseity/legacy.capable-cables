package $group__.client.ui.core.mvvm.views.components.parsers;

import $group__.utilities.interfaces.IHasGenericClass;

public interface IUIResourceParser<T, R> extends IHasGenericClass<T> {
	void parse(R resource)
			throws Throwable;

	void reset();

	T construct();
}
