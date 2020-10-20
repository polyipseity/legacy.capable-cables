package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentTransformChildrenModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.paths.IAffineTransformStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.paths.IPath;

import java.awt.geom.AffineTransform;
import java.util.Optional;

public class UIDefaultComponentContextStackMutator
		implements IUIComponentContextStackMutator {
	@Override
	public IUIComponentContextMutatorResult push(IUIComponentContextStack contextStack, IUIComponent element) {
		IPath<IUIComponent> path = contextStack.getPathRef();
		IAffineTransformStack transformStack = contextStack.getTransformStackRef();

		Optional<? extends IUIComponent> pathEnd = path.getPathEnd();

		AffineTransform transform = transformStack.push();
		pathEnd.ifPresent(presentPathEnd -> {
			if (presentPathEnd instanceof IUIComponentContainer)
				IUIComponentTransformChildrenModifier.handleComponentModifiers((IUIComponentContainer) presentPathEnd,
						presentPathEnd.getModifiersView(),
						transform);
			else
				throw new IllegalStateException();
		});

		path.subPath(ImmutableList.of(element));

		return UIImmutableComponentContextMutatorResult.of(element);
	}

	@Override
	public IUIComponentContextMutatorResult pop(IUIComponentContextStack contextStack) {
		IPath<IUIComponent> path = contextStack.getPathRef();
		IAffineTransformStack transformStack = contextStack.getTransformStackRef();

		Optional<? extends IUIComponent> pathEnd = path.getPathEnd();
		path.parentPath(1);
		assert pathEnd.isPresent();

		transformStack.pop();

		return UIImmutableComponentContextMutatorResult.of(pathEnd.get());
	}
}
