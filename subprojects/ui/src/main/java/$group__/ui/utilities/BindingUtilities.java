package $group__.ui.utilities;

import $group__.ui.core.mvvm.binding.IBindingField;
import $group__.ui.core.mvvm.binding.IBindingMethod;
import $group__.ui.core.mvvm.binding.IHasBinding;
import $group__.ui.parsers.components.UIDOMPrototypeParser;
import $group__.utilities.*;
import $group__.utilities.ThrowableUtilities.Try;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Node;

import javax.annotation.Nullable;
import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

public enum BindingUtilities {
	;
	protected static final ConcurrentMap<Class<?>, Function<?, Iterable<IBindingField<?>>>> BINDING_FIELDS_MAP
			= MapUtilities.getMapMakerMultiThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_MEDIUM).makeMap();
	protected static final ConcurrentMap<Class<?>, Function<?, Iterable<IBindingMethod<?>>>> BINDING_METHODS_MAP
			= MapUtilities.getMapMakerMultiThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_MEDIUM).makeMap();
	protected static final ConcurrentMap<Class<?>, Function<?, Iterable<IHasBinding>>> HAS_BINDING_VARIABLES_MAP
			= MapUtilities.getMapMakerMultiThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_MEDIUM).makeMap();
	private static final Logger LOGGER = LogManager.getLogger();

	public static Iterable<IBindingField<?>> getBindingFields(Object object) { return getBindingFieldsFunction(object.getClass()).apply(CastUtilities.castUnchecked(object)); /* COMMENT should be safe */ }

	@SuppressWarnings({"SynchronizationOnLocalVariableOrMethodParameter", "UnstableApiUsage"})
	public static <T> Function<T, Iterable<IBindingField<?>>> getBindingFieldsFunction(Class<T> clazz) {
		synchronized (clazz) {
			@SuppressWarnings("unchecked") @Nullable Function<T, Iterable<IBindingField<?>>> ret =
					(Function<T, Iterable<IBindingField<?>>>) BINDING_FIELDS_MAP.get(clazz); // COMMENT should be safe
			if (ret != null)
				return ret;
			Collection<Field> fs = DynamicUtilities.getAllFields(clazz);
			ImmutableSet<Function<T, Optional<IBindingField<?>>>> b = fs.stream().unordered()
					.filter(f -> !Modifier.isStatic(f.getModifiers()) && IBindingField.class.isAssignableFrom(f.getType()))
					.map(f -> Try.call(() -> DynamicUtilities.IMPL_LOOKUP.unreflectGetter(f), LOGGER))
					.filter(Optional::isPresent)
					.map(m -> (Function<T, Optional<IBindingField<?>>>) o ->
							Try.call(() -> (IBindingField<?>) m.get().invoke(o), LOGGER))
					.collect(ImmutableSet.toImmutableSet());
			Function<T, Iterable<IHasBinding>> c = getHasBindingsVariablesFunction(clazz);
			ret = o -> Streams.concat(b.stream().unordered()
							.map(f -> f.apply(o))
							.filter(Optional::isPresent)
							.map(Optional::get),
					Streams.stream(c.apply(o)).unordered()
							.flatMap(hb -> Streams.stream(hb.getBindingFields())))
					.collect(ImmutableSet.toImmutableSet());
			BINDING_FIELDS_MAP.put(clazz, ret);
			return ret;
		}
	}

	@SuppressWarnings({"SynchronizationOnLocalVariableOrMethodParameter", "UnstableApiUsage"})
	public static <T> Function<T, Iterable<IHasBinding>> getHasBindingsVariablesFunction(Class<T> clazz) {
		synchronized (clazz) {
			@SuppressWarnings("unchecked") @Nullable Function<T, Iterable<IHasBinding>> ret =
					(Function<T, Iterable<IHasBinding>>) HAS_BINDING_VARIABLES_MAP.get(clazz); // COMMENT should be safe
			if (ret != null)
				return ret;
			ImmutableSet<Function<T, Optional<IHasBinding>>> v = DynamicUtilities.getAllFields(clazz).stream().unordered()
					.filter(f -> !Modifier.isStatic(f.getModifiers()) && IHasBinding.class.isAssignableFrom(f.getType()))
					.map(f -> Try.call(() -> DynamicUtilities.IMPL_LOOKUP.unreflectGetter(f), LOGGER))
					.filter(Optional::isPresent)
					.map(m -> (Function<T, Optional<IHasBinding>>) o ->
							Try.call(() -> (IHasBinding) m.get().invoke(o), LOGGER))
					.collect(ImmutableSet.toImmutableSet());
			ret = o -> v.stream().unordered()
					.map(f -> f.apply(o))
					.filter(Optional::isPresent)
					.map(Optional::get)
					.collect(ImmutableSet.toImmutableSet());
			HAS_BINDING_VARIABLES_MAP.put(clazz, ret);
			return ret;
		}
	}

	public static Iterable<IBindingMethod<?>> getBindingMethods(Object object) { return getBindingMethodsFunction(object.getClass()).apply(CastUtilities.castUnchecked(object)); /* COMMENT should be safe */ }

	@SuppressWarnings({"SynchronizationOnLocalVariableOrMethodParameter", "UnstableApiUsage"})
	public static <T> Function<T, Iterable<IBindingMethod<?>>> getBindingMethodsFunction(Class<T> clazz) {
		synchronized (clazz) {
			@SuppressWarnings("unchecked") @Nullable Function<T, Iterable<IBindingMethod<?>>> ret =
					(Function<T, Iterable<IBindingMethod<?>>>) BINDING_METHODS_MAP.get(clazz); // COMMENT should be safe
			if (ret != null)
				return ret;
			Collection<Field> fs = DynamicUtilities.getAllFields(clazz);
			ImmutableSet<Function<T, Optional<IBindingMethod<?>>>> b = fs.stream().unordered()
					.filter(f -> !Modifier.isStatic(f.getModifiers()) && IBindingMethod.class.isAssignableFrom(f.getType()))
					.map(f -> Try.call(() -> DynamicUtilities.IMPL_LOOKUP.unreflectGetter(f), LOGGER))
					.filter(Optional::isPresent)
					.map(m -> (Function<T, Optional<IBindingMethod<?>>>) o ->
							Try.call(() -> (IBindingMethod<?>) m.get().invoke(o), LOGGER))
					.collect(ImmutableSet.toImmutableSet());
			Function<T, Iterable<IHasBinding>> c = getHasBindingsVariablesFunction(clazz);
			ret = o -> Streams.concat(b.stream().unordered()
							.map(f -> f.apply(o))
							.filter(Optional::isPresent)
							.map(Optional::get),
					Streams.stream(c.apply(o)).unordered()
							.flatMap(hb -> Streams.stream(hb.getBindingMethods())))
					.collect(ImmutableSet.toImmutableSet());
			BINDING_METHODS_MAP.put(clazz, ret);
			return ret;
		}
	}

	public static Iterable<IHasBinding> getHasBindingsVariables(Object object) { return getHasBindingsVariablesFunction(object.getClass()).apply(CastUtilities.castUnchecked(object)); /* COMMENT should be safe */ }

	public enum Deserializers {
		;

		private static final Logger LOGGER = LogManager.getLogger();

		public static <T> Optional<T> warnIfNotPresent(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<T> optional, Node node) {
			if (!optional.isPresent())
				LOGGER.warn(() -> LoggerUtilities.EnumMessages.SUFFIX_WITH_THROWABLE.makeMessage(
						LoggerUtilities.EnumMessages.FACTORY_PARAMETERIZED_MESSAGE.makeMessage("Cannot deserialize node:{}{}",
								System.lineSeparator(), node),
						ThrowableUtilities.createIfDebug().orElse(null)
				));
			return optional;
		}

		public static Optional<Boolean> deserializeBoolean(Node node) {
			return warnIfNotPresent(DOMUtilities.getAttributeValue(node, "boolean")
					.map(Boolean::valueOf), node);
		}

		public static Optional<Byte> deserializeByte(Node node) {
			return warnIfNotPresent(DOMUtilities.getAttributeValue(node, "byte")
					.map(Byte::valueOf), node);
		}

		public static Optional<Short> deserializeShort(Node node) {
			return warnIfNotPresent(DOMUtilities.getAttributeValue(node, "short")
					.map(Short::valueOf), node);
		}

		public static Optional<Integer> deserializeInt(Node node) {
			return warnIfNotPresent(DOMUtilities.getAttributeValue(node, "int")
					.map(Integer::valueOf), node);
		}

		public static Optional<Long> deserializeLong(Node node) {
			return warnIfNotPresent(DOMUtilities.getAttributeValue(node, "long")
					.map(Long::valueOf), node);
		}

		public static Optional<Float> deserializeFloat(Node node) {
			return warnIfNotPresent(DOMUtilities.getAttributeValue(node, "float")
					.map(Float::valueOf), node);
		}

		public static Optional<Double> deserializeDouble(Node node) {
			return warnIfNotPresent(DOMUtilities.getAttributeValue(node, "double")
					.map(Double::valueOf), node);
		}

		public static Optional<String> deserializeString(Node node) {
			return warnIfNotPresent(DOMUtilities.getAttributeValue(node, "string"), node);
		}

		public static Optional<Color> deserializeColor(Node node) {
			return warnIfNotPresent(DOMUtilities.getChildByTagNameNS(node, UIDOMPrototypeParser.SCHEMA_NAMESPACE_URI, "color")
					.map(nc -> new Color(
							Integer.valueOf(DOMUtilities.getAttributeValue(nc, "red").orElse("00"), RadixUtilities.RADIX_HEX),
							Integer.valueOf(DOMUtilities.getAttributeValue(nc, "green").orElse("00"), RadixUtilities.RADIX_HEX),
							Integer.valueOf(DOMUtilities.getAttributeValue(nc, "blue").orElse("00"), RadixUtilities.RADIX_HEX),
							Integer.valueOf(DOMUtilities.getAttributeValue(nc, "alpha").orElse("FF"), RadixUtilities.RADIX_HEX))), node);
		}
	}
}
