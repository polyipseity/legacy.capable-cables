package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.components.handlers;

import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.components.Anchor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponentManager;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.components.IParserContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures.shapes.interactions.ShapeAnchor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IConsumer3;

public class DefaultUIComponentParserAnchorHandler<TH extends Throwable>
		implements IConsumer3<IParserContext, Object, Anchor, TH> {
	@Override
	@SuppressWarnings({"rawtypes", "RedundantSuppression"})
	public void accept(@SuppressWarnings("unused") IParserContext context, Object container, Anchor object)
			throws TH {
		if (!(container instanceof IUIComponent))
			return;
		IUIComponent component = (IUIComponent) container;
		component.getManager()
				.flatMap(IUIComponentManager::getView)
				.ifPresent(view ->
						view.getShapeAnchorController().addAnchors(component,
								ImmutableList.of(new ShapeAnchor(
										IUIViewComponent.StaticHolder.getComponentByID(view, object.getTarget()),
										object.getOriginSide().toJava(),
										object.getTargetSide().toJava(),
										object.getBorderThickness()))));
	}
}
