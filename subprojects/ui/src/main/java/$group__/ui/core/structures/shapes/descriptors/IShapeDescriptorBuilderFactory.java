package $group__.ui.core.structures.shapes.descriptors;

import $group__.ui.UIConfiguration;
import $group__.ui.structures.shapes.descriptors.builders.RectangularShapeDescriptorBuilder;
import $group__.utilities.PreconditionUtilities;
import $group__.utilities.ThrowableUtilities.BecauseOf;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.structures.NamespacePrefixedString;
import $group__.utilities.structures.Registry;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.function.Function;

public interface IShapeDescriptorBuilderFactory {
	static DefaultFactory getDefault() {
		return (DefaultFactory) RegSDBFactory.INSTANCE.get(DefaultFactory.KEY)
				.orElseThrow(InternalError::new).getValue();
	}

	<S extends Shape> IShapeDescriptorBuilder<S> createBuilder(Class<S> clazz)
			throws ShapeDescriptorBuilderNotFoundException;

	class RegSDBFactory extends Registry<INamespacePrefixedString, IShapeDescriptorBuilderFactory> {
		public static final RegSDBFactory INSTANCE = new RegSDBFactory();

		protected RegSDBFactory() {
			super(true, UIConfiguration.INSTANCE.getLogger());
			PreconditionUtilities.requireRunOnceOnly(UIConfiguration.INSTANCE.getLogger());

			data.put(DefaultFactory.KEY, new Registry.RegistryObject<>(new DefaultFactory()));
		}

		@Override
		public <VL extends IShapeDescriptorBuilderFactory> RegistryObject<VL> register(INamespacePrefixedString key, VL value) {
			if (DefaultFactory.KEY.equals(key))
				throw BecauseOf.illegalArgument("Default factory is not replaceable",
						"DefaultFactory.KEY", DefaultFactory.KEY,
						"key", key,
						"value", value);
			return super.register(key, value);
		}
	}

	class DefaultFactory
			extends Registry<Class<? extends Shape>, Function<? super Class<? extends Shape>, ? extends IShapeDescriptorBuilder<?>>>
			implements IShapeDescriptorBuilderFactory {
		public static final INamespacePrefixedString KEY = new NamespacePrefixedString(INamespacePrefixedString.DEFAULT_NAMESPACE, "default");

		protected DefaultFactory() {
			super(true, UIConfiguration.INSTANCE.getLogger());

			data.put(Rectangle2D.class, new RegistryObject<>(c ->
					new RectangularShapeDescriptorBuilder.Rectangle2DSD()));
			data.put(Ellipse2D.class, new RegistryObject<>(c ->
					new RectangularShapeDescriptorBuilder.Ellipse2DSD()));
		}

		@SuppressWarnings("unchecked")
		@Override
		public <S extends Shape> IShapeDescriptorBuilder<S> createBuilder(Class<S> clazz)
				throws ShapeDescriptorBuilderNotFoundException {
			return (IShapeDescriptorBuilder<S>) get(clazz)
					.orElseThrow(ShapeDescriptorBuilderNotFoundException::new)
					.getValue()
					.apply(clazz); // COMMENT responsibility goes to the registerer
		}
	}
}
