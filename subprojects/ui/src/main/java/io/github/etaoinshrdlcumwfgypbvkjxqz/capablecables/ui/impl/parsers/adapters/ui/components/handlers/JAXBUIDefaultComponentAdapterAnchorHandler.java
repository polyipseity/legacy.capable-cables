package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui.components.handlers;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.Anchor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIComponentManager;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.mvvm.views.components.IUIViewComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.naming.INamed;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.adapters.IJAXBAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.adapters.registries.IJAXBAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.adapters.ui.components.contexts.IJAXBUIComponentAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.JAXBUIUtilities.ObjectFactories;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.interactions.ImmutableShapeAnchor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUISide;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.CacheUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;

import java.util.Map;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressUnboxing;

public class JAXBUIDefaultComponentAdapterAnchorHandler
		extends JAXBUIAbstractSubContextualAdapterHandler<Anchor, IJAXBUIComponentAdapterContext> {
	private final LoadingCache<IUIViewComponent<?, ?>, Map<String, IUIComponent>> viewComponentNamedMaps =
			CacheUtilities.newCacheBuilderSingleThreaded().initialCapacity(CapacityUtilities.getInitialCapacityTiny()).weakKeys()
					.build(CacheLoader.from(view -> {
						@Immutable Map<String, IUIComponent> result = INamed.toNamedMap(AssertionUtilities.assertNonnull(view).getChildrenFlatView().iterator());
						Map<String, IUIComponent> weakResult = MapBuilderUtilities.newMapMakerSingleThreaded().initialCapacity(result.size()).weakValues().makeMap();
						weakResult.putAll(result);
						return weakResult;
					}));

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
														AssertionUtilities.assertNonnull(getViewComponentNamedMaps().getUnchecked(view).get(left.getTarget())),
														(EnumUISide) IJAXBAdapterRegistry.adaptFromJAXB(context,
																ObjectFactories.getDefaultUIObjectFactory().createUiSide(left.getOriginSide())
														),
														(EnumUISide) IJAXBAdapterRegistry.adaptFromJAXB(context,
																ObjectFactories.getDefaultUIObjectFactory().createUiSide(left.getTargetSide())
														),
														suppressUnboxing(left.getBorderThickness())))
														.iterator()
										)
								)
				);
	}

	protected LoadingCache<IUIViewComponent<?, ?>, Map<String, IUIComponent>> getViewComponentNamedMaps() {
		return viewComponentNamedMaps;
	}
}
