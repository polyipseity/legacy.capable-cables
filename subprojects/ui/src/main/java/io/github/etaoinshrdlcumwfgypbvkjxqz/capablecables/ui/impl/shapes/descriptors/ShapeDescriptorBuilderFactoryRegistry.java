package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.descriptors;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors.IShapeDescriptorBuilderFactory;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.core.IRegistryObject;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.core.IRegistryObjectInternal;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.impl.AbstractRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.impl.DefaultRegistryObject;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;
import org.slf4j.Logger;

import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

// TODO should really use ServiceLoader or remove this class
public final class ShapeDescriptorBuilderFactoryRegistry
		extends AbstractRegistry<IIdentifier, IShapeDescriptorBuilderFactory> {
	private static final @Immutable Map<IIdentifier, Supplier<@Nonnull IRegistryObjectInternal<? extends IShapeDescriptorBuilderFactory>>> DEFAULTS_SUPPLIER = ImmutableMap.<IIdentifier, Supplier<@Nonnull IRegistryObjectInternal<? extends IShapeDescriptorBuilderFactory>>>builder()
			.put(DefaultShapeDescriptorBuilderFactory.getDefaultFactoryKey(), () -> new DefaultRegistryObject<>(new DefaultShapeDescriptorBuilderFactory()))
			.build();
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());
	private static final Supplier<@Nonnull ShapeDescriptorBuilderFactoryRegistry> INSTANCE = Suppliers.memoize(ShapeDescriptorBuilderFactoryRegistry::new);
	private static final long serialVersionUID = 6379579158201410218L;

	private final ConcurrentMap<IIdentifier, IRegistryObjectInternal<? extends IShapeDescriptorBuilderFactory>> data;

	private ShapeDescriptorBuilderFactoryRegistry() {
		super(true);
		this.data = MapBuilderUtilities.newMapMakerNormalThreaded().initialCapacity(CapacityUtilities.getInitialCapacitySmall()).makeMap();
		this.data.putAll(Maps.transformValues(getDefaultsSupplier(), Supplier::get));
	}

	protected static @Immutable Map<IIdentifier, Supplier<@Nonnull IRegistryObjectInternal<? extends IShapeDescriptorBuilderFactory>>> getDefaultsSupplier() { return DEFAULTS_SUPPLIER; }

	public static DefaultShapeDescriptorBuilderFactory getDefaultFactory() {
		return (DefaultShapeDescriptorBuilderFactory) getInstance()
				.get(DefaultShapeDescriptorBuilderFactory.getDefaultFactoryKey())
				.orElseThrow(InternalError::new)
				.getValue();
	}

	public static ShapeDescriptorBuilderFactoryRegistry getInstance() { return INSTANCE.get(); }

	@Override
	public <VL extends IShapeDescriptorBuilderFactory> IRegistryObject<VL> register(IIdentifier key, VL value) {
		if (DefaultShapeDescriptorBuilderFactory.getDefaultFactoryKey().equals(key))
			throw new IllegalStateException(
					new LogMessageBuilder()
							.addMarkers(UIMarkers.getInstance()::getMarkerShape)
							.addKeyValue("key", key).addKeyValue("value", value)
							.addMessages(() -> getResourceBundle().getString("register.default.irreplaceable"))
							.build()
			);
		return super.register(key, value);
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	@Override
	protected ConcurrentMap<IIdentifier, IRegistryObjectInternal<? extends IShapeDescriptorBuilderFactory>> getData() {
		return data;
	}

	@Override
	protected Logger getLogger() {
		return UIConfiguration.getInstance().getLogger();
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }
}
