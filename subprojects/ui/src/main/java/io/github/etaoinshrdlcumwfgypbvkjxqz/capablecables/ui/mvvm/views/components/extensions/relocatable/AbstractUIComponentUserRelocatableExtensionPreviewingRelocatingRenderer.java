package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.extensions.relocatable;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.IUIComponentUserRelocatableExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;

public abstract class AbstractUIComponentUserRelocatableExtensionPreviewingRelocatingRenderer
		extends AbstractUIComponentUserRelocatableExtensionRelocatingRenderer {
	public AbstractUIComponentUserRelocatableExtensionPreviewingRelocatingRenderer(Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings) {
		super(mappings);
	}

	@Override
	public final void render(IUIComponentContext context, IUIComponentUserRelocatableExtension.IRelocateData data) {
		context.getViewContext().getInputDevices().getPointerDevice()
				.ifPresent(pointerDevice -> {
					IUIComponent container = context.getPath().getPathEnd().orElseThrow(AssertionError::new);
					Point2D currentCursorPosition = pointerDevice.getPositionView();
					Rectangle2D resultRectangle = container.getShapeDescriptor().getShapeOutput().getBounds2D();
					render0(context, data.handle((Point2D) currentCursorPosition.clone(), resultRectangle, resultRectangle));
				});
	}

	public abstract void render0(IUIComponentContext context, Rectangle2D rectangle);
}
