package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui.components.handlers;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.RendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.IUIView;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.rendering.IUIRendererContainerContainer;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IJAXBAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.ui.components.contexts.IJAXBUIComponentAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;

public class JAXBUIDefaultComponentAdapterRendererContainerHandler
		extends JAXBUIAbstractSubContextualAdapterHandler<RendererContainer, IJAXBUIComponentAdapterContext> {
	public JAXBUIDefaultComponentAdapterRendererContainerHandler() {
		super(IJAXBUIComponentAdapterContext.class);
	}


	@Override
	protected void accept0(IJAXBAdapterContext context, IJAXBUIComponentAdapterContext subContext, RendererContainer left) {
		subContext.getContainer()
				.flatMap(container -> CastUtilities.castChecked(IUIRendererContainerContainer.class, container))
				.map(CastUtilities::<IUIRendererContainerContainer<?>>castUnchecked)
				.ifPresent(container -> {
					container.initializeRendererContainer(left.getName());
					IUIRendererContainer<?> rendererContainer = container.getRendererContainer();

					IUIViewComponent<?, ?> view = subContext.getView()
							.orElseThrow(AssertionError::new);
					IUIView.getNamedTrackers(view).add(IUIRendererContainer.class, rendererContainer);
					IUIView.getThemeStack(view).element().apply(ImmutableList.of(rendererContainer));
				});
	}
}
