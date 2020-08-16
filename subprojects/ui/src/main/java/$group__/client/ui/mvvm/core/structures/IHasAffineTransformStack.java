package $group__.client.ui.mvvm.core.structures;

import java.awt.geom.AffineTransform;

public interface IHasAffineTransformStack {
	IAffineTransformStack getTransformStackView(); // TODO mark as cleanup required

	default AffineTransform getTransformCurrentView() { return getTransformView(-1); }

	AffineTransform getTransformView(int depth);

	int sizeOfTransformStack();
}
