package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContextInternal;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContextMutatorResult;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentTransformChildrenModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.paths.IAffineTransformStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.paths.IPath;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Optional;

public class UIDefaultComponentContextMutator
		extends UIAbstractComponentContextMutator {
	@Override
	public IUIComponentContextMutatorResult pop(IUIComponentContextInternal context) {
		Graphics2D graphics = context.getGraphicsRef();
		IPath<IUIComponent> path = context.getPathRef();
		IAffineTransformStack transformStack = context.getTransformStackRef();

		Optional<? extends IUIComponent> pathEnd = path.getPathEnd();

		// COMMENT path
		path.parentPath(1);
		assert pathEnd.isPresent(); // COMMENT due to path.parentPath
		// COMMENT transform
		transformStack.pop();
		// COMMENT graphics
		graphics.setTransform(transformStack.element());
		graphics.setClip(
				path.getPathEnd()
						.map(IUIComponent::getShape)
						.orElse(null)
		); // COMMENT automatically transforms

		return UIImmutableComponentContextMutatorResult.of(pathEnd.get());
	}

	@Override
	public IUIComponentContextMutatorResult push(IUIComponentContextInternal context, IUIComponent element) {
		Graphics2D graphics = context.getGraphicsRef();
		IPath<IUIComponent> path = context.getPathRef();
		IAffineTransformStack transformStack = context.getTransformStackRef();

		// COMMENT transform
		AffineTransform transform = transformStack.push();
		path.getPathEnd().ifPresent(presentPathEnd ->
				IUIComponentTransformChildrenModifier.handleComponentModifiers(presentPathEnd,
						presentPathEnd.getModifiersView(),
						transform)
		);
		// COMMENT path
		path.subPath(ImmutableList.of(element));
		// COMMENT graphics
		graphics.setTransform(transform);
		graphics.setClip(IUIComponent.getShape(element)); // COMMENT automatically transforms

		return UIImmutableComponentContextMutatorResult.of(element);
	}
}
