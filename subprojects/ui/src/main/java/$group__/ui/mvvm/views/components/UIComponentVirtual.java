package $group__.ui.mvvm.views.components;

import $group__.ui.core.mvvm.views.components.IUIComponent;
import $group__.ui.core.mvvm.views.components.IUIComponentContainer;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import com.google.common.collect.ImmutableMap;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.Optional;

public class UIComponentVirtual
		extends UIComponent {
	protected WeakReference<IUIComponent> relatedComponent = new WeakReference<>(null);

	public UIComponentVirtual(IShapeDescriptor<?> shapeDescriptor) {
		super(ImmutableMap.of(), shapeDescriptor);
	}

	@Override
	public Optional<? extends IUIComponentContainer> getParent() { return getRelatedComponent().flatMap(IUIComponent::getParent); }

	public Optional<? extends IUIComponent> getRelatedComponent() { return Optional.ofNullable(relatedComponent.get()); }

	public void setRelatedComponent(@Nullable IUIComponent relatedComponent) { this.relatedComponent = new WeakReference<>(relatedComponent); }
}
