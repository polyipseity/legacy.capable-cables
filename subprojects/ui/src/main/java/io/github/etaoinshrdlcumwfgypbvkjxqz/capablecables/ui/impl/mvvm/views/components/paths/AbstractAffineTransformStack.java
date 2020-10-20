package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.paths;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.paths.IAffineTransformStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.AbstractObjectStack;

import java.awt.geom.AffineTransform;

public abstract class AbstractAffineTransformStack
		extends AbstractObjectStack.CopyPushable<AffineTransform>
		implements IAffineTransformStack {
	@Override
	public AbstractAffineTransformStack copy() {
		AbstractAffineTransformStack ret = newInstanceForCopying();
		ret.getData().clear();
		getData().stream().sequential()
				.map(AffineTransform::clone)
				.map(AffineTransform.class::cast)
				.forEachOrdered(ret.getData()::add);
		return ret;
	}

	protected abstract AbstractAffineTransformStack newInstanceForCopying();

	@Override
	public void close() { createCleaner().run(); }

	@Override
	public boolean isClean() { return IAffineTransformStack.isClean(getData()); }

	@Override
	public Runnable createCleaner() { return () -> IAffineTransformStack.popNTimes(this, getData().size() - 1); }

	@Override
	protected AffineTransform copyElement(AffineTransform object) { return (AffineTransform) object.clone(); }
}
