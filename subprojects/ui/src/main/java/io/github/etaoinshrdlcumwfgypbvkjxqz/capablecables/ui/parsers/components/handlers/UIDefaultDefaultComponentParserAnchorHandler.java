package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.components.handlers;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.components.Anchor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentManager;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.components.contexts.IUIDefaultComponentParserContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures.shapes.interactions.ShapeAnchor;

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
							view.getShapeAnchorController().addAnchors(component,
									ImmutableList.of(new ShapeAnchor(
											view.getNamedTrackers().get(IUIComponent.class, object.getTarget())
													.orElseThrow(AssertionError::new),
											object.getOriginSide().toJava(),
											object.getTargetSide().toJava(),
											object.getBorderThickness()))));
		});
	}
}
