package $group__.ui.core.mvvm.views.components;

import $group__.ui.UIConfiguration;
import $group__.ui.UIMarkers;
import $group__.ui.core.binding.traits.IHasBindingMap;
import $group__.ui.core.mvvm.views.IUIView;
import $group__.ui.core.structures.IUIComponentContext;
import $group__.ui.core.structures.paths.IUIComponentPathResolver;
import $group__.utilities.CastUtilities;
import $group__.utilities.LogMessageBuilder;
import $group__.utilities.TreeUtilities;
import $group__.utilities.binding.core.traits.IHasBinding;
import $group__.utilities.functions.FunctionUtilities;
import $group__.utilities.functions.IConsumer3;
import $group__.utilities.functions.IThrowingBiFunction;
import $group__.utilities.templates.CommonConfigurationTemplate;
import com.google.common.collect.ImmutableList;

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

	IUIComponentContext createContext();

	List<IUIComponent> getChildrenFlatView();

	IUIComponentPathResolver<IUIComponent> getPathResolver();

	IUIComponentShapeAnchorController getShapeAnchorController();

	void setManager(@Nullable M manager);

	enum StaticHolder {
		;
		private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());

		public static <T extends Throwable> void traverseComponentTreeDefault(IUIComponentContext context,
		                                                                      IUIComponent root,
		                                                                      BiConsumer<? super IUIComponentContext, ? super IUIComponent> pre,
		                                                                      IConsumer3<? super IUIComponentContext, ? super IUIComponent, ? super Iterable<? super IUIComponent>, ? extends T> post) throws T {
			traverseComponentTreeDefault(context, root, pre, post, FunctionUtilities.alwaysTruePredicate());
		}

		public static <T extends Throwable> void traverseComponentTreeDefault(IUIComponentContext context,
		                                                                      IUIComponent root,
		                                                                      BiConsumer<? super IUIComponentContext, ? super IUIComponent> pre,
		                                                                      IConsumer3<? super IUIComponentContext, ? super IUIComponent, ? super Iterable<? super IUIComponent>, ? extends T> post,
		                                                                      Predicate<? super IUIComponent> predicate) throws T {
			TreeUtilities.visitNodes(TreeUtilities.EnumStrategy.DEPTH_FIRST, root,
					component -> {
						if (predicate.test(component)) {
							context.push(component);
							pre.accept(context, component);
						}
						return component;
					},
					component -> CastUtilities.castChecked(IUIComponentContainer.class, component)
							.filter(predicate)
							.map(IUIComponentContainer::getChildrenView)
							.orElseGet(ImmutableList::of),
					IThrowingBiFunction.execute((parent, children) -> {
						if (predicate.test(parent)) {
							post.accept(context, parent, children);
							context.pop();
						}
						return parent;
					}),
					repeated -> { throw new AssertionError(); });
		}

		public static IUIComponent getComponentByID(IUIViewComponent<?, ?> view, String id) {
			return findComponentByID(view, id)
					.orElseThrow(() ->
							new IllegalArgumentException(
									new LogMessageBuilder()
											.addMarkers(UIMarkers.getInstance()::getMarkerUIView, UIMarkers.getInstance()::getMarkerUIComponent)
											.addKeyValue("view", view).addKeyValue("id", id)
											.addMessages(() -> getResourceBundle().getString("component.get.id.fail"))
											.build()
							));
		}

		public static Optional<IUIComponent> findComponentByID(IUIViewComponent<?, ?> view, String id) {
			for (IUIComponent c : view.getChildrenFlatView()) {
				if (c.getID().filter(Predicate.isEqual(id)).isPresent())
					return Optional.of(c);
			}
			return Optional.empty();
		}

		protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }
	}
}
