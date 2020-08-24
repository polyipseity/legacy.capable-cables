package $group__.client.ui.mvvm.core.binding;

import $group__.utilities.CapacityUtilities;
import $group__.utilities.DynamicUtilities;
import $group__.utilities.MapUtilities;
import $group__.utilities.interfaces.INamespacePrefixedString;
import $group__.utilities.reactive.DisposableObserverAuto;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import io.reactivex.rxjava3.observers.DisposableObserver;
import net.jodah.typetools.TypeResolver;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Not thread-safe.
 */
public interface IBinder {
	static <B extends IHasBindingKey> Multimap<INamespacePrefixedString, B> sortAndTrimBindings(Iterable<B> bindings) {
		@SuppressWarnings("UnstableApiUsage") Multimap<INamespacePrefixedString, B> ret = MultimapBuilder
				.hashKeys(CapacityUtilities.INITIAL_CAPACITY_MEDIUM)
				.hashSetValues(CapacityUtilities.INITIAL_CAPACITY_TINY).build();
		bindings.forEach(f -> f.getBindingKey().ifPresent(rl -> ret.put(rl, f)));
		return ret;
	}

	static DisposableObserver<IBinderAction> createBinderActionObserver(IBinder binder) {
		return new DisposableObserverAuto<IBinderAction>() {
			@Override
			public void onNext(@Nonnull IBinderAction o) {
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
			}
		};
	}

	@SuppressWarnings("unchecked")
	static <T, R> Optional<Function<T, R>> getTransformer(Map<Class<?>, Map<Class<?>, Function<?, ?>>> transformers, Class<T> from, Class<R> to) {
		// COMMENT should be of the right type
		return from.equals(to) ? Optional.of(t -> (R) t) :
				Optional.ofNullable(transformers.get(from))
						.map(m -> (Function<T, R>) m.get(to));
	}

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

	boolean bindFields(Iterable<IBindingField<?>> fields);

	boolean bindMethods(Iterable<IBindingMethod<?>> methods);

	boolean unbindFields(Iterable<IBindingField<?>> fields);

	boolean unbindMethods(Iterable<IBindingMethod<?>> methods);

	<T, R> Optional<Function<T, R>> addFieldTransformer(Function<T, R> transformer);

	<T, R> Optional<Function<T, R>> addMethodTransformer(Function<T, R> transformer);

	<T, R> Optional<Function<T, R>> removeFieldTransformer(Function<T, R> transformer);

	<T, R> Optional<Function<T, R>> removeMethodTransformer(Function<T, R> transformer);

	default boolean unbindAll() { return unbindAllFields() | unbindAllMethods(); }

	boolean unbindAllFields();

	boolean unbindAllMethods();
}
