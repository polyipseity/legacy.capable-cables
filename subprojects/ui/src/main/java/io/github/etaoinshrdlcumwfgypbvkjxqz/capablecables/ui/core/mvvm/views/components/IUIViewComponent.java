package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.animations.IUIAnimationController;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.traits.IHasBindingMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIViewContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.paths.IUIComponentPathResolver;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.TreeUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.traits.IHasBinding;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.FunctionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IConsumer3;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IThrowingBiFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public interface IUIViewComponent<S extends Shape, M extends IUIComponentManager<?>>
		extends IHasBinding, IHasBindingMap, IUIView<S> {
	Optional<? extends M> getManager();

	void setManager(@Nullable M manager);

	IUIComponentContext createComponentContext(IUIViewContext viewContext);

	List<IUIComponent> getChildrenFlatView();

	IUIComponentPathResolver getPathResolver();

	IUIComponentShapeAnchorController getShapeAnchorController();

	IUIAnimationController getAnimationController();

	enum StaticHolder {
		;
		private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());

		public static Optional<IUIComponentContext> createComponentContextWithManager(IUIViewComponent<?, ?> view, IUIViewContext viewContext) {
			return view.getManager()
					.map(manager -> {
						IUIComponentContext context = view.createComponentContext(viewContext);
						context.getMutator().push(context.getStackRef(), manager);
						return context;
					});
		}

		public static <T extends Throwable> void traverseComponentTreeDefault(IUIComponentContext context,
		                                                                      IUIComponent root,
		                                                                      BiConsumer<? super IUIComponentContext, ? super IUIComponentContextMutatorResult> pre,
		                                                                      IConsumer3<? super IUIComponentContext, ? super IUIComponentContextMutatorResult, ? super Iterable<? super IUIComponent>, ? extends T> post) throws T {
			traverseComponentTreeDefault(context, root, pre, post, FunctionUtilities.alwaysTruePredicate());
		}

		public static <T extends Throwable> void traverseComponentTreeDefault(IUIComponentContext context,
		                                                                      IUIComponent root,
		                                                                      BiConsumer<? super IUIComponentContext, ? super IUIComponentContextMutatorResult> pre,
		                                                                      IConsumer3<? super IUIComponentContext, ? super IUIComponentContextMutatorResult, ? super Iterable<? super IUIComponent>, ? extends T> post,
		                                                                      Predicate<? super IUIComponent> predicate) throws T {
			TreeUtilities.visitNodes(TreeUtilities.EnumStrategy.DEPTH_FIRST, root,
					component -> {
						if (predicate.test(component)) {
							pre.accept(context, context.getMutator().push(context.getStackRef(), component));
						}
						return component;
					},
					component -> CastUtilities.castChecked(IUIComponentContainer.class, component)
							.filter(predicate)
							.map(IUIComponentContainer::getChildrenView)
							.orElseGet(ImmutableList::of),
					IThrowingBiFunction.executeNow((parent, children) -> {
						if (predicate.test(parent)) {
							try (IUIComponentContextStack contextStackCopy = context.getStackRef().copy()) {
								post.accept(context, context.getMutator().pop(contextStackCopy), children);
							}
							context.getMutator().pop(context.getStackRef());
						}
						return parent;
					}),
					repeated -> { throw new AssertionError(); });
		}

		public static IUIComponent getComponentByName(IUIViewComponent<?, ?> view, String name) {
			return findComponentByName(view, name)
					.orElseThrow(() ->
							new IllegalArgumentException(
									new LogMessageBuilder()
											.addMarkers(UIMarkers.getInstance()::getMarkerUIView)
											.addKeyValue("view", view).addKeyValue("name", name)
											.addMessages(() -> getResourceBundle().getString("component.get.id.fail"))
											.build()
							));
		}

		public static Optional<IUIComponent> findComponentByName(IUIViewComponent<?, ?> view, String name) {
			return view.getChildrenFlatView().stream().sequential()
					.filter(child -> child.getName().filter(Predicate.isEqual(name)).isPresent())
					.findFirst();
		}

		protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }
	}
}
