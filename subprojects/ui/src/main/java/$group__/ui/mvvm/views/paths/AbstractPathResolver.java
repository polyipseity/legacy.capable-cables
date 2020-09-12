package $group__.ui.mvvm.views.paths;

import $group__.ui.core.mvvm.views.paths.IPathResolver;
import $group__.utilities.CapacityUtilities;
import $group__.utilities.collections.CacheUtilities;
import $group__.utilities.collections.ManualLoadingCache;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractPathResolver<T>
		implements IPathResolver<T> {
	protected final LoadingCache<T, List<T>> virtualElements =
			ManualLoadingCache.newNestedLoadingCacheCollection(CacheUtilities.newCacheBuilderSingleThreaded()
					.initialCapacity(CapacityUtilities.INITIAL_CAPACITY_SMALL)
					.weakKeys()
					.build(CacheLoader.from(() -> new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_SMALL))));

	@Override
	public boolean addVirtualElement(T element,
	                                 T virtualElement) {
		return getVirtualElements().getUnchecked(element).add(virtualElement);
	}

	@Override
	public boolean removeVirtualElement(T element,
	                                    T virtualElement) {
		boolean ret = Optional.ofNullable(getVirtualElements().getIfPresent(element))
				.filter(ve -> ve.remove(virtualElement))
				.isPresent();
		if (ret)
			getVirtualElements().cleanUp();
		return ret;
	}

	@Override
	public boolean moveVirtualElementToTop(T element, T virtualElement) {
		@Nullable List<T> ve = getVirtualElements().getIfPresent(element);
		if (ve == null)
			return false;
		if (ve.remove(virtualElement)) {
			ve.add(ve.size(), virtualElement);
			return true;
		}
		return false;
	}

	protected LoadingCache<T, List<T>> getVirtualElements() { return virtualElements; }
}
