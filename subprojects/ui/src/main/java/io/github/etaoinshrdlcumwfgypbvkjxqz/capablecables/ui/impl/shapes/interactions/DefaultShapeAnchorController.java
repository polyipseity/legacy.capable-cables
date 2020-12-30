package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.interactions;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.interactions.IShapeAnchor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.interactions.IShapeAnchorController;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.interactions.IShapeAnchorSet;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.interactions.IShapeDescriptorProvider;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.CacheUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.ManualLoadingCache;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.impl.MappableSupplier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ConstantValue;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities.assertNonnull;

public class DefaultShapeAnchorController<T extends IShapeDescriptorProvider>
		implements IShapeAnchorController<T> {
	private final LoadingCache<T, IShapeAnchorSet> anchorSets =
			ManualLoadingCache.newNestedLoadingCache(CacheUtilities.newCacheBuilderSingleThreaded()
							.initialCapacity(CapacityUtilities.getInitialCapacityMedium())
							.weakKeys()
							.build(CacheLoader.from(DefaultShapeAnchorSet::new)),
					e -> assertNonnull(e.getValue()).isEmpty());
	private final LoadingCache<IShapeDescriptorProvider, Set<IShapeAnchor>> subscribersMap =
			ManualLoadingCache.newNestedLoadingCacheCollection(CacheUtilities.newCacheBuilderSingleThreaded()
					.initialCapacity(CapacityUtilities.getInitialCapacityMedium())
					.weakKeys()
					.build(CacheLoader.from(
							new MappableSupplier<>(ConstantValue.of(MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(CapacityUtilities.getInitialCapacitySmall())))
									.map(t -> Collections.newSetFromMap(t.makeMap())))));

	@Override
	public Map<? extends T, ? extends IShapeAnchorSet> getAnchorSetsView() { return ImmutableMap.copyOf(getAnchorSets().asMap()); }

	@Override
	public void anchor() {
		getAnchorSets().asMap().entrySet().stream().unordered()
				.forEach(entry -> assertNonnull(entry.getValue()).anchor(assertNonnull(entry.getKey())));
	}

	@Override
	public boolean addAnchors(T origin, Iterator<? extends IShapeAnchor> anchors) {
		IShapeAnchorSet as = getAnchorSets().getUnchecked(origin);
		boolean ret = as.addAnchors(anchors);
		as.getAnchorsView().values().stream().unordered()
				.forEach(a ->
						a.getTarget().ifPresent(t ->
								getSubscribersMap().getUnchecked(t).add(a)));
		return ret;
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public boolean removeAnchors(T origin, Iterator<? extends IShapeAnchor> anchors) {
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
