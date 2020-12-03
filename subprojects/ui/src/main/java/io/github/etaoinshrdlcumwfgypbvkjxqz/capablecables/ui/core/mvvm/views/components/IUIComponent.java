package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.traits.IHasBindingMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentActiveLifecycleModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentStructureLifecycleModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentTransformChildrenModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIComponentRenderer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainerContainer;
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
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.time.core.ITicker;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Predicate;

public interface IUIComponent
		extends INamed, INode, IShapeDescriptorProvider, IHasBinding, IHasBindingMap, IUIEventTarget, IExtensionContainer<INamespacePrefixedString>, IUIRendererContainerContainer<IUIComponentRenderer<?>>,
		IUIComponentStructureLifecycleModifier, IUIComponentActiveLifecycleModifier, IUIComponentTransformChildrenModifier {
	void update(ITicker ticker);

	static <T> Optional<T> getYoungestParentInstanceOf(IUIComponent self, Class<T> clazz) {
		for (Iterator<IUIComponent> iterator = new ParentIterator(self.getParent().orElse(null));
		     iterator.hasNext(); ) {
			IUIComponent element = iterator.next();
			if (clazz.isInstance(element))
				return Optional.of(clazz.cast(element));
		}
		return Optional.empty();
	}

	Optional<? extends IUIComponent> getParent();

	static Shape getContextualShape(IUIComponentContext context, IUIComponent component) { return IUIComponentContext.createContextualShape(context, getShape(component)); }

	static Shape getShape(IUIComponent component) { return component.getShapeDescriptor().getShapeOutput(); }

	@Immutable
	@SuppressWarnings({"rawtypes", "RedundantSuppression"})
	static IUIComponentContext createContextTo(IUIComponent component) {
		IUIComponentContext context =
				component
						.getManager()
						.flatMap(IUIComponentManager::getView)
						.flatMap(IUIViewComponent::createComponentContext)
						.orElseThrow(IllegalStateException::new);
		getPath(component).asList()
				.forEach(element -> context.getMutator().push(context, element));
		return context;
	}

	default Optional<? extends IUIComponentManager<?>> getManager() { return UIDefaultCacheExtension.CacheUniversal.getManager().getValue().get(this); }

	@Immutable
	static IPath<IUIComponent> getPath(IUIComponent component) {
		List<IUIComponent> path = new ArrayList<>(CapacityUtilities.getInitialCapacitySmall());
		path.add(component);
		new ParentIterator(component.getParent().orElse(null))
				.forEachRemaining(path::add);
		return ImmutablePath.of(Lists.reverse(path));
	}

	static <S extends Shape> boolean reshapeComponent(IUIComponent self, IShapeDescriptor<? super S> shapeDescriptor, Predicate<? super IShapeDescriptor<? super S>> action)
			throws ConcurrentModificationException {
		return self.modifyShape(() ->
				action.test(shapeDescriptor));
	}

	@SuppressWarnings("UnstableApiUsage")
	static Map<String, IUIComponent> getNamedChildrenMapView(IUIComponent instance) {
		return instance.getChildrenView().stream().unordered()
				.filter(c -> c.getName().isPresent())
				.collect(ImmutableMap.toImmutableMap(c -> {
					Optional<? extends String> id = c.getName();
					assert id.isPresent();
					return id.get();
				}, Function.identity()));
	}

	List<IUIComponent> getChildrenView();

	boolean isVisible();

	void setVisible(boolean visible);

	void setActive(boolean active);

	@Override
	default boolean isFocusable() { return false; }

	void onParentChange(@Nullable IUIComponent previous, @Nullable IUIComponent next);

	@Override
	void transformChildren(AffineTransform transform);

	@SuppressWarnings("UnusedReturnValue")
	boolean addChildren(Iterable<? extends IUIComponent> components);

	boolean addChildAt(int index, IUIComponent component);

	@SuppressWarnings("UnusedReturnValue")
	boolean removeChildren(Iterable<? extends IUIComponent> components);

	boolean moveChildTo(int index, IUIComponent component);

	@SuppressWarnings("UnusedReturnValue")
	boolean moveChildToTop(IUIComponent component);

	List<? extends IUIComponentModifier> getModifiersView();

	@SuppressWarnings("UnusedReturnValue")
	boolean addModifier(IUIComponentModifier modifier);

	boolean removeModifier(IUIComponentModifier modifier);

	@SuppressWarnings("UnusedReturnValue")
	boolean moveModifierToTop(IUIComponentModifier modifier);

	boolean containsPoint(IUIComponentContext context, Point2D point);

	@Override
	default List<? extends IUIComponent> getChildNodes() { return getChildrenView(); }

	@Override
	default Optional<? extends IUIComponent> getParentNode() { return getParent(); }

	class ParentIterator
			implements Iterator<IUIComponent> {
		private final AtomicReference<IUIComponent> current;

		public ParentIterator(@Nullable IUIComponent current) {
			this.current = new AtomicReference<>(current);
		}

		@Override
		public boolean hasNext() {
			return getCurrent().get() != null;
		}

		@Override
		public IUIComponent next() {
			return AssertionUtilities.assertNonnull(getCurrent().getAndUpdate(cur -> {
				if (cur == null)
					throw new NoSuchElementException();
				return cur.getParent().orElse(null);
			}));
		}

		protected AtomicReference<IUIComponent> getCurrent() { return current; }
	}
}
