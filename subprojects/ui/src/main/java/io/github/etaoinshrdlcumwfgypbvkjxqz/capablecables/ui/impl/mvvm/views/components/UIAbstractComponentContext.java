package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponentContextInternal;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LoopUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.ListBackedDeque;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.paths.IPath;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.paths.ImmutablePath;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Deque;
import java.util.Optional;

public abstract class UIAbstractComponentContext
		implements IUIComponentContextInternal {
	@Override
	public UIAbstractComponentContext clone() {
		try {
			return (UIAbstractComponentContext) super.clone();
		} catch (CloneNotSupportedException e) {
			throw ThrowableUtilities.propagate(e);
		}
	}

	@Override
	public @Immutable IPath<? extends IUIComponent> getPathView() {
		return ImmutablePath.of(getPathRef().asList());
	}

	@Override
	public @Immutable Deque<? extends AffineTransform> getTransformStackView() {
		return ListBackedDeque.ofImmutable(getTransformStackRef());
	}

	@Override
	public @Immutable Deque<? extends Shape> getClipStackView() {
		return ListBackedDeque.ofImmutable(getClipStackRef());
	}

	@Override
	public void close() {
		getGraphicsRef().ifPresent(Graphics::dispose);

		IPath<IUIComponent> path = getPathRef();
		int size = path.size();

		path.parentPath(size);
		LoopUtilities.doNTimes(size, getTransformStackRef()::pop);
		LoopUtilities.doNTimes(size, getClipStackRef()::pop);
	}

	@Override
	public Optional<? extends Graphics2D> createGraphics() {
		return getGraphicsRef()
				.map(Graphics::create)
				.map(Graphics2D.class::cast);
	}
}
