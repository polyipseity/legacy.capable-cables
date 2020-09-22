package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures.paths;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IAffineTransformStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.paths.IUIComponentPathResolver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.CacheUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.ManualLoadingCache;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class AbstractPathResolver<T extends IUIComponent>
		implements IUIComponentPathResolver<T> {
	protected final LoadingCache<T, List<T>> virtualElements =
			ManualLoadingCache.newNestedLoadingCacheCollection(CacheUtilities.newCacheBuilderSingleThreaded()
					.initialCapacity(CapacityUtilities.INITIAL_CAPACITY_SMALL)
					.weakKeys()
					.build(CacheLoader.from(() -> new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_SMALL))));
	protected final LoadingCache<IUIComponent, List<Consumer<? super IAffineTransformStack>>> childrenTransformers =
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

	@Override
	public boolean addChildrenTransformer(T element, Consumer<? super IAffineTransformStack> transformer) { return getChildrenTransformers().getUnchecked(element).add(transformer); }

	protected LoadingCache<IUIComponent, List<Consumer<? super IAffineTransformStack>>> getChildrenTransformers() { return childrenTransformers; }

	@Override
	public boolean removeChildrenTransformer(T element, Consumer<? super IAffineTransformStack> transformer) {
		boolean ret = Optional.ofNullable(getChildrenTransformers().getIfPresent(element))
				.filter(ts -> ts.remove(transformer))
				.isPresent();
		if (ret)
			getChildrenTransformers().cleanUp();
		return ret;
	}
}
