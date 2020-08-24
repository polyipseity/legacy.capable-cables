package $group__.ui.core.structures.shapes.interactions;

import java.util.Map;

public interface IShapeAnchorController<T extends IShapeDescriptorProvider> {
	Map<? extends T, ? extends IShapeAnchorSet> getAnchorSetsView();

	boolean addAnchors(T origin, Iterable<? extends IShapeAnchor> anchors);

	boolean removeAnchors(T origin, Iterable<? extends IShapeAnchor> anchors);
}
