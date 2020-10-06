package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.traits.IHasBindingMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentLifecycleModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.modifiers.IUIComponentModifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.events.IUIEventTarget;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.interactions.IShapeDescriptorProvider;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.extensions.caches.UICacheExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.traits.IHasBinding;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.extensions.core.IExtensionContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.paths.INode;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/*
TODO auto resizing based on min size and preferred size
 */
public interface IUIComponent
		extends INode, IShapeDescriptorProvider, IHasBinding, IHasBindingMap, IUIEventTarget, IExtensionContainer<INamespacePrefixedString>,
		IUIComponentLifecycleModifier {
	static <T> Optional<T> getYoungestParentInstanceOf(IUIComponent self, Class<T> clazz) {
		Optional<? extends IUIComponentContainer> parent = self.getParent();
		Optional<T> ret = Optional.empty();
		while (parent.isPresent()) {
			IUIComponentContainer p = parent.get();
			if (clazz.isInstance(p)) {
				ret = Optional.of(clazz.cast(p));
				break;
			}
			parent = p.getParent();
		}
		return ret;
	}

	static <S extends Shape> boolean reshapeComponent(IUIComponent self, IShapeDescriptor<? super S> shapeDescriptor, Predicate<? super IShapeDescriptor<? super S>> action)
			throws ConcurrentModificationException {
		return self.modifyShape(() ->
				action.test(shapeDescriptor));
	}

	Optional<? extends String> getName();

	Optional<? extends IUIComponentContainer> getParent();

	default Optional<? extends IUIComponentManager<?>> getManager() { return UICacheExtension.CacheUniversal.MANAGER.getValue().get(this); }

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

	boolean addModifier(IUIComponentModifier modifier);

	boolean removeModifier(IUIComponentModifier modifier);

	boolean moveModifierToTop(IUIComponentModifier modifier);

	@Override
	void initialize(IUIComponentContext context);

	@Override
	void removed(IUIComponentContext context);

	boolean containsPoint(IUIComponentContext context, Point2D point);

	enum StaticHolder {
		;

		public static Shape getContextualShape(IUIComponentContext context, IUIComponent component) { return IUIComponentContext.StaticHolder.createContextualShape(context, getShape(component)); }

		public static Shape getShape(IUIComponent component) { return component.getShapeDescriptor().getShapeOutput(); }
	}
}
