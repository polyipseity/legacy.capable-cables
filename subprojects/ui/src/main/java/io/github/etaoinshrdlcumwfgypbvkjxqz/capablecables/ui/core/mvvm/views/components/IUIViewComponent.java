package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.traits.IHasBindingMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.paths.IUIComponentPathResolver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.TreeUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.FunctionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IConsumer3;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IThrowingBiFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.traits.IHasBinding;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.optionals.impl.Optional2;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public interface IUIViewComponent<S extends Shape, M extends IUIComponentManager<?>>
		extends IHasBinding, IHasBindingMap, IUIView<S> {
	static Optional<IUIComponentContext> createComponentContextWithManager(IUIViewComponent<?, ?> instance) {
		return Optional2.of(
				() -> instance.createComponentContext().orElse(null),
				() -> instance.getManager().orElse(null))
				.map(values -> {
					IUIComponentContext context = values.getValue1Nonnull();
					IUIComponentManager<?> manager = values.getValue2Nonnull();
					context.getMutator().push(context, manager);
					return context;
				});
	}

	Optional<? extends IUIComponentContext> createComponentContext()
			throws IllegalStateException;

	Optional<? extends M> getManager();

	void setManager(@Nullable M manager);

	static <T extends Throwable> void traverseComponentTreeDefault(IUIComponentContext context,
	                                                               IUIComponent root,
	                                                               BiConsumer<? super IUIComponentContext, ? super IUIComponentContextMutatorResult> pre,
	                                                               IConsumer3<? super IUIComponentContext, ? super IUIComponentContextMutatorResult, ? super Iterable<? super IUIComponent>, ? extends T> post) throws T {
		traverseComponentTreeDefault(context, root, pre, post, FunctionUtilities.getAlwaysTruePredicate());
	}

	static <T extends Throwable> void traverseComponentTreeDefault(IUIComponentContext context,
	                                                               IUIComponent root,
	                                                               BiConsumer<? super IUIComponentContext, ? super IUIComponentContextMutatorResult> pre,
	                                                               IConsumer3<? super IUIComponentContext, ? super IUIComponentContextMutatorResult, ? super Iterable<? super IUIComponent>, ? extends T> post,
	                                                               Predicate<? super IUIComponent> predicate) throws T {
		TreeUtilities.visitNodes(TreeUtilities.EnumStrategy.DEPTH_FIRST, root,
				component -> {
					if (predicate.test(component)) {
						pre.accept(context, context.getMutator().push(context, component));
					}
					return component;
				},
				component -> CastUtilities.castChecked(IUIComponentContainer.class, component)
						.filter(predicate)
						.map(IUIComponentContainer::getChildrenView)
						.orElseGet(ImmutableList::of),
				IThrowingBiFunction.executeNow((parent, children) -> {
					if (predicate.test(parent)) {
						try (IUIComponentContext contextCopy = context.clone()) {
							post.accept(context, contextCopy.getMutator().pop(contextCopy), children);
						}
						context.getMutator().pop(context);
					}
					return parent;
				}),
				repeated -> { throw new AssertionError(); });
	}

	List<IUIComponent> getChildrenFlatView();

	static IUIComponentPathResolver getPathResolver(IUIViewComponent<?, ?> instance) {
		return instance.getCoordinator(IUIComponentPathResolver.class).orElseThrow(AssertionError::new);
	}

	static IUIComponentShapeAnchorController getShapeAnchorController(IUIViewComponent<?, ?> instance) {
		return instance.getCoordinator(IUIComponentShapeAnchorController.class).orElseThrow(AssertionError::new);
	}
}
