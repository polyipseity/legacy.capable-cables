package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.mvvm.views.components.extensions.resizable;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.binding.IUIPropertyMappingValue;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.extensions.IUIComponentUserResizableExtension;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ui.UIObjectUtilities;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;

public abstract class AbstractUIComponentUserResizableExtensionPreviewingRelocatingRenderer
		extends AbstractUIComponentUserResizeableExtensionRelocatingRenderer {
	public AbstractUIComponentUserResizableExtensionPreviewingRelocatingRenderer(Map<INamespacePrefixedString, IUIPropertyMappingValue> mappings) {
		super(mappings);
	}

	@Override
	public final void render(IUIComponentContext context, IUIComponentUserResizableExtension.IResizeData data) {
		context.getViewContext().getInputDevices().getPointerDevice()
				.ifPresent(pointerDevice -> {
					IUIComponent container = context.getPath().getPathEnd().orElseThrow(AssertionError::new);
					Point2D currentCursorPosition = pointerDevice.getPositionView();
					Rectangle2D resultRectangle = container.getShapeDescriptor().getShapeOutput().getBounds2D();
					UIObjectUtilities.acceptRectangularShape(
							data.handle((Point2D) currentCursorPosition.clone(), resultRectangle, resultRectangle),
							(x, y, w, h) -> {
								assert x != null;
								assert y != null;
								assert w != null;
								assert h != null;
								resultRectangle.setFrame(x, y, w - 1, h - 1);
							});
					render0(context, resultRectangle);
				});
	}

	public abstract void render0(IUIComponentContext context, Rectangle2D rectangle);
}
