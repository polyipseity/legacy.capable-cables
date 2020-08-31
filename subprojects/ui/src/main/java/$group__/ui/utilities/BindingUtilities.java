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
import java.lang.reflect.Modifier;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public enum BindingUtilities {
	;
	protected static final ConcurrentMap<Class<?>, BiFunction<? super Set<EnumOptions>, ?, ? extends Iterable<IBindingField<?>>>> BINDING_FIELDS_MAP
			= MapUtilities.getMapMakerMultiThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_MEDIUM).makeMap();
	protected static final ConcurrentMap<Class<?>, BiFunction<? super Set<EnumOptions>, ?, ? extends Iterable<IBindingMethod<?>>>> BINDING_METHODS_MAP
			= MapUtilities.getMapMakerMultiThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_MEDIUM).makeMap();
	protected static final ConcurrentMap<Class<?>, Function<?, ? extends Iterable<IHasBinding>>> HAS_BINDING_VARIABLES_MAP
			= MapUtilities.getMapMakerMultiThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_MEDIUM).makeMap();
	private static final Logger LOGGER = LogManager.getLogger();

	public static Iterable<IBindingField<?>> getBindingFields(Object object, Set<EnumOptions> options) { return getBindingFieldsFunction(object.getClass(), options).apply(CastUtilities.castUnchecked(object)); /* COMMENT should be safe */ }

	@SuppressWarnings({"SynchronizationOnLocalVariableOrMethodParameter", "UnstableApiUsage"})
	public static <T> Function<T, Iterable<IBindingField<?>>> getBindingFieldsFunction(Class<T> clazz, Set<EnumOptions> options) {
		synchronized (clazz) {
			final BiFunction<? super Set<EnumOptions>, T, ? extends Iterable<IBindingField<?>>> retF;
			final Set<EnumOptions> optionsF = EnumSet.copyOf(options);
			@SuppressWarnings("unchecked") @Nullable BiFunction<? super Set<EnumOptions>, T, ? extends Iterable<IBindingField<?>>> ret =
					(BiFunction<? super Set<EnumOptions>, T, ? extends Iterable<IBindingField<?>>>) BINDING_FIELDS_MAP.get(clazz); // COMMENT should be safe
			if (ret != null) {
				retF = ret;
				return o -> retF.apply(optionsF, o);
			}
			ImmutableSet<Function<T, Optional<IBindingField<?>>>> b = DynamicUtilities.getAllFields(clazz).stream().unordered()
					.filter(f -> !Modifier.isStatic(f.getModifiers()) && IBindingField.class.isAssignableFrom(f.getType()))
					.map(f -> Try.call(() -> DynamicUtilities.IMPL_LOOKUP.unreflectGetter(f), LOGGER))
					.filter(Optional::isPresent)
					.map(m -> (Function<T, Optional<IBindingField<?>>>) o ->
							Try.call(() -> (IBindingField<?>) m.get().invoke(o), LOGGER))
					.collect(ImmutableSet.toImmutableSet());
			Function<T, Iterable<IHasBinding>> c = getHasBindingsVariablesFunction(clazz);
			ret = (op, o) -> Streams.concat(
					op.contains(EnumOptions.SELF) ? b.stream().unordered()
							.map(f -> f.apply(o))
							.filter(Optional::isPresent)
							.map(Optional::get) : Stream.empty(),
					op.contains(EnumOptions.VARIABLES) ? Streams.stream(c.apply(o)).unordered()
							.<IBindingField<?>>flatMap(hb -> Streams.stream(hb.getBindingFields())) : Stream.empty())
					.collect(ImmutableSet.toImmutableSet());
			BINDING_FIELDS_MAP.put(clazz, ret);
			retF = ret;
			return o -> retF.apply(optionsF, o);
		}
	}

	public static Iterable<IBindingMethod<?>> getBindingMethods(Object object, Set<EnumOptions> options) { return getBindingMethodsFunction(object.getClass(), options).apply(CastUtilities.castUnchecked(object)); /* COMMENT should be safe */ }

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

	@SuppressWarnings({"SynchronizationOnLocalVariableOrMethodParameter", "UnstableApiUsage"})
	public static <T> Function<T, Iterable<IBindingMethod<?>>> getBindingMethodsFunction(Class<T> clazz, Set<EnumOptions> options) {
		synchronized (clazz) {
			final BiFunction<? super Set<EnumOptions>, T, ? extends Iterable<IBindingMethod<?>>> retF;
			final Set<EnumOptions> optionsF = EnumSet.copyOf(options);
			@SuppressWarnings("unchecked") @Nullable BiFunction<? super Set<EnumOptions>, T, ? extends Iterable<IBindingMethod<?>>> ret =
					(BiFunction<? super Set<EnumOptions>, T, ? extends Iterable<IBindingMethod<?>>>) BINDING_METHODS_MAP.get(clazz); // COMMENT should be safe
			if (ret != null) {
				retF = ret;
				return o -> retF.apply(optionsF, o);
			}
			ImmutableSet<Function<T, Optional<IBindingMethod<?>>>> b = DynamicUtilities.getAllFields(clazz).stream().unordered()
					.filter(f -> !Modifier.isStatic(f.getModifiers()) && IBindingMethod.class.isAssignableFrom(f.getType()))
					.map(f -> Try.call(() -> DynamicUtilities.IMPL_LOOKUP.unreflectGetter(f), LOGGER))
					.filter(Optional::isPresent)
					.map(m -> (Function<T, Optional<IBindingMethod<?>>>) o ->
							Try.call(() -> (IBindingMethod<?>) m.get().invoke(o), LOGGER))
					.collect(ImmutableSet.toImmutableSet());
			Function<T, Iterable<IHasBinding>> c = getHasBindingsVariablesFunction(clazz);
			ret = (op, o) -> Streams.concat(
					op.contains(EnumOptions.SELF) ? b.stream().unordered()
							.map(f -> f.apply(o))
							.filter(Optional::isPresent)
							.map(Optional::get) : Stream.empty(),
					op.contains(EnumOptions.VARIABLES) ? Streams.stream(c.apply(o)).unordered()
							.<IBindingMethod<?>>flatMap(hb -> Streams.stream(hb.getBindingMethods())) : Stream.empty())
					.collect(ImmutableSet.toImmutableSet());
			BINDING_METHODS_MAP.put(clazz, ret);
			retF = ret;
			return o -> retF.apply(optionsF, o);
		}
	}

	public enum EnumOptions {
		SELF,
		VARIABLES,
	}

	public static Iterable<IHasBinding> getHasBindingsVariables(Object object) { return getHasBindingsVariablesFunction(object.getClass()).apply(CastUtilities.castUnchecked(object)); /* COMMENT should be safe */ }

	public enum Deserializers {
		;

		private static final Logger LOGGER = LogManager.getLogger();

		public static Optional<Boolean> deserializeBoolean(Node node, boolean nullable) {
			return warnIfNotPresent(nullable, DOMUtilities.getAttributeValue(node, "boolean")
					.map(Boolean::valueOf), node);
		}

		// TODO some values are nullable
		public static <T> Optional<T> warnIfNotPresent(boolean nullable, @SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<T> optional, Node node) {
			if (!(nullable || optional.isPresent()))
				LOGGER.warn(() -> LoggerUtilities.EnumMessages.SUFFIX_WITH_THROWABLE.makeMessage(
						LoggerUtilities.EnumMessages.FACTORY_PARAMETERIZED_MESSAGE.makeMessage("Cannot deserialize node:{}{}",
								System.lineSeparator(), node),
						ThrowableUtilities.createIfDebug().orElse(null)
				));
			return optional;
		}

		public static Optional<Byte> deserializeByte(Node node, boolean nullable) {
			return warnIfNotPresent(nullable, DOMUtilities.getAttributeValue(node, "byte")
					.map(Byte::valueOf), node);
		}

		public static Optional<Short> deserializeShort(Node node, boolean nullable) {
			return warnIfNotPresent(nullable, DOMUtilities.getAttributeValue(node, "short")
					.map(Short::valueOf), node);
		}

		public static Optional<Integer> deserializeInt(Node node, boolean nullable) {
			return warnIfNotPresent(nullable, DOMUtilities.getAttributeValue(node, "int")
					.map(Integer::valueOf), node);
		}

		public static Optional<Long> deserializeLong(Node node, boolean nullable) {
			return warnIfNotPresent(nullable, DOMUtilities.getAttributeValue(node, "long")
					.map(Long::valueOf), node);
		}

		public static Optional<Float> deserializeFloat(Node node, boolean nullable) {
			return warnIfNotPresent(nullable, DOMUtilities.getAttributeValue(node, "float")
					.map(Float::valueOf), node);
		}

		public static Optional<Double> deserializeDouble(Node node, boolean nullable) {
			return warnIfNotPresent(nullable, DOMUtilities.getAttributeValue(node, "double")
					.map(Double::valueOf), node);
		}

		public static Optional<String> deserializeString(Node node, boolean nullable) {
			return warnIfNotPresent(nullable, DOMUtilities.getAttributeValue(node, "string"), node);
		}

		public static Optional<Color> deserializeColor(Node node, boolean nullable) {
			return warnIfNotPresent(nullable, DOMUtilities.getChildByTagNameNS(node, UIDOMPrototypeParser.SCHEMA_NAMESPACE_URI, "color")
					.map(nc -> new Color(
							Integer.valueOf(DOMUtilities.getAttributeValue(nc, "red").orElse("00"), RadixUtilities.RADIX_HEX),
							Integer.valueOf(DOMUtilities.getAttributeValue(nc, "green").orElse("00"), RadixUtilities.RADIX_HEX),
							Integer.valueOf(DOMUtilities.getAttributeValue(nc, "blue").orElse("00"), RadixUtilities.RADIX_HEX),
							Integer.valueOf(DOMUtilities.getAttributeValue(nc, "alpha").orElse("FF"), RadixUtilities.RADIX_HEX))), node);
		}
	}
}
