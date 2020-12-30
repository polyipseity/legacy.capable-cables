package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.interactions;

import java.util.Iterator;
import java.util.Map;

public interface IShapeAnchorController<T extends IShapeDescriptorProvider> {
	Map<? extends T, ? extends IShapeAnchorSet> getAnchorSetsView();

	void anchor();

	@SuppressWarnings("UnusedReturnValue")
	boolean addAnchors(T origin, Iterator<? extends IShapeAnchor> anchors);

	@SuppressWarnings("UnusedReturnValue")
	boolean removeAnchors(T origin, Iterator<? extends IShapeAnchor> anchors);
}
