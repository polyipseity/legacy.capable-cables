package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui.components.handlers;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.Anchor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentManager;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.registries.IJAXBAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.ui.components.contexts.IUIDefaultComponentParserContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.UIJAXBUtilities.ObjectFactories;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.JAXBImmutableAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.LegacyJAXBAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.interactions.ImmutableShapeAnchor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUISide;

public class UIDefaultDefaultComponentParserAnchorHandler
		extends UIAbstractDefaultParserObjectHandler<IUIDefaultComponentParserContext, Anchor> {
	@Override
	@SuppressWarnings({"rawtypes", "RedundantSuppression", "RedundantThrows"})
	public void accept(@Nonnull IUIDefaultComponentParserContext context, @Nonnull Anchor object) {
		context.getContainer().ifPresent(container -> {
			if (!(container instanceof IUIComponent))
				return;
			IUIComponent component = (IUIComponent) container;
			component.getManager()
					.flatMap(IUIComponentManager::getView)
					.ifPresent(view ->
							IUIViewComponent.getShapeAnchorController(view).addAnchors(component,
									ImmutableList.of(new ImmutableShapeAnchor(
											IUIView.getNamedTrackers(view).get(IUIComponent.class, object.getTarget())
													.orElseThrow(AssertionError::new),
											(EnumUISide) IJAXBAdapterRegistry.adaptFromJAXB(JAXBImmutableAdapterContext.of(LegacyJAXBAdapterRegistry.getRegistry()),
													ObjectFactories.getDefaultUIObjectFactory().createUiSide(object.getOriginSide())
											),
											(EnumUISide) IJAXBAdapterRegistry.adaptFromJAXB(JAXBImmutableAdapterContext.of(LegacyJAXBAdapterRegistry.getRegistry()),
													ObjectFactories.getDefaultUIObjectFactory().createUiSide(object.getTargetSide())
											),
											object.getBorderThickness()))));
		});
	}
}
