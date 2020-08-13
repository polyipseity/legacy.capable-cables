package $group__.client.ui.mvvm.core.views.components;

import $group__.client.ui.mvvm.core.binding.IHasBinding;
import $group__.client.ui.mvvm.core.extensions.IUIExtension;
import $group__.client.ui.mvvm.core.structures.IAffineTransformStack;
import $group__.client.ui.mvvm.core.structures.IShapeDescriptor;
import $group__.client.ui.mvvm.core.structures.IUIPropertyMappingValue;
import $group__.client.ui.mvvm.core.views.events.IUIEventTarget;
import $group__.client.ui.mvvm.core.views.nodes.IUINode;
import $group__.client.ui.mvvm.views.components.extensions.UIExtensionCache;
import $group__.utilities.extensions.IExtensionContainer;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/*
TODO auto resizing based on min size and preferred size
 */
public interface IUIComponent
		extends IUINode, IHasBinding, IUIEventTarget, IExtensionContainer<ResourceLocation, IUIExtension<? extends ResourceLocation, ? super IUIComponent>> {
	static <C extends IUIComponent> C createComponent(Supplier<? extends C> constructor) {
		C ret = constructor.get();
		ret.onCreated();
		return ret;
	}

	@OverridingMethodsMustInvokeSuper
	default void onCreated() {}

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

	Optional<IUIComponentContainer> getParent();

	default Optional<IUIComponentManager<?>> getManager() { return UIExtensionCache.CacheUniversal.MANAGER.getValue().get(this); }

	static ImmutableList<IUIComponent> computeComponentPath(IUIComponent component) {
		ImmutableList.Builder<IUIComponent> builder = ImmutableList.builder();
		builder.add(component);
		Optional<? extends IUIComponentContainer> parent = component.getParent();
		while (parent.isPresent()) {
			builder.add(parent.get());
			parent = parent.get().getParent();
		}
		return builder.build().reverse();
	}

	IShapeDescriptor<?> getShapeDescriptor();

	boolean isVisible();

	void setVisible(boolean visible);

	boolean contains(final IAffineTransformStack stack, Point2D point);

	void onIndexMove(int previous, int next);

	void onParentChange(@Nullable IUIComponentContainer previous, @Nullable IUIComponentContainer next);

	void setActive(boolean active);

	@Override
	default boolean isFocusable() { return false; }

	@Override
	default List<IUINode> getChildNodes() { return ImmutableList.of(); }

	@Override
	default Optional<IUINode> getParentNode() { return getParent().map(Function.identity()); }

	Map<String, IUIPropertyMappingValue> getPropertyMappingView();
}
