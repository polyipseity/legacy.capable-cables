package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui.components.handlers;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.Anchor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentManager;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IJAXBAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.registries.IJAXBAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.ui.components.contexts.IJAXBUIComponentAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.JAXBUIUtilities.ObjectFactories;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.interactions.ImmutableShapeAnchor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUISide;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;

public class JAXBUIDefaultComponentAdapterAnchorHandler
		extends JAXBUIAbstractSubContextualAdapterHandler<Anchor, IJAXBUIComponentAdapterContext> {
	public JAXBUIDefaultComponentAdapterAnchorHandler() {
		super(IJAXBUIComponentAdapterContext.class);
	}

	@Override
	@SuppressWarnings({"rawtypes", "RedundantSuppression"})
	protected void accept0(IJAXBAdapterContext context, IJAXBUIComponentAdapterContext subContext, Anchor left) {
		subContext.getContainer()
				.flatMap(container -> CastUtilities.castChecked(IUIComponent.class, container))
				.ifPresent(container ->
						container.getManager()
								.flatMap(IUIComponentManager::getView)
								.ifPresent(view ->
										IUIViewComponent.getShapeAnchorController(view).addAnchors(container,
												ImmutableList.of(new ImmutableShapeAnchor(
														IUIView.getNamedTrackers(view).get(IUIComponent.class, left.getTarget())
																.orElseThrow(AssertionError::new),
														(EnumUISide) IJAXBAdapterRegistry.adaptFromJAXB(context,
																ObjectFactories.getDefaultUIObjectFactory().createUiSide(left.getOriginSide())
														),
														(EnumUISide) IJAXBAdapterRegistry.adaptFromJAXB(context,
																ObjectFactories.getDefaultUIObjectFactory().createUiSide(left.getTargetSide())
														),
														left.getBorderThickness()))))
				);
	}
}
