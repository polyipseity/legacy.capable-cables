package $group__.ui.core.mvvm.views.components;

import $group__.ui.core.binding.traits.IHasBindingMap;
import $group__.ui.core.mvvm.views.IUIView;
import $group__.ui.core.mvvm.views.components.paths.IUIComponentPathResolver;
import $group__.ui.core.structures.IAffineTransformStack;
import $group__.utilities.CastUtilities;
import $group__.utilities.ThrowableUtilities;
import $group__.utilities.TreeUtilities;
import $group__.utilities.binding.core.traits.IHasBinding;
import $group__.utilities.functions.IConsumer3;
import com.google.common.collect.ImmutableSet;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
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

	IAffineTransformStack getCleanTransformStack();

	List<IUIComponent> getChildrenFlatView();

	static void traverseComponentTreeDefault(IAffineTransformStack stack, IUIComponent root, BiConsumer<? super IAffineTransformStack, ? super IUIComponent> pre, IConsumer3<? super IAffineTransformStack, ? super IUIComponent, ? super Iterable<? super IUIComponent>> post) {
		TreeUtilities.visitNodes(TreeUtilities.EnumStrategy.DEPTH_FIRST, root,
				Function.identity(),
				component -> {
					pre.accept(stack, component);
					stack.push();
					return CastUtilities.castChecked(IUIComponentContainer.class, component)
							.<Iterable<IUIComponent>>map(container -> {
								container.transformChildren(stack);
								return container.getChildrenView();
							})
							.orElseGet(ImmutableSet::of);
				},
				(parent, children) -> {
					stack.pop();
					post.accept(stack, parent, children);
					return parent;
				},
				repeated -> { throw new AssertionError(); });
	}

	void setManager(@Nullable M manager);

	IUIComponentPathResolver<IUIComponent> getPathResolver();

	IUIComponentShapeAnchorController getShapeAnchorController();
}
