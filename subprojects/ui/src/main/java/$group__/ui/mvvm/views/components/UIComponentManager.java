package $group__.ui.mvvm.views.components;

import $group__.ui.core.binding.IUIPropertyMappingValue;
import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.components.IUIComponentManager;
import $group__.ui.core.mvvm.views.components.IUIViewComponent;
import $group__.ui.core.parsers.components.UIComponentConstructor;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import $group__.utilities.interfaces.INamespacePrefixedString;

import javax.annotation.Nullable;
import java.awt.*;
import java.lang.ref.WeakReference;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public class UIComponentManager<S extends Shape>
		extends UIComponentContainer
		implements IUIComponentManager<S> {
	protected WeakReference<IUIViewComponent<?, ?>> view = new WeakReference<>(null);

	@UIComponentConstructor(type = UIComponentConstructor.EnumConstructorType.MAPPINGS__ID__SHAPE_DESCRIPTOR)
	public UIComponentManager(Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings, @Nullable String id, IShapeDescriptor<S> shapeDescriptor) { super(mappings, id, shapeDescriptor); }

	@Override
	public boolean reshape(Predicate<? super IShapeDescriptor<? super S>> action) throws ConcurrentModificationException { return IUIComponent.reshapeComponent(this, getShapeDescriptor(), action); }

	@SuppressWarnings("unchecked")
	@Override
	public IShapeDescriptor<S> getShapeDescriptor() {
		return (IShapeDescriptor<S>) super.getShapeDescriptor(); // COMMENT should be safe
	}

	@Override
	public boolean isFocusable() { return true; }

	@Override
	public Optional<? extends IUIViewComponent<?, ?>> getView() {
		@Nullable IUIViewComponent<?, ?> ret;
		if ((ret = view.get()) != null)
			return Optional.of(ret);
		return getParent()
				.flatMap(IUIComponent::getManager)
				.flatMap(IUIComponentManager::getView);
	}

	@Override
	public void setView(@Nullable IUIViewComponent<?, ?> view) { this.view = new WeakReference<>(view); }
}
