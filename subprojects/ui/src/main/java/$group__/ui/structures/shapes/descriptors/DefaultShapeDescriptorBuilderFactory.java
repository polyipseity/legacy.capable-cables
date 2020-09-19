package $group__.ui.structures.shapes.descriptors;

import $group__.ui.UIConfiguration;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptorBuilder;
import $group__.ui.core.structures.shapes.descriptors.IShapeDescriptorBuilderFactory;
import $group__.ui.core.structures.shapes.descriptors.ShapeDescriptorBuilderNotFoundException;
import $group__.ui.structures.shapes.descriptors.builders.RectangularShapeDescriptorBuilder;
import $group__.utilities.AssertionUtilities;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.structures.NamespacePrefixedString;
import $group__.utilities.structures.RegistryWithDefaults;
import com.google.common.collect.ImmutableMap;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Optional;
import java.util.function.Supplier;

public class DefaultShapeDescriptorBuilderFactory
		extends RegistryWithDefaults<Class<? extends Shape>, Supplier<? extends IShapeDescriptorBuilder<?>>>
		implements IShapeDescriptorBuilderFactory {
	private static final INamespacePrefixedString DEFAULT_FACTORY_KEY = new NamespacePrefixedString(INamespacePrefixedString.DEFAULT_NAMESPACE, "default");
	private static final ImmutableMap<Class<? extends Shape>, RegistryObject<? extends Supplier<? extends IShapeDescriptorBuilder<?>>>> DEFAULT_ENTRIES =
			ImmutableMap.<Class<? extends Shape>, RegistryObject<? extends Supplier<? extends IShapeDescriptorBuilder<?>>>>builder()
					.put(Rectangle2D.class, new RegistryObject<>(RectangularShapeDescriptorBuilder.Rectangle2DSD::new))
					.put(Ellipse2D.class, new RegistryObject<>(RectangularShapeDescriptorBuilder.Ellipse2DSD::new))
					.build();

	public DefaultShapeDescriptorBuilderFactory() { super(true, getDefaultEntries(), UIConfiguration.getInstance().getLogger()); }

	protected static ImmutableMap<Class<? extends Shape>, RegistryObject<? extends Supplier<? extends IShapeDescriptorBuilder<?>>>> getDefaultEntries() { return DEFAULT_ENTRIES; }

	public static INamespacePrefixedString getDefaultFactoryKey() { return DEFAULT_FACTORY_KEY; }

	@SuppressWarnings("unchecked")
	@Override
	public <S extends Shape> IShapeDescriptorBuilder<S> createBuilder(Class<S> clazz)
			throws ShapeDescriptorBuilderNotFoundException {
		return (IShapeDescriptorBuilder<S>)
				AssertionUtilities.assertNonnull(
						get(clazz)
								.orElseThrow(ShapeDescriptorBuilderNotFoundException::new)
								.getValue()
								.get()); // COMMENT responsibility goes to the registerer
	}

	@Override
	public Optional<? extends RegistryObject<? extends Supplier<? extends IShapeDescriptorBuilder<?>>>> get(Class<? extends Shape> key) {
		Optional<? extends RegistryObject<? extends Supplier<? extends IShapeDescriptorBuilder<?>>>> ret = super.get(key);
		if (!ret.isPresent())
			ret = Optional.ofNullable(getDefaultEntries().get(key));
		return ret;
	}
}
