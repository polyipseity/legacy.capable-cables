package $group__.client.ui.utilities;

import $group__.client.ui.mvvm.core.binding.IBindingField;
import $group__.client.ui.mvvm.core.binding.IBindingMethod;
import $group__.client.ui.mvvm.core.binding.IHasBinding;
import $group__.utilities.CapacityUtilities;
import $group__.utilities.CastUtilities;
import $group__.utilities.DynamicUtilities;
import $group__.utilities.specific.MapUtilities;
import $group__.utilities.specific.ThrowableUtilities.Try;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.collect.Streams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Stream;

public enum BindingUtilities {
	;
	// TODO Should these be a cache instead?
	protected static final ConcurrentMap<Class<?>, Function<?, Iterable<IBindingField<?>>>> BINDING_FIELDS_MAP
			= MapUtilities.getMapMakerMultiThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_MEDIUM).makeMap();
	protected static final ConcurrentMap<Class<?>, Function<?, Iterable<IBindingMethod<?>>>> BINDING_METHODS_MAP
			= MapUtilities.getMapMakerMultiThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_MEDIUM).makeMap();
	private static final Logger LOGGER = LogManager.getLogger();

	public static Iterable<IBindingField<?>> getBindingFields(Object object) { return getBindingFieldsFunction(object.getClass()).apply(CastUtilities.castUnchecked(object)); /* COMMENT should be safe */ }

	@SuppressWarnings({"SynchronizationOnLocalVariableOrMethodParameter", "UnstableApiUsage"})
	public static <T> Function<T, Iterable<IBindingField<?>>> getBindingFieldsFunction(Class<T> clazz) {
		synchronized (clazz) {
			@Nullable Function<T, Iterable<IBindingField<?>>> ret = CastUtilities.castUncheckedNullable(BINDING_FIELDS_MAP.get(clazz)); // COMMENT should be safe
			if (ret != null)
				return ret;
			Stream<Field> fs = DynamicUtilities.getSuperclasses(clazz).stream().unordered()
					.flatMap(c -> Sets.newHashSet(c.getDeclaredFields()).stream());
			ImmutableSet<Function<T, Optional<IBindingField<?>>>> b = fs.distinct()
					.filter(f -> !Modifier.isStatic(f.getModifiers()) && IBindingField.class.isAssignableFrom(f.getType()))
					.map(f -> Try.call(() -> DynamicUtilities.IMPL_LOOKUP.unreflectGetter(f), LOGGER))
					.filter(Optional::isPresent)
					.map(m -> (Function<T, Optional<IBindingField<?>>>) o ->
							Try.call(() -> (IBindingField<?>) m.get().invoke(o), LOGGER))
					.collect(ImmutableSet.toImmutableSet());
			ImmutableSet<Function<T, Optional<IHasBinding>>> c = fs
					.filter(f -> !Modifier.isStatic(f.getModifiers()) && IHasBinding.class.isAssignableFrom(f.getType()))
					.map(f -> Try.call(() -> DynamicUtilities.IMPL_LOOKUP.unreflectGetter(f), LOGGER))
					.filter(Optional::isPresent)
					.map(m -> (Function<T, Optional<IHasBinding>>) o ->
							Try.call(() -> (IHasBinding) m.get().invoke(o), LOGGER))
					.collect(ImmutableSet.toImmutableSet());
			ret = o -> Streams.concat(b.stream().unordered()
							.map(f -> f.apply(o))
							.filter(Optional::isPresent)
							.map(Optional::get),
					c.stream().unordered()
							.map(f -> f.apply(o))
							.filter(Optional::isPresent)
							.map(Optional::get)
							.flatMap(hb -> Streams.stream(hb.getBindingFields())))
					.collect(ImmutableSet.toImmutableSet());
			BINDING_FIELDS_MAP.put(clazz, ret);
			return ret;
		}
	}

	public static Iterable<IBindingMethod<?>> getBindingMethods(Object object) { return getBindingMethodsFunction(object.getClass()).apply(CastUtilities.castUnchecked(object)); /* COMMENT should be safe */ }

	@SuppressWarnings({"SynchronizationOnLocalVariableOrMethodParameter", "UnstableApiUsage"})
	public static <T> Function<T, Iterable<IBindingMethod<?>>> getBindingMethodsFunction(Class<T> clazz) {
		synchronized (clazz) {
			@Nullable Function<T, Iterable<IBindingMethod<?>>> ret = CastUtilities.castUncheckedNullable(BINDING_METHODS_MAP.get(clazz)); // COMMENT should be safe
			if (ret != null)
				return ret;
			Stream<Field> fs = DynamicUtilities.getSuperclasses(clazz).stream().unordered()
					.flatMap(c -> Sets.newHashSet(c.getDeclaredFields()).stream());
			ImmutableSet<Function<T, Optional<IBindingMethod<?>>>> b = fs.distinct()
					.filter(f -> !Modifier.isStatic(f.getModifiers()) && IBindingMethod.class.isAssignableFrom(f.getType()))
					.map(f -> Try.call(() -> DynamicUtilities.IMPL_LOOKUP.unreflectGetter(f), LOGGER))
					.filter(Optional::isPresent)
					.map(m -> (Function<T, Optional<IBindingMethod<?>>>) o ->
							Try.call(() -> (IBindingMethod<?>) m.get().invoke(o), LOGGER))
					.collect(ImmutableSet.toImmutableSet());
			ImmutableSet<Function<T, Optional<IHasBinding>>> c = fs
					.filter(f -> !Modifier.isStatic(f.getModifiers()) && IHasBinding.class.isAssignableFrom(f.getType()))
					.map(f -> Try.call(() -> DynamicUtilities.IMPL_LOOKUP.unreflectGetter(f), LOGGER))
					.filter(Optional::isPresent)
					.map(m -> (Function<T, Optional<IHasBinding>>) o ->
							Try.call(() -> (IHasBinding) m.get().invoke(o), LOGGER))
					.collect(ImmutableSet.toImmutableSet());
			ret = o -> Streams.concat(b.stream().unordered()
							.map(f -> f.apply(o))
							.filter(Optional::isPresent)
							.map(Optional::get),
					c.stream().unordered()
							.map(f -> f.apply(o))
							.filter(Optional::isPresent)
							.map(Optional::get)
							.flatMap(hb -> Streams.stream(hb.getBindingMethods())))
					.collect(ImmutableSet.toImmutableSet());
			BINDING_METHODS_MAP.put(clazz, ret);
			return ret;
		}
	}
}
