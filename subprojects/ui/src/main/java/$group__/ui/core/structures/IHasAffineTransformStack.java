package $group__.ui.core.structures;

import java.awt.geom.AffineTransform;

public interface IHasAffineTransformStack {
	/**
	 * Returned stack requires explicit cleanup.
	 */
	IAffineTransformStack getTransformStackView();

	default AffineTransform getTransformCurrentView() { return getTransformView(-1); }

	AffineTransform getTransformView(int depth);

	int sizeOfTransformStack();
}
