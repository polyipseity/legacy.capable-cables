package $group__.ui.core.mvvm.views.paths;

import java.util.List;

public interface IPath<T> {
	int size();

	default T getPathRoot() { return getAt(0); }

	T getAt(int depth);

	default T getPathEnd() { return getAt(-1); }

	List<? extends T> asList();
}
