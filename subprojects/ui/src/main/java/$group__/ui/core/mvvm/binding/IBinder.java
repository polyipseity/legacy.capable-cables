package $group__.ui.core.mvvm.binding;

import $group__.utilities.CapacityUtilities;
import $group__.utilities.CastUtilities;
import $group__.utilities.DynamicUtilities;
import $group__.utilities.collections.MapUtilities;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.reactive.DisposableObserverAuto;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import io.reactivex.rxjava3.observers.DisposableObserver;
import net.jodah.typetools.TypeResolver;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Not thread-safe.
 */
@SuppressWarnings("UnusedReturnValue")
public interface IBinder {
	static <B extends IHasBindingKey> Multimap<INamespacePrefixedString, B> sortAndTrimBindings(Iterable<B> bindings) {
		@SuppressWarnings("UnstableApiUsage") Multimap<INamespacePrefixedString, B> ret = MultimapBuilder
				.linkedHashKeys(CapacityUtilities.INITIAL_CAPACITY_MEDIUM)
				.linkedHashSetValues(CapacityUtilities.INITIAL_CAPACITY_TINY).build(); // COMMENT order is important
		bindings.forEach(f -> f.getBindingKey().ifPresent(rl -> ret.put(rl, f)));
		return ret;
	}

	static DisposableObserver<IBinderAction> createBinderActionObserver(IBinder binder) {
		return new DisposableObserverAuto<IBinderAction>() {
			@Override
			public void onNext(@Nonnull IBinderAction o) {
				try {
					switch (o.getType()) {
						case BIND:
							binder.bindFields(o.getFields());
							binder.bindMethods(o.getMethods());
							break;
						case UNBIND:
							binder.unbindFields(o.getFields());
							binder.unbindMethods(o.getMethods());
							break;
						default:
							onError(new InternalError());
							break;
					}
				} catch (BindingTransformerNotFoundException ex) {
					onError(ex);
				}
			}
		};
	}

	@Nullable
	static <T, R> R transform(Map<Class<?>, Map<Class<?>, Function<?, ?>>> transformers, @Nullable T value, Class<T> from, Class<R> to)
			throws BindingTransformerNotFoundException {
		return IBinder.getTransformer(transformers, from, to)
				.map(t -> t.apply(value))
				.orElseThrow(() -> new BindingTransformerNotFoundException(
						"Cannot find transformer for '" + from + "' -> '" + to + "' in transformers:" + System.lineSeparator()
								+ transformers));
	}

	boolean bindFields(Iterable<? extends IBindingField<?>> fields)
			throws BindingTransformerNotFoundException;

	@SuppressWarnings("unchecked")
	static <T, R> Optional<Function<T, R>> putTransformer(Map<Class<?>, Map<Class<?>, Function<?, ?>>> transformers, Class<T> from, Class<R> to, Function<T, R> transformer) {
		// COMMENT should be of the right type
		return Optional.of(transformers.computeIfAbsent(from, k ->
				MapUtilities.getMapMakerSingleThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_SMALL).makeMap()))
				.map(m -> (Function<T, R>) m.put(to, transformer));
	}

	@SuppressWarnings("unchecked")
	static <T, R> Optional<Function<T, R>> removeTransformer(Map<Class<?>, Map<Class<?>, Function<?, ?>>> transformers, Class<T> from, Class<R> to) {
		// COMMENT should be of the right type
		return Optional.ofNullable(transformers.get(from))
				.map(m -> (Function<T, R>) m.remove(to));
	}

	static Optional<Class<?>>[] resolveFunctionTypes(Function<?, ?> transformer) { return DynamicUtilities.Extensions.wrapTypeResolverResults(TypeResolver.resolveRawArguments(Function.class, transformer.getClass())); }

	@SuppressWarnings("unchecked")
	static <T, R> Optional<Function<T, R>> getTransformer(Map<Class<?>, Map<Class<?>, Function<?, ?>>> transformers, Class<T> from, Class<R> to) {
		// COMMENT should be of the right type
		return from.equals(to) ? Optional.of(CastUtilities::castUncheckedNullable) :
				Optional.ofNullable(transformers.get(from))
						.map(m -> (Function<T, R>) m.get(to));
	}

	boolean bindMethods(Iterable<? extends IBindingMethod<?>> methods);

	boolean unbindFields(Iterable<? extends IBindingField<?>> fields);

	boolean unbindMethods(Iterable<? extends IBindingMethod<?>> methods);

	<T, R> Optional<? extends Function<T, R>> addFieldTransformer(Function<T, R> transformer);

	<T, R> Optional<? extends Function<T, R>> addMethodTransformer(Function<T, R> transformer);

	<T, R> Optional<? extends Function<T, R>> removeFieldTransformer(Class<T> from, Class<R> to);

	<T, R> Optional<? extends Function<T, R>> removeMethodTransformer(Class<T> from, Class<R> to);

	default boolean unbindAll() { return unbindAllFields() | unbindAllMethods(); }

	boolean unbindAllFields();

	boolean unbindAllMethods();
}
