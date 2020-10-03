package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentTransformChildrenModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IAffineTransformStack;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IUIComponentContextMutator;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.IUIComponentContextMutatorResult;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.paths.IPath;

import java.awt.geom.AffineTransform;
import java.util.Optional;

public class DefaultUIComponentContextMutator
		implements IUIComponentContextMutator {
	@Override
	public IUIComponentContextMutatorResult push(IUIComponentContext context, IUIComponent element) {
		IPath<IUIComponent> path = context.getPath();
		IAffineTransformStack transformStack = context.getTransformStack();

		Optional<? extends IUIComponent> pathEnd = path.getPathEnd();

		AffineTransform transform = transformStack.push();
		pathEnd.ifPresent(presentPathEnd -> {
			if (presentPathEnd instanceof IUIComponentContainer)
				IUIComponentTransformChildrenModifier.StaticHolder.handleComponentModifiers((IUIComponentContainer) presentPathEnd,
						presentPathEnd.getModifiersView(),
						transform);
			else
				throw new IllegalStateException();
		});

		path.subPath(ImmutableList.of(element));

		return ImmutableUIComponentContextMutatorResult.of(element);
	}

	@Override
	public IUIComponentContextMutatorResult pop(IUIComponentContext context) {
		IPath<IUIComponent> path = context.getPath();
		IAffineTransformStack transformStack = context.getTransformStack();

		Optional<? extends IUIComponent> pathEnd = path.getPathEnd();
		path.parentPath(1);
		assert pathEnd.isPresent();

		transformStack.pop();

		return ImmutableUIComponentContextMutatorResult.of(pathEnd.get());
	}
}
