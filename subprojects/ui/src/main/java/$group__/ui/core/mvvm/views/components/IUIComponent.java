package $group__.ui.core.mvvm.views.components;

import $group__.ui.core.binding.traits.IHasBindingMap;
import $group__.ui.core.mvvm.views.events.IUIEventTarget;
import $group__.ui.core.structures.IUIComponentContext;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.ui.core.structures.shapes.interactions.IShapeDescriptorProvider;
import $group__.ui.mvvm.views.components.extensions.caches.UICacheExtension;
import $group__.utilities.binding.core.traits.IHasBinding;
import $group__.utilities.extensions.core.IExtensionContainer;
import $group__.utilities.structures.INamespacePrefixedString;
import $group__.utilities.structures.ImmutableNamespacePrefixedString;
import $group__.utilities.structures.paths.INode;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/*
TODO auto resizing based on min size and preferred size
 */
public interface IUIComponent
		extends INode, IShapeDescriptorProvider, IHasBinding, IHasBindingMap, IUIEventTarget, IExtensionContainer<INamespacePrefixedString> {
	String PROPERTY_ID = INamespacePrefixedString.StaticHolder.DEFAULT_PREFIX + "id";
	INamespacePrefixedString PROPERTY_ID_LOCATION = new ImmutableNamespacePrefixedString(PROPERTY_ID);

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

	Optional<? extends String> getID();

	Optional<? extends IUIComponentContainer> getParent();

	default Optional<? extends IUIComponentManager<?>> getManager() { return UICacheExtension.CacheUniversal.MANAGER.getValue().get(this); }

	boolean isVisible();

	void setVisible(boolean visible);

	@SuppressWarnings("EmptyMethod")
	void onIndexMove(int previous, int next);

	void onParentChange(@Nullable IUIComponentContainer previous, @Nullable IUIComponentContainer next);

	void setActive(boolean active);

	@Override
	default boolean isFocusable() { return false; }

	@Override
	default List<? extends IUIComponent> getChildNodes() { return ImmutableList.of(); }

	@Override
	default Optional<? extends IUIComponentContainer> getParentNode() { return getParent(); }

	default void initialize(IUIComponentContext context) {}

	default void removed(IUIComponentContext context) {}
}
