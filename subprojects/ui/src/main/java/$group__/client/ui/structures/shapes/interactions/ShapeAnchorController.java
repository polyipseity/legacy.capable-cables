package $group__.client.ui.structures.shapes.interactions;

import $group__.client.ui.core.structures.shapes.interactions.IShapeAnchor;
import $group__.client.ui.core.structures.shapes.interactions.IShapeAnchorController;
import $group__.client.ui.core.structures.shapes.interactions.IShapeAnchorSet;
import $group__.client.ui.core.structures.shapes.interactions.IShapeDescriptorProvider;
import $group__.utilities.CapacityUtilities;
import $group__.utilities.MapUtilities;
import $group__.utilities.references.WeakReferenceValueEquals;
import com.google.common.collect.*;
import sun.misc.Cleaner;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public class ShapeAnchorController<T extends IShapeDescriptorProvider>
		implements IShapeAnchorController<T> {
	protected final BiMap<WeakReference<T>, IShapeAnchorSet> anchorSets =
			Maps.synchronizedBiMap(HashBiMap.create(CapacityUtilities.INITIAL_CAPACITY_MEDIUM));
	protected final ConcurrentMap<IShapeDescriptorProvider, Set<IShapeAnchor>> subscribersMap =
			MapUtilities.getMapMakerSingleThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_MEDIUM).weakKeys().makeMap(); // COMMENT the keys and the values of the set are weak

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public Map<? extends T, ? extends IShapeAnchorSet> getAnchorSetsView() {
		return ImmutableMap.copyOf(getAnchorSets().entrySet().stream().unordered()
				.map(e -> Maps.immutableEntry(Optional.ofNullable(e.getKey()).map(Reference::get).orElse(null), e.getValue()))
				.filter(e -> e.getKey() != null)
				.collect(ImmutableList.toImmutableList()));
	}

	@Override
	public boolean addAnchors(T origin, Iterable<? extends IShapeAnchor> anchors) {
		boolean ret = getAnchorSet(origin).addAnchors(anchors);
		anchors.forEach(a ->
				getSubscribers(a.getTarget()).add(a));
		anchors.forEach(a ->
				a.anchor(origin));
		return ret;
	}

	@Override
	public boolean removeAnchors(T origin, Iterable<? extends IShapeAnchor> anchors) {
		boolean ret = getAnchorSet(origin).removeAnchors(anchors);
		anchors.forEach(a -> getSubscribers(a.getTarget()).remove(a));
		return ret;
	}

	protected IShapeAnchorSet getAnchorSet(T origin) {
		return getAnchorSets().computeIfAbsent(new WeakReferenceValueEquals<>(origin), k -> {
			Cleaner.create(origin, () ->
					getAnchorSets().remove(k));
			return new ShapeAnchorSet();
		});
	}

	protected Set<IShapeAnchor> getSubscribers(IShapeDescriptorProvider notifier) {
		return getSubscribersMap().computeIfAbsent(notifier, k ->
				Collections.newSetFromMap(MapUtilities.getMapMakerSingleThreaded().weakKeys().makeMap()));
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<IShapeDescriptorProvider, Set<IShapeAnchor>> getSubscribersMap() { return subscribersMap; }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected BiMap<WeakReference<T>, IShapeAnchorSet> getAnchorSets() { return anchorSets; }
}
