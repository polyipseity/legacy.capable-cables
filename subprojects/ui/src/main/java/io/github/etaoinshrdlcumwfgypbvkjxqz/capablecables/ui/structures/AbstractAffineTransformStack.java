package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IAffineTransformStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.AbstractObjectStack;

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
	public boolean isClean() { return IAffineTransformStack.StaticHolder.isClean(getData()); }

	@Override
	public Runnable createCleaner() { return () -> StaticHolder.popNTimes(this, getData().size() - 1); }

	@Override
	protected AffineTransform copyElement(AffineTransform object) { return (AffineTransform) object.clone(); }
}
