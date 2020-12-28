package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.interactions;

import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components.IUIComponent;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors.IShapeDescriptor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.interactions.IShapeDescriptorDynamicDetector;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.interactions.IShapeDescriptorProvider;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;

import java.awt.*;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class UIFunctionalComponentShapeDescriptorDynamicDetector
		implements IShapeDescriptorDynamicDetector {
	private final ConcurrentMap<IUIComponent, Shape> componentLastShapeMap =
			MapBuilderUtilities.newMapMakerSingleThreaded().weakKeys().initialCapacity(CapacityUtilities.getInitialCapacityLarge()).makeMap();
	private final Supplier<? extends Iterable<? extends IUIComponent>> componentIterableSupplier;

	public UIFunctionalComponentShapeDescriptorDynamicDetector(Supplier<? extends Iterable<? extends IUIComponent>> componentIterableSupplier) {
		this.componentIterableSupplier = componentIterableSupplier;
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public void detect() {
		Streams.stream(getComponentIterableSupplier().get()).unordered()
				.forEach(component -> {
					IShapeDescriptor<?> shapeDescriptor = component.getShapeDescriptor();
					if (shapeDescriptor.isDynamic()) {
						Shape shapeOutput = shapeDescriptor.getShapeOutput();
						boolean different =
								!Optional.ofNullable(getComponentLastShapeMap().put(component, shapeOutput))
										.filter(Predicate.isEqual(shapeOutput))
										.isPresent();
						if (different)
							IShapeDescriptorProvider.refreshShape(component);
					}
				});
	}

	protected Supplier<? extends Iterable<? extends IUIComponent>> getComponentIterableSupplier() {
		return componentIterableSupplier;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected ConcurrentMap<IUIComponent, Shape> getComponentLastShapeMap() {
		return componentLastShapeMap;
	}
}
