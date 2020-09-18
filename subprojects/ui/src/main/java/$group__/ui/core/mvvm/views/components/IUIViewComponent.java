package $group__.ui.core.mvvm.views.components;

import $group__.ui.core.binding.traits.IHasBindingMap;
import $group__.ui.core.mvvm.views.IUIView;
import $group__.ui.core.structures.IUIComponentContext;
import $group__.ui.core.structures.paths.IUIComponentPathResolver;
import $group__.utilities.CastUtilities;
import $group__.utilities.ThrowableUtilities;
import $group__.utilities.TreeUtilities;
import $group__.utilities.binding.core.traits.IHasBinding;
import $group__.utilities.functions.FunctionUtilities;
import $group__.utilities.functions.IConsumer3;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public interface IUIViewComponent<S extends Shape, M extends IUIComponentManager<?>>
		extends IHasBinding, IHasBindingMap, IUIView<S> {
	Optional<? extends M> getManager();

	static IUIComponent getComponentByID(IUIViewComponent<?, ?> view, String id) {
		return findComponentByID(view, id)
				.orElseThrow(() ->
						ThrowableUtilities.BecauseOf.illegalArgument("Cannot find the component with the specified ID",
								"id", id,
								"view.getChildrenFlatView()", view.getChildrenFlatView(),
								"view", view));
	}

	static Optional<IUIComponent> findComponentByID(IUIViewComponent<?, ?> view, String id) {
		for (IUIComponent c : view.getChildrenFlatView()) {
			if (c.getID().filter(Predicate.isEqual(id)).isPresent())
				return Optional.of(c);
		}
		return Optional.empty();
	}

	IUIComponentContext createContext();

	List<IUIComponent> getChildrenFlatView();

	IUIComponentPathResolver<IUIComponent> getPathResolver();

	IUIComponentShapeAnchorController getShapeAnchorController();

	void setManager(@Nullable M manager);

	enum StaticHolder {
		;

		public static void traverseComponentTreeDefault(IUIComponentContext context, IUIComponent root, BiConsumer<? super IUIComponentContext, ? super IUIComponent> pre, IConsumer3<? super IUIComponentContext, ? super IUIComponent, ? super Iterable<? super IUIComponent>> post) {traverseComponentTreeDefault(context, root, pre, post, FunctionUtilities.alwaysTruePredicate());}

		public static void traverseComponentTreeDefault(IUIComponentContext context, IUIComponent root, BiConsumer<? super IUIComponentContext, ? super IUIComponent> pre, IConsumer3<? super IUIComponentContext, ? super IUIComponent, ? super Iterable<? super IUIComponent>> post, Predicate<? super IUIComponent> predicate) {
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
					(parent, children) -> {
						if (predicate.test(parent)) {
							post.accept(context, parent, children);
							context.pop();
						}
						return parent;
					},
					repeated -> { throw new AssertionError(); });
		}
	}
}
