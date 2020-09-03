package $group__.utilities.binding;

import $group__.utilities.CapacityUtilities;
import $group__.utilities.CastUtilities;
import $group__.utilities.DynamicUtilities;
import $group__.utilities.ThrowableUtilities.Try;
import $group__.utilities.binding.core.fields.IBindingField;
import $group__.utilities.binding.core.methods.IBindingMethod;
import $group__.utilities.binding.core.traits.IHasBinding;
import $group__.utilities.collections.MapUtilities;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
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
			= MapUtilities.newMapMakerNormalThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_MEDIUM).makeMap();
	protected static final ConcurrentMap<Class<?>, BiFunction<? super Set<EnumOptions>, ?, ? extends Iterable<IBindingMethod<?>>>> BINDING_METHODS_MAP
			= MapUtilities.newMapMakerNormalThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_MEDIUM).makeMap();
	protected static final ConcurrentMap<Class<?>, Function<?, ? extends Iterable<IHasBinding>>> HAS_BINDING_VARIABLES_MAP
			= MapUtilities.newMapMakerNormalThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_MEDIUM).makeMap();
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

	public static Iterable<IHasBinding> getHasBindingsVariables(Object object) { return getHasBindingsVariablesFunction(object.getClass()).apply(CastUtilities.castUnchecked(object)); /* COMMENT should be safe */ }

	public enum EnumOptions {
		SELF,
		VARIABLES,
	}
}
