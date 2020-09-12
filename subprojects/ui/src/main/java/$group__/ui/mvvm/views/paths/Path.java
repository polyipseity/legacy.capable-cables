package $group__.ui.mvvm.views.paths;

import $group__.ui.core.mvvm.views.paths.IPath;
import $group__.utilities.AssertionUtilities;
import com.google.common.collect.ImmutableList;

import java.util.List;

public class Path<T>
		implements IPath<T> {
	protected final List<T> data;

	public Path(List<? extends T> data) {
		this.data = ImmutableList.copyOf(data);
	}

	@Override
	public int size() { return getData().size(); }

	@Override
	public T getAt(int depth) { return AssertionUtilities.assertNonnull(getData().get(Math.floorMod(depth, size()))); }

	@Override
	public List<T> asList() { return ImmutableList.copyOf(getData()); }

	protected List<T> getData() { return data; }
}
