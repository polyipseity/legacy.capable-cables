package $group__.ui.structures.shapes.interactions;

import $group__.ui.core.structures.shapes.interactions.IShapeAnchor;
import $group__.ui.core.structures.shapes.interactions.IShapeAnchorController;
import $group__.ui.core.structures.shapes.interactions.IShapeAnchorSet;
import $group__.ui.core.structures.shapes.interactions.IShapeDescriptorProvider;
import $group__.utilities.AssertionUtilities;
import $group__.utilities.CapacityUtilities;
import $group__.utilities.collections.CacheUtilities;
import $group__.utilities.collections.ManualLoadingCache;
import $group__.utilities.collections.MapUtilities;
import $group__.utilities.functions.ConstantSupplier;
import $group__.utilities.functions.MappableSupplier;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Streams;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class ShapeAnchorController<T extends IShapeDescriptorProvider>
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
							new MappableSupplier<>(ConstantSupplier.of(MapUtilities.newMapMakerSingleThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_SMALL)))
									.<Set<IShapeAnchor>>map(t -> Collections.newSetFromMap(t.makeMap()))
									::get)));

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
