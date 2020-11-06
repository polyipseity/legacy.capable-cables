package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.descriptors;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors.IShapeDescriptorBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors.IShapeDescriptorBuilderFactory;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.shapes.descriptors.NoSuchShapeDescriptorBuilderException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.shapes.descriptors.builders.RectangularShapeDescriptorBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapBuilderUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.ImmutableNamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.core.IRegistryObjectInternal;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.impl.AbstractRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.impl.DefaultRegistryObject;
import org.slf4j.Logger;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

public class DefaultShapeDescriptorBuilderFactory
		extends AbstractRegistry<Class<? extends Shape>, Supplier<@Nonnull ? extends IShapeDescriptorBuilder<?>>>
		implements IShapeDescriptorBuilderFactory {
	private static final INamespacePrefixedString DEFAULT_FACTORY_KEY = ImmutableNamespacePrefixedString.of(StaticHolder.getDefaultNamespace(), "default");
	private static final @Immutable Map<Class<? extends Shape>, Supplier<@Nonnull IRegistryObjectInternal<Supplier<@Nonnull ? extends IShapeDescriptorBuilder<?>>>>> DEFAULTS_SUPPLIER =
			ImmutableMap.<Class<? extends Shape>, Supplier<@Nonnull IRegistryObjectInternal<Supplier<@Nonnull ? extends IShapeDescriptorBuilder<?>>>>>builder()
					.put(Rectangle2D.class, () -> new DefaultRegistryObject<>(RectangularShapeDescriptorBuilder.Rectangle2DSD::new))
					.put(Ellipse2D.class, () -> new DefaultRegistryObject<>(RectangularShapeDescriptorBuilder.Ellipse2DSD::new))
					.build();
	private static final long serialVersionUID = -7885480361684661050L;

	private final ConcurrentMap<Class<? extends Shape>, IRegistryObjectInternal<? extends Supplier<@Nonnull ? extends IShapeDescriptorBuilder<?>>>> data;

	public DefaultShapeDescriptorBuilderFactory() {
		super(true);
		this.data = MapBuilderUtilities.newMapMakerNormalThreaded().initialCapacity(CapacityUtilities.getInitialCapacityMedium()).makeMap();
		this.data.putAll(Maps.transformValues(getDefaultsSupplier(), Supplier::get));
	}

	protected static @Immutable Map<Class<? extends Shape>, Supplier<@Nonnull IRegistryObjectInternal<Supplier<@Nonnull ? extends IShapeDescriptorBuilder<?>>>>> getDefaultsSupplier() { return DEFAULTS_SUPPLIER; }

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

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	@Override
	protected Map<Class<? extends Shape>, IRegistryObjectInternal<? extends Supplier<@Nonnull ? extends IShapeDescriptorBuilder<?>>>> getData() {
		return data;
	}

	@Override
	protected Logger getLogger() {
		return UIConfiguration.getInstance().getLogger();
	}
}
