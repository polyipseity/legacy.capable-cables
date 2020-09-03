package $group__.utilities.binding.core;

import $group__.utilities.DynamicUtilities;
import net.jodah.typetools.TypeResolver;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
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

	boolean bind(Iterable<? extends IBinding<?>> bindings)
			throws BindingTransformerNotFoundException;

	boolean unbind(Iterable<? extends IBinding<?>> bindings);

	<T, R> Optional<? extends Function<T, R>> addTransformer(IBinding.EnumBindingType type, Function<T, R> transformer);

	<T, R> Optional<? extends Function<T, R>> removeTransformer(IBinding.EnumBindingType type, Class<T> from, Class<R> to);

	default boolean unbindAll() { return unbindAll(EnumSet.allOf(IBinding.EnumBindingType.class)); }

	boolean unbindAll(Set<IBinding.EnumBindingType> types);
}
