package $group__.client.ui.mvvm.views.components;

import $group__.client.ui.core.structures.shapes.IShapeDescriptor;
import $group__.client.ui.mvvm.core.views.components.IUIComponent;
import $group__.client.ui.mvvm.core.views.components.IUIComponentContainer;
import com.google.common.collect.ImmutableMap;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.Optional;

public class UIComponentVirtual
		extends UIComponent {
	protected WeakReference<IUIComponent> relatedComponent = new WeakReference<>(null);

	public UIComponentVirtual(IShapeDescriptor<?> shapeDescriptor) {
		super(shapeDescriptor, ImmutableMap.of());
	}

	@Override
	public Optional<IUIComponentContainer> getParent() { return getRelatedComponent().flatMap(IUIComponent::getParent); }

	public Optional<IUIComponent> getRelatedComponent() { return Optional.ofNullable(relatedComponent.get()); }

	public void setRelatedComponent(@Nullable IUIComponent relatedComponent) { this.relatedComponent = new WeakReference<>(relatedComponent); }
}
