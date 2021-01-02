package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components;

import com.google.common.collect.Iterators;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponentContextInternal;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponentContextMutatorResult;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.modifiers.IUIComponentTransformChildrenModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AffineTransformUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.paths.IPath;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.graphics.impl.UIObjectUtilities;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Deque;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class UIDefaultComponentContextMutator
		extends UIAbstractComponentContextMutator {
	@Override
	protected IUIComponentContextMutatorResult pop(IUIComponentContextInternal context) {
		IPath<IUIComponent> path = context.getPathRef();
		Deque<AffineTransform> transformStack = context.getTransformStackRef();
		Deque<Shape> clipStack = context.getClipStackRef();

		Optional<? extends IUIComponent> pathEnd = path.getPathEnd();

		// COMMENT clip
		clipStack.pop();
		// COMMENT path
		path.parentPath(1);
		assert pathEnd.isPresent(); // COMMENT due to path.parentPath
		// COMMENT transform
		transformStack.pop();

		// COMMENT graphics
		context.getGraphicsRef().ifPresent(graphics -> {
			graphics.setTransform(AffineTransformUtilities.getIdentity()); // COMMENT need to un-transform it to set the clip absolutely
			graphics.setClip(clipStack.peek());
			graphics.setTransform(transformStack.element());
		});

		return UIImmutableComponentContextMutatorResult.of(pathEnd.get());
	}

	@Override
	protected IUIComponentContextMutatorResult push(IUIComponentContextInternal context, IUIComponent element) {
		IPath<IUIComponent> path = context.getPathRef();
		Deque<AffineTransform> transformStack = context.getTransformStackRef();
		Deque<Shape> clipStack = context.getClipStackRef();

		// COMMENT transform
		AffineTransform transform = (AffineTransform) AssertionUtilities.assertNonnull(transformStack.element()).clone();
		path.getPathEnd().ifPresent(presentPathEnd ->
				IUIComponentTransformChildrenModifier.handleComponentModifiers(presentPathEnd,
						presentPathEnd.getModifiersView(),
						transform)
		);
		transformStack.push(transform);
		// COMMENT path
		path.subPath(Iterators.singletonIterator(element));
		// COMMENT clip
		Shape clip = UIObjectUtilities.intersectShapes(
				Stream.of(clipStack.peek(), transform.createTransformedShape(IUIComponent.getShape(element)))
						.filter(Objects::nonNull)
						.iterator()
		).orElseThrow(AssertionError::new); // COMMENT must have at least 1 element
		clipStack.push(clip);

		// COMMENT graphics
		context.getGraphicsRef().ifPresent(graphics -> {
			graphics.setTransform(AffineTransformUtilities.getIdentity()); // COMMENT need to un-transform it to set the clip absolutely
			graphics.setClip(clip);
			graphics.setTransform(transform);
		});

		return UIImmutableComponentContextMutatorResult.of(element);
	}
}
