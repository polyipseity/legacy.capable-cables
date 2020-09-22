package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures.shapes.descriptors;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.descriptors.IShapeDescriptorBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.descriptors.IShapeDescriptorBuilderFactory;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.structures.shapes.descriptors.NoSuchShapeDescriptorBuilderException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.structures.shapes.descriptors.builders.RectangularShapeDescriptorBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.RegistryWithDefaults;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import java.util.function.Supplier;

public class DefaultShapeDescriptorBuilderFactory
		extends RegistryWithDefaults<Class<? extends Shape>, Supplier<? extends IShapeDescriptorBuilder<?>>>
		implements IShapeDescriptorBuilderFactory {
	private static final INamespacePrefixedString DEFAULT_FACTORY_KEY = new ImmutableNamespacePrefixedString(INamespacePrefixedString.StaticHolder.DEFAULT_NAMESPACE, "default");
	private static final ImmutableMap<Class<? extends Shape>, Supplier<RegistryObject<Supplier<? extends IShapeDescriptorBuilder<?>>>>> DEFAULTS_SUPPLIER =
			ImmutableMap.<Class<? extends Shape>, Supplier<RegistryObject<Supplier<? extends IShapeDescriptorBuilder<?>>>>>builder()
					.put(Rectangle2D.class, () -> new RegistryObject<>(RectangularShapeDescriptorBuilder.Rectangle2DSD::new))
					.put(Ellipse2D.class, () -> new RegistryObject<>(RectangularShapeDescriptorBuilder.Ellipse2DSD::new))
					.build();
	private final ImmutableMap<Class<? extends Shape>, RegistryObject<? extends Supplier<? extends IShapeDescriptorBuilder<?>>>> defaults =
			ImmutableMap.copyOf(Maps.transformValues(getDefaultsSupplier(), Supplier::get));

	public DefaultShapeDescriptorBuilderFactory() { super(true, UIConfiguration.getInstance().getLogger()); }

	protected static ImmutableMap<Class<? extends Shape>, Supplier<RegistryObject<Supplier<? extends IShapeDescriptorBuilder<?>>>>> getDefaultsSupplier() { return DEFAULTS_SUPPLIER; }

	public static INamespacePrefixedString getDefaultFactoryKey() { return DEFAULT_FACTORY_KEY; }

	@SuppressWarnings("unchecked")
	@Override
	public <S extends Shape> IShapeDescriptorBuilder<S> createBuilder(Class<S> clazz)
			throws NoSuchShapeDescriptorBuilderException {
		return (IShapeDescriptorBuilder<S>)
				AssertionUtilities.assertNonnull(
						get(clazz)
								.orElseThrow(NoSuchShapeDescriptorBuilderException::new)
								.getValue()
								.get()); // COMMENT responsibility goes to the registerer
	}

	@Override
	protected Map<Class<? extends Shape>, RegistryObject<? extends Supplier<? extends IShapeDescriptorBuilder<?>>>> getDefaults() { return defaults; }
}
