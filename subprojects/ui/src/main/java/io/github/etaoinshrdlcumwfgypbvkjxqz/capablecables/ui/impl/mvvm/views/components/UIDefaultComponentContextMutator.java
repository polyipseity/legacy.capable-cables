package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContextInternal;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContextMutatorResult;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentTransformChildrenModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.paths.IPath;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Deque;
import java.util.Optional;

public class UIDefaultComponentContextMutator
		extends UIAbstractComponentContextMutator {
	@Override
	public IUIComponentContextMutatorResult pop(IUIComponentContextInternal context) {
		Graphics2D graphics = context.getGraphicsRef();
		IPath<IUIComponent> path = context.getPathRef();
		Deque<AffineTransform> transformStack = context.getTransformStackRef();
		Deque<Shape> clipStack = context.getClipStackRef();

		Optional<? extends IUIComponent> pathEnd = path.getPathEnd();

		// COMMENT path
		path.parentPath(1);
		assert pathEnd.isPresent(); // COMMENT due to path.parentPath
		// COMMENT transform
		transformStack.pop();
		// COMMENT clip
		clipStack.pop();
		// COMMENT graphics
		graphics.setTransform(transformStack.element());
		graphics.setClip(clipStack.element()); // COMMENT automatically transforms

		return UIImmutableComponentContextMutatorResult.of(pathEnd.get());
	}

	@Override
	public IUIComponentContextMutatorResult push(IUIComponentContextInternal context, IUIComponent element) {
		Graphics2D graphics = context.getGraphicsRef();
		IPath<IUIComponent> path = context.getPathRef();
		Deque<AffineTransform> transformStack = context.getTransformStackRef();
		Deque<Shape> clipStack = context.getClipStackRef();

		// COMMENT transform
		AffineTransform transform = (AffineTransform) AssertionUtilities.assertNonnull(transformStack.element()).clone();
		transformStack.push(transform);
		path.getPathEnd().ifPresent(presentPathEnd ->
				IUIComponentTransformChildrenModifier.handleComponentModifiers(presentPathEnd,
						presentPathEnd.getModifiersView(),
						transform)
		);
		// COMMENT path
		path.subPath(ImmutableList.of(element));
		// COMMENT graphics
		graphics.setTransform(transform);
		graphics.clip(IUIComponent.getShape(element)); // COMMENT automatically transforms
		// COMMENT clip
		clipStack.push(graphics.getClip());

		return UIImmutableComponentContextMutatorResult.of(element);
	}
}
