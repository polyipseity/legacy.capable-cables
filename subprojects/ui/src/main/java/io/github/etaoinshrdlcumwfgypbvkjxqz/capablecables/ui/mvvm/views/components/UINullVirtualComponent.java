package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.references.OptionalWeakReference;

import javax.annotation.Nullable;
import java.util.Optional;

public class UINullVirtualComponent
		extends UIComponent {
	private OptionalWeakReference<IUIComponent> relatedComponent = new OptionalWeakReference<>(null);

	public UINullVirtualComponent(IShapeDescriptor<?> shapeDescriptor) {
		super(ImmutableMap.of(), null, shapeDescriptor);
	}

	@Override
	public Optional<? extends IUIComponentContainer> getParent() { return getRelatedComponent().flatMap(IUIComponent::getParent); }

	public Optional<? extends IUIComponent> getRelatedComponent() { return relatedComponent.getOptional(); }

	public void setRelatedComponent(@Nullable IUIComponent relatedComponent) { this.relatedComponent = new OptionalWeakReference<>(relatedComponent); }
}
