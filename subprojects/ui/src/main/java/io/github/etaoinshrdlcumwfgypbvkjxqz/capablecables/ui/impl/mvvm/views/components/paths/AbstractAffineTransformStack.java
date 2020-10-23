package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.paths;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.paths.IAffineTransformStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.AbstractObjectStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;

import java.awt.geom.AffineTransform;

public abstract class AbstractAffineTransformStack
		extends AbstractObjectStack.CopyPushable<AffineTransform>
		implements IAffineTransformStack {
	@Override
	public void close() { createCleaner().run(); }

	@Override
	public boolean isClean() { return IAffineTransformStack.isClean(getData()); }

	@Override
	public Runnable createCleaner() { return () -> IAffineTransformStack.popNTimes(this, getData().size() - 1); }

	@Override
	protected AffineTransform copyElement(AffineTransform object) { return (AffineTransform) object.clone(); }

	@Override
	public AbstractAffineTransformStack clone() {
		try {
			return (AbstractAffineTransformStack) super.clone();
		} catch (CloneNotSupportedException e) {
			throw ThrowableUtilities.propagate(e);
		}
	}
}
