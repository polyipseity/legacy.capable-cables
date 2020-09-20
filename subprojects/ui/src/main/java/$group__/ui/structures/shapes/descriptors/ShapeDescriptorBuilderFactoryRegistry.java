package $group__.ui.structures.shapes.descriptors;

import $group__.ui.UIConfiguration;
import $group__.ui.UIMarkers;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptorBuilderFactory;
import $group__.utilities.LogMessageBuilder;
import $group__.utilities.PreconditionUtilities;
import $group__.utilities.ThrowableUtilities;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.structures.RegistryWithDefaults;
import $group__.utilities.templates.CommonConfigurationTemplate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Supplier;

public final class ShapeDescriptorBuilderFactoryRegistry
		extends RegistryWithDefaults<INamespacePrefixedString, IShapeDescriptorBuilderFactory> {
	private static final ShapeDescriptorBuilderFactoryRegistry INSTANCE = new ShapeDescriptorBuilderFactoryRegistry();
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());
	private static final ImmutableMap<INamespacePrefixedString, Supplier<RegistryObject<? extends IShapeDescriptorBuilderFactory>>> DEFAULTS_SUPPLIER =
			ImmutableMap.<INamespacePrefixedString, Supplier<RegistryObject<? extends IShapeDescriptorBuilderFactory>>>builder()
					.put(DefaultShapeDescriptorBuilderFactory.getDefaultFactoryKey(), () -> new RegistryObject<>(new DefaultShapeDescriptorBuilderFactory()))
					.build();
	private final Map<INamespacePrefixedString, RegistryObject<? extends IShapeDescriptorBuilderFactory>> defaults =
			ImmutableMap.copyOf(Maps.transformValues(getDefaultsSupplier(), Supplier::get));

	private ShapeDescriptorBuilderFactoryRegistry() {
		super(true, UIConfiguration.getInstance().getLogger());
		PreconditionUtilities.requireRunOnceOnly(UIConfiguration.getInstance().getLogger());
	}

	public static DefaultShapeDescriptorBuilderFactory getDefaultFactory() {
		return (DefaultShapeDescriptorBuilderFactory) getInstance()
				.get(DefaultShapeDescriptorBuilderFactory.getDefaultFactoryKey())
				.orElseThrow(InternalError::new)
				.getValue();
	}

	public static ShapeDescriptorBuilderFactoryRegistry getInstance() { return INSTANCE; }

	protected static ImmutableMap<INamespacePrefixedString, Supplier<RegistryObject<? extends IShapeDescriptorBuilderFactory>>> getDefaultsSupplier() { return DEFAULTS_SUPPLIER; }

	@Override
	public <VL extends IShapeDescriptorBuilderFactory> RegistryObject<VL> register(INamespacePrefixedString key, VL value) {
		if (DefaultShapeDescriptorBuilderFactory.getDefaultFactoryKey().equals(key))
			throw ThrowableUtilities.logAndThrow(
					new IllegalStateException(
							new LogMessageBuilder()
									.addMarkers(UIMarkers.getInstance()::getMarkerShape)
									.addKeyValue("key", key).addKeyValue("value", value)
									.addMessages(() -> getResourceBundle().getString("register.default.irreplaceable"))
									.build()
					),
					UIConfiguration.getInstance().getLogger()
			);
		return super.register(key, value);
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	@Override
	protected Map<INamespacePrefixedString, RegistryObject<? extends IShapeDescriptorBuilderFactory>> getDefaults() { return defaults; }
}
