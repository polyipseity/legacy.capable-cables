package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.components.handlers;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.components.RendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.components.contexts.IUIDefaultComponentParserContext;

public class UIDefaultDefaultComponentParserRendererContainerHandler
		extends UIAbstractDefaultParserObjectHandler<IUIDefaultComponentParserContext, RendererContainer> {
	@Override
	public void acceptNonnull(IUIDefaultComponentParserContext context, RendererContainer object) {
		context.getContainer().ifPresent(container -> {
			if (!(container instanceof IUIRendererContainerContainer))
				return;
			IUIRendererContainerContainer<?> rendererContainerContainer = ((IUIRendererContainerContainer<?>) container);

			rendererContainerContainer.initializeRendererContainer(object.getName());
			context.getView()
					.orElseThrow(AssertionError::new)
					.getNamedTrackers()
					.add(IUIRendererContainer.class, rendererContainerContainer.getRendererContainer());
		});
	}
}
