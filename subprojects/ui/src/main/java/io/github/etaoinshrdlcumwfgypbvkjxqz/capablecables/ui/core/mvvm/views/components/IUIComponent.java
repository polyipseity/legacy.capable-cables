package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.traits.IHasBindingMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentLifecycleModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.naming.INamed;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.interactions.IShapeDescriptorProvider;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.mvvm.views.components.extensions.caches.UIDefaultCacheExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.paths.INode;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.paths.IPath;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.paths.ImmutablePath;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.traits.IHasBinding;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.extensions.core.IExtensionContainer;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

/*
 * TODO auto resizing based on min size and preferred size
 */
public interface IUIComponent
		extends INamed, INode, IShapeDescriptorProvider, IHasBinding, IHasBindingMap, IUIEventTarget, IExtensionContainer<INamespacePrefixedString>,
		IUIComponentLifecycleModifier {
	static <T> Optional<T> getYoungestParentInstanceOf(IUIComponent self, Class<T> clazz) {
		for (Iterator<IUIComponentContainer> iterator = new ParentIterator(self.getParent().orElse(null));
		     iterator.hasNext(); ) {
			IUIComponentContainer element = iterator.next();
			if (clazz.isInstance(element))
				return Optional.of(clazz.cast(element));
		}
		return Optional.empty();
	}

	static Shape getContextualShape(IUIComponentContext context, IUIComponent component) { return IUIComponentContext.createContextualShape(context, getShape(component)); }

	static Shape getShape(IUIComponent component) { return component.getShapeDescriptor().getShapeOutput(); }

	@Immutable
	@SuppressWarnings({"rawtypes", "RedundantSuppression"})
	static IUIComponentContext getContext(IUIComponent component) {
		IUIComponentContext context =
				component
						.getManager()
						.flatMap(IUIComponentManager::getView)
						.flatMap(IUIViewComponent::createComponentContext)
						.orElseThrow(IllegalStateException::new);
		getPath(component).asList()
				.forEach(element -> context.getMutator().push(context.getStackRef(), element));
		return context;
	}

	@Immutable
	static IPath<IUIComponent> getPath(IUIComponent component) {
		List<IUIComponent> path = new ArrayList<>(CapacityUtilities.getInitialCapacitySmall());
		path.add(component);
		new ParentIterator(component.getParent().orElse(null))
				.forEachRemaining(path::add);
		return new ImmutablePath<>(Lists.reverse(path));
	}

	static <S extends Shape> boolean reshapeComponent(IUIComponent self, IShapeDescriptor<? super S> shapeDescriptor, Predicate<? super IShapeDescriptor<? super S>> action)
			throws ConcurrentModificationException {
		return self.modifyShape(() ->
				action.test(shapeDescriptor));
	}

	Optional<? extends IUIComponentContainer> getParent();

	default Optional<? extends IUIComponentManager<?>> getManager() { return UIDefaultCacheExtension.CacheUniversal.getManager().getValue().get(this); }

	boolean isVisible();

	void setVisible(boolean visible);

	void onParentChange(@Nullable IUIComponentContainer previous, @Nullable IUIComponentContainer next);

	void setActive(boolean active);

	@Override
	default boolean isFocusable() { return false; }

	@Override
	default List<? extends IUIComponent> getChildNodes() { return ImmutableList.of(); }

	@Override
	default Optional<? extends IUIComponentContainer> getParentNode() { return getParent(); }

	List<? extends IUIComponentModifier> getModifiersView();

	@SuppressWarnings("UnusedReturnValue")
	boolean addModifier(IUIComponentModifier modifier);

	boolean removeModifier(IUIComponentModifier modifier);

	@SuppressWarnings("UnusedReturnValue")
	boolean moveModifierToTop(IUIComponentModifier modifier);

	@Override
	void initialize(IUIComponentContext context);

	@Override
	void removed(IUIComponentContext context);

	boolean containsPoint(IUIComponentContext context, Point2D point);

	class ParentIterator
			implements Iterator<IUIComponentContainer> {
		private final AtomicReference<IUIComponentContainer> current;

		public ParentIterator(@Nullable IUIComponentContainer current) {
			this.current = new AtomicReference<>(current);
		}

		@Override
		public boolean hasNext() {
			return getCurrent().get() != null;
		}

		@Override
		public IUIComponentContainer next() {
			return AssertionUtilities.assertNonnull(getCurrent().getAndUpdate(cur -> {
				if (cur == null)
					throw new NoSuchElementException();
				return cur.getParent().orElse(null);
			}));
		}

		protected AtomicReference<IUIComponentContainer> getCurrent() { return current; }
	}
}
