package $group__.client.ui.mvvm.core.views.components;

import $group__.client.ui.core.structures.shapes.IShapeDescriptor;
import $group__.client.ui.mvvm.core.binding.IHasBinding;
import $group__.client.ui.mvvm.core.extensions.IUIExtension;
import $group__.client.ui.mvvm.core.structures.IUIPropertyMappingValue;
import $group__.client.ui.mvvm.core.views.events.IUIEventTarget;
import $group__.client.ui.mvvm.core.views.paths.IUINode;
import $group__.client.ui.mvvm.views.components.extensions.caches.UIExtensionCache;
import $group__.client.ui.mvvm.views.events.bus.EventUIComponent;
import $group__.utilities.NamespaceUtilities;
import $group__.utilities.events.EnumEventHookStage;
import $group__.utilities.events.EventUtilities;
import $group__.utilities.extensions.IExtensionContainer;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/*
TODO auto resizing based on min size and preferred size
 */
public interface IUIComponent
		extends IUINode, IHasBinding, IUIEventTarget, IExtensionContainer<ResourceLocation, IUIExtension<? extends ResourceLocation, ? super IUIComponent>> {
	String PROPERTY_ID = NamespaceUtilities.NAMESPACE_MINECRAFT_DEFAULT_PREFIX + "id";
	ResourceLocation PROPERTY_ID_LOCATION = new ResourceLocation(PROPERTY_ID);

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

	static <S extends Shape> boolean reshapeComponent(IUIComponent self, IShapeDescriptor<? super S> shapeDescriptor, Function<? super IShapeDescriptor<? super S>, ? extends Boolean> action)
			throws ConcurrentModificationException {
		return EventUtilities.callWithPrePostHooks(() ->
						shapeDescriptor.modify(() ->
								action.apply(shapeDescriptor)),
				new EventUIComponent.ShapeDescriptorModify(EnumEventHookStage.PRE, self),
				new EventUIComponent.ShapeDescriptorModify(EnumEventHookStage.POST, self));
	}

	Optional<String> getID();

	Optional<IUIComponentContainer> getParent();

	default Optional<IUIComponentManager<?>> getManager() { return UIExtensionCache.CacheUniversal.MANAGER.getValue().get(this); }

	IShapeDescriptor<?> getShapeDescriptor();

	boolean isVisible();

	void setVisible(boolean visible);

	void onIndexMove(int previous, int next);

	void onParentChange(@Nullable IUIComponentContainer previous, @Nullable IUIComponentContainer next);

	void setActive(boolean active);

	@Override
	default boolean isFocusable() { return false; }

	@Override
	default List<IUINode> getChildNodes() { return ImmutableList.of(); }

	@Override
	default Optional<IUINode> getParentNode() { return getParent().map(Function.identity()); }

	Map<ResourceLocation, IUIPropertyMappingValue> getPropertyMappingView();
}
