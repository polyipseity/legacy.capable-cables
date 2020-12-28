package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.interactions;

import java.util.Map;

public interface IShapeAnchorController<T extends IShapeDescriptorProvider> {
	Map<? extends T, ? extends IShapeAnchorSet> getAnchorSetsView();

	void anchor();

	@SuppressWarnings("UnusedReturnValue")
	boolean addAnchors(T origin, Iterable<? extends IShapeAnchor> anchors);

	boolean removeAnchors(T origin, Iterable<? extends IShapeAnchor> anchors);
}
