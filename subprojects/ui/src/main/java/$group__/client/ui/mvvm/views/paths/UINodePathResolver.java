package $group__.client.ui.mvvm.views.paths;

import $group__.client.ui.mvvm.core.views.paths.IUINode;
import $group__.client.ui.mvvm.core.views.paths.IUINodePathResolver;
import $group__.utilities.CapacityUtilities;
import $group__.utilities.MapUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

public abstract class UINodePathResolver<T extends IUINode>
		implements IUINodePathResolver<T> {
	protected final ConcurrentMap<T, List<T>> virtualElementsMap =
			MapUtilities.getMapMakerSingleThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_SMALL).weakKeys().makeMap();

	@Override
	public boolean addVirtualElement(T element,
	                                 T virtualElement) {
		return getVirtualElements(element).add(virtualElement);
	}

	@Override
	public boolean removeVirtualElement(T element,
	                                    T virtualElement) {
		return getVirtualElements(element).remove(virtualElement);
	}

	@Override
	public boolean moveVirtualElementToTop(T element, T virtualElement) {
		List<T> ve = getVirtualElements(element);
		if (ve.remove(virtualElement)) {
			ve.add(ve.size(), virtualElement);
			return true;
		}
		return false;
	}

	protected List<T> getVirtualElements(T element) { return getVirtualElementsMap().computeIfAbsent(element, k -> new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_SMALL)); }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<T, List<T>> getVirtualElementsMap() { return virtualElementsMap; }
}
