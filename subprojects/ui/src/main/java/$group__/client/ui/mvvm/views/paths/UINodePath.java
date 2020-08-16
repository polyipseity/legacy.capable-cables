package $group__.client.ui.mvvm.views.paths;

import $group__.client.ui.mvvm.core.views.paths.IUINode;
import $group__.client.ui.mvvm.core.views.paths.IUINodePath;
import com.google.common.collect.ImmutableList;

import java.util.List;

public class UINodePath<T extends IUINode>
		implements IUINodePath {
	protected final List<T> delegated;

	public UINodePath(List<? extends T> delegated) {
		this.delegated = ImmutableList.copyOf(delegated);
	}

	@Override
	public int size() { return getDelegated().size(); }

	@Override
	public T getAt(int depth) { return getDelegated().get(Math.floorMod(depth, size())); }

	@Override
	public List<T> asList() { return ImmutableList.copyOf(getDelegated()); }

	protected List<T> getDelegated() { return delegated; }
}
