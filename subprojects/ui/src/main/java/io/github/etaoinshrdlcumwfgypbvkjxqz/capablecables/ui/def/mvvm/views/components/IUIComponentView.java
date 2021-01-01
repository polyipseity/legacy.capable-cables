package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components;

import com.google.common.collect.ImmutableSet;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.binding.traits.IHasBindingMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.paths.IUIComponentPathResolver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.shapes.interactions.IShapeDescriptorDynamicDetector;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.TreeUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.def.IConsumer3;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.def.IThrowingBiFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.impl.FunctionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.traits.IHasBinding;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface IUIComponentView<S extends Shape, M extends IUIComponentManager<?>>
		extends IHasBinding, IHasBindingMap, IUIView<S> {
	static Optional<IUIComponentContext> createComponentContextWithManager(IUIComponentView<?, ?> instance) {
		return instance.createComponentContext()
				.map(context -> {
					context.getMutator().push(context, instance.getManager());
					return context;
				});
	}

	Optional<? extends IUIComponentContext> createComponentContext();

	M getManager();

	void setManager(M manager);

	static <T extends IUIComponentView<S, M>,
			S extends Shape,
			M extends IUIComponentManager<?>> T create(Supplier<@Nonnull ? extends T> constructor,
	                                                   M manager) {
		T result = constructor.get();
		result.setManager(manager);
		return result;
	}

	static <T extends Throwable> void traverseComponentTreeDefault(IUIComponentContext context,
	                                                               IUIComponent root,
	                                                               BiConsumer<@Nonnull ? super IUIComponentContext, @Nonnull ? super IUIComponentContextMutatorResult> pre,
	                                                               IConsumer3<@Nonnull ? super IUIComponentContext, @Nonnull ? super IUIComponentContextMutatorResult, @Nonnull ? super Iterable<? super IUIComponent>, @Nonnull ? extends T> post) throws T {
		traverseComponentTreeDefault(context, root, pre, post, FunctionUtilities.getAlwaysTruePredicate());
	}

	static <T extends Throwable> void traverseComponentTreeDefault(IUIComponentContext context,
	                                                               IUIComponent root,
	                                                               BiConsumer<@Nonnull ? super IUIComponentContext, @Nonnull ? super IUIComponentContextMutatorResult> pre,
	                                                               IConsumer3<@Nonnull ? super IUIComponentContext, @Nonnull ? super IUIComponentContextMutatorResult, @Nonnull ? super Iterable<? super IUIComponent>, @Nonnull ? extends T> post,
	                                                               Predicate<@Nonnull ? super IUIComponent> predicate) throws T {
		TreeUtilities.visitNodes(TreeUtilities.EnumStrategy.DEPTH_FIRST, root,
				component -> {
					if (predicate.test(component)) {
						pre.accept(context, context.getMutator().push(context, component));
					}
					return component;
				},
				component -> predicate.test(component) ? component.getChildrenView() : ImmutableSet.of(),
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

	static void traverseComponentTreeDefault(IUIComponent root,
	                                         Consumer<@Nonnull ? super IUIComponent> pre,
	                                         BiConsumer<@Nonnull ? super IUIComponent, @Nonnull ? super Iterable<? super IUIComponent>> post) {
		traverseComponentTreeDefault(root, pre, post, FunctionUtilities.getAlwaysTruePredicate());
	}

	static void traverseComponentTreeDefault(IUIComponent root,
	                                         Consumer<@Nonnull ? super IUIComponent> pre,
	                                         BiConsumer<@Nonnull ? super IUIComponent, @Nonnull ? super Iterable<? super IUIComponent>> post,
	                                         Predicate<@Nonnull ? super IUIComponent> predicate) {
		TreeUtilities.visitNodes(TreeUtilities.EnumStrategy.DEPTH_FIRST, root,
				component -> {
					if (predicate.test(component)) {
						pre.accept(component);
					}
					return component;
				},
				component -> predicate.test(component) ? component.getChildrenView() : ImmutableSet.of(),
				(parent, children) -> {
					if (predicate.test(parent)) {
						post.accept(parent, children);
					}
					return parent;
				},
				repeated -> { throw new AssertionError(); });
	}

	static IUIComponentPathResolver getPathResolver(IUIComponentView<?, ?> instance) {
		return instance.getCoordinator(IUIComponentPathResolver.class)
				.orElseThrow(AssertionError::new);
	}

	static IUIComponentShapeAnchorController getShapeAnchorController(IUIComponentView<?, ?> instance) {
		return instance.getCoordinator(IUIComponentShapeAnchorController.class)
				.orElseThrow(AssertionError::new);
	}

	static IShapeDescriptorDynamicDetector getShapeDescriptorDynamicDetector(IUIComponentView<?, ?> instance) {
		return instance.getCoordinator(IShapeDescriptorDynamicDetector.class)
				.orElseThrow(AssertionError::new);
	}

	@Immutable List<? extends IUIComponent> getChildrenFlatView();
}
