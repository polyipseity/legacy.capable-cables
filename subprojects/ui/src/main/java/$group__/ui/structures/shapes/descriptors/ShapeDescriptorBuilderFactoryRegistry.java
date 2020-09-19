package $group__.ui.structures.shapes.descriptors;

import $group__.ui.UIConfiguration;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptorBuilderFactory;
import $group__.utilities.LogMessageBuilder;
import $group__.utilities.PreconditionUtilities;
import $group__.utilities.ThrowableUtilities;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.structures.Registry;
import $group__.utilities.templates.CommonConfigurationTemplate;

import java.util.ResourceBundle;

public final class ShapeDescriptorBuilderFactoryRegistry
		extends Registry<INamespacePrefixedString, IShapeDescriptorBuilderFactory> {
	private static final ShapeDescriptorBuilderFactoryRegistry INSTANCE = new ShapeDescriptorBuilderFactoryRegistry();
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());

	private ShapeDescriptorBuilderFactoryRegistry() {
		super(true, UIConfiguration.getInstance().getLogger());
		PreconditionUtilities.requireRunOnceOnly(UIConfiguration.getInstance().getLogger());

		this.data.put(DefaultShapeDescriptorBuilderFactory.getDefaultFactoryKey(), new RegistryObject<>(new DefaultShapeDescriptorBuilderFactory()));
	}

	public static DefaultShapeDescriptorBuilderFactory getDefaultFactory() {
		return (DefaultShapeDescriptorBuilderFactory) getInstance()
				.get(DefaultShapeDescriptorBuilderFactory.getDefaultFactoryKey())
				.orElseThrow(InternalError::new)
				.getValue();
	}

	public static ShapeDescriptorBuilderFactoryRegistry getInstance() { return INSTANCE; }

	@Override
	public <VL extends IShapeDescriptorBuilderFactory> RegistryObject<VL> register(INamespacePrefixedString key, VL value) {
		if (DefaultShapeDescriptorBuilderFactory.getDefaultFactoryKey().equals(key))
			throw ThrowableUtilities.logAndThrow(
					new IllegalStateException(
							new LogMessageBuilder()
									.addKeyValue("key", key).addKeyValue("value", value)
									.appendMessages(getResourceBundle().getString("register.default.irreplaceable"))
									.build()
					),
					UIConfiguration.getInstance().getLogger()
			);
		return super.register(key, value);
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }
}
