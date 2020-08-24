package $group__.client.ui.structures.shapes.interactions;

import $group__.client.ui.core.structures.shapes.interactions.IShapeAnchor;
import $group__.client.ui.core.structures.shapes.interactions.IShapeAnchorController;
import $group__.client.ui.core.structures.shapes.interactions.IShapeAnchorSet;
import $group__.client.ui.core.structures.shapes.interactions.IShapeDescriptorProvider;
import $group__.utilities.CapacityUtilities;
import $group__.utilities.CollectionUtilities;
import $group__.utilities.MapUtilities;
import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public class ShapeAnchorController<T extends IShapeDescriptorProvider>
		implements IShapeAnchorController<T> {
	protected final ConcurrentMap<T, IShapeAnchorSet> anchorSets =
			MapUtilities.getMapMakerSingleThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_MEDIUM).weakKeys().makeMap();
	protected final ConcurrentMap<IShapeAnchorSet, T> anchorSetsInverse =
			MapUtilities.getMapMakerSingleThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_MEDIUM).weakValues().makeMap();
	protected final ConcurrentMap<IShapeDescriptorProvider, Set<IShapeAnchor>> subscribersMap =
			MapUtilities.getMapMakerSingleThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_MEDIUM).weakKeys().makeMap(); // COMMENT the keys and the values of the set are weak

	@Override
	public Map<? extends T, ? extends IShapeAnchorSet> getAnchorSetsView() { return ImmutableMap.copyOf(getAnchorSets()); }

	@Override
	public boolean addAnchors(T origin, Iterable<? extends IShapeAnchor> anchors) {
		boolean ret = getAnchorSet(origin).addAnchors(anchors);
		anchors.forEach(a ->
				a.getTarget().ifPresent(t ->
						getSubscribers(t).add(a)));
		anchors.forEach(a ->
				a.anchor(origin));
		return ret;
	}

	@Override
	public boolean removeAnchors(T origin, Iterable<? extends IShapeAnchor> anchors) {
		boolean ret = getAnchorSet(origin).removeAnchors(anchors);
		anchors.forEach(a ->
				a.getTarget().ifPresent(t ->
						getSubscribers(t).remove(a)));
		return ret;
	}

	protected IShapeAnchorSet getAnchorSet(T origin) {
		return getAnchorSets().computeIfAbsent(origin, k -> {
			IShapeAnchorSet v = new ShapeAnchorSet();
			getAnchorSetsInverse().put(v, k);
			return v;
		});
	}

	protected Set<IShapeAnchor> getSubscribers(IShapeDescriptorProvider notifier) {
		return getSubscribersMap().computeIfAbsent(notifier, k ->
				CollectionUtilities.newConcurrentWeakSet(CapacityUtilities.INITIAL_CAPACITY_SMALL));
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<IShapeAnchorSet, T> getAnchorSetsInverse() { return anchorSetsInverse; }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<IShapeDescriptorProvider, Set<IShapeAnchor>> getSubscribersMap() { return subscribersMap; }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<T, IShapeAnchorSet> getAnchorSets() { return anchorSets; }
}
