package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.components.handlers;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.Anchor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentManager;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.registries.IJAXBAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.UIJAXBUtilities.ObjectFactories;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.LegacyJAXBAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.components.contexts.IUIDefaultComponentParserContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.interactions.DefaultShapeAnchor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUISide;

public class UIDefaultDefaultComponentParserAnchorHandler
		extends UIAbstractDefaultParserObjectHandler<IUIDefaultComponentParserContext, Anchor> {
	@Override
	@SuppressWarnings({"rawtypes", "RedundantSuppression", "RedundantThrows"})
	public void acceptNonnull(@SuppressWarnings("unused") IUIDefaultComponentParserContext context, Anchor object) {
		context.getContainer().ifPresent(container -> {
			if (!(container instanceof IUIComponent))
				return;
			IUIComponent component = (IUIComponent) container;
			component.getManager()
					.flatMap(IUIComponentManager::getView)
					.ifPresent(view ->
							IUIViewComponent.getShapeAnchorController(view).addAnchors(component,
									ImmutableList.of(new DefaultShapeAnchor(
											IUIView.getNamedTrackers(view).get(IUIComponent.class, object.getTarget())
													.orElseThrow(AssertionError::new),
											(EnumUISide) IJAXBAdapterRegistry.adaptFromJAXB(LegacyJAXBAdapterRegistry.getRegistry(),
													ObjectFactories.getDefaultUIObjectFactory().createUiSide(object.getOriginSide())
											),
											(EnumUISide) IJAXBAdapterRegistry.adaptFromJAXB(LegacyJAXBAdapterRegistry.getRegistry(),
													ObjectFactories.getDefaultUIObjectFactory().createUiSide(object.getTargetSide())
											),
											object.getBorderThickness()))));
		});
	}
}
