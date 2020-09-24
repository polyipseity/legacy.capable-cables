package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures.shapes.interactions;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.interactions.IShapeAnchor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.interactions.IShapeAnchorController;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.interactions.IShapeAnchorSet;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.interactions.IShapeDescriptorProvider;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.CacheUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.ManualLoadingCache;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.ConstantSupplier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.MappableSupplier;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class DefaultShapeAnchorController<T extends IShapeDescriptorProvider>
		implements IShapeAnchorController<T> {
	protected final LoadingCache<T, IShapeAnchorSet> anchorSets =
			ManualLoadingCache.newNestedLoadingCache(CacheUtilities.newCacheBuilderSingleThreaded()
							.initialCapacity(CapacityUtilities.INITIAL_CAPACITY_MEDIUM)
							.weakKeys()
							.build(CacheLoader.from(ShapeAnchorSet::new)),
					e -> AssertionUtilities.assertNonnull(e.getValue()).isEmpty());
	protected final LoadingCache<IShapeDescriptorProvider, Set<IShapeAnchor>> subscribersMap =
			ManualLoadingCache.newNestedLoadingCacheCollection(CacheUtilities.newCacheBuilderSingleThreaded()
					.initialCapacity(CapacityUtilities.INITIAL_CAPACITY_MEDIUM)
					.weakKeys()
					.build(CacheLoader.from(
							new MappableSupplier<>(ConstantSupplier.of(MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_SMALL)))
									.map(t -> Collections.newSetFromMap(t.makeMap())))));

	@Override
	public Map<? extends T, ? extends IShapeAnchorSet> getAnchorSetsView() { return ImmutableMap.copyOf(getAnchorSets().asMap()); }

	@Override
	public boolean addAnchors(T origin, Iterable<? extends IShapeAnchor> anchors) {
		IShapeAnchorSet as = getAnchorSets().getUnchecked(origin);
		boolean ret = as.addAnchors(anchors);
		as.getAnchorsView().values().stream().unordered()
				.forEach(a ->
						a.getTarget().ifPresent(t ->
								getSubscribersMap().getUnchecked(t).add(a)));
		as.anchor(origin);
		return ret;
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public boolean removeAnchors(T origin, Iterable<? extends IShapeAnchor> anchors) {
		@Nullable IShapeAnchorSet as = getAnchorSets().getIfPresent(origin);
		if (as == null)
			return false;
		boolean ret = as.removeAnchors(anchors);
		Streams.stream(anchors).unordered()
				.forEach(a ->
						a.getTarget().ifPresent(t ->
								getSubscribersMap().getUnchecked(t).remove(a)));
		if (ret)
			getSubscribersMap().cleanUp();
		return ret;
	}

	protected LoadingCache<IShapeDescriptorProvider, Set<IShapeAnchor>> getSubscribersMap() { return subscribersMap; }

	protected LoadingCache<T, IShapeAnchorSet> getAnchorSets() { return anchorSets; }
}
