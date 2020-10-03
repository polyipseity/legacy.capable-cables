package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.IBinder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.IBinding;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.NoSuchBindingTransformerException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.binding.core.bindings.IBindings;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.CacheUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.ManualLoadingCache;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IThrowingBiFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.INamespacePrefixedString;

import java.util.*;
import java.util.function.Function;

public class Binder implements IBinder {
	@SuppressWarnings("UnstableApiUsage")
	private static final MultimapBuilder.SetMultimapBuilder<Object, Object> BINDINGS_MULTI_MAP_BUILDER = MultimapBuilder
			.linkedHashKeys(CapacityUtilities.INITIAL_CAPACITY_MEDIUM)
			.linkedHashSetValues(CapacityUtilities.INITIAL_CAPACITY_TINY); // COMMENT order is important
	protected final LoadingCache<IBinding.EnumBindingType, LoadingCache<INamespacePrefixedString, IBindings<?>>> bindings;
	protected final LoadingCache<IBinding.EnumBindingType, LoadingCache<Class<?>, Cache<Class<?>, Function<?, ?>>>> transformers;

	{
		CacheBuilder<Object, Object> cb = CacheUtilities.newCacheBuilderSingleThreaded()
				.initialCapacity(CapacityUtilities.INITIAL_CAPACITY_SMALL);
		this.bindings = ManualLoadingCache.newNestedLoadingCacheCache(
				CacheUtilities.newCacheBuilderSingleThreaded()
						.initialCapacity(IBinding.EnumBindingType.values().length)
						.build(CacheLoader.from(type -> ManualLoadingCache.newNestedLoadingCache(
								cb.build(CacheLoader.from(k ->
										AssertionUtilities.assertNonnull(type).createBindings(AssertionUtilities.assertNonnull(k), () ->
												getTransformers().getUnchecked(type)))),
								e -> AssertionUtilities.assertNonnull(e.getValue()).isEmpty())
						)));
		this.transformers = ManualLoadingCache.newNestedLoadingCacheCache(
				CacheUtilities.newCacheBuilderSingleThreaded()
						.initialCapacity(IBinding.EnumBindingType.values().length)
						.build(CacheLoader.from(() ->
								ManualLoadingCache.newNestedLoadingCacheCache(
										cb.build(CacheUtilities.newCacheBuilderSingleThreadedLoader(CapacityUtilities.INITIAL_CAPACITY_SMALL))))));
	}

	protected LoadingCache<IBinding.EnumBindingType, LoadingCache<Class<?>, Cache<Class<?>, Function<?, ?>>>> getTransformers() { return transformers; }

	@Override
	public boolean bind(Iterable<? extends IBinding<?>> bindings)
			throws NoSuchBindingTransformerException {
		return sortAndTrimBindings(bindings).entrySet().stream().unordered()
				.reduce(false,
						IThrowingBiFunction.executeNow((r, e) -> {
							LoadingCache<INamespacePrefixedString, IBindings<?>> bs = getBindings().getUnchecked(AssertionUtilities.assertNonnull(e.getKey()));
							return AssertionUtilities.assertNonnull(e.getValue()).asMap().entrySet().stream().sequential() // COMMENT sequential, field binding order matters
									.reduce(false,
											IThrowingBiFunction.
													<Boolean, Map.Entry<INamespacePrefixedString, ? extends Collection<? extends IBinding<?>>>, Boolean,
															NoSuchBindingTransformerException>
															executeNow((r2, e2) ->
															bs.getUnchecked(AssertionUtilities.assertNonnull(e2.getKey()))
																	.add(CastUtilities.castUnchecked( // COMMENT should be of the right type
																			AssertionUtilities.assertNonnull(e2.getValue()))) || r2),
											Boolean::logicalOr);
						}),
						Boolean::logicalOr);
	}

	@Override
	public boolean unbind(Iterable<? extends IBinding<?>> bindings) {
		boolean ret = sortAndTrimBindings(bindings).entrySet().stream().unordered()
				.reduce(false,
						(r, e) -> {
							LoadingCache<INamespacePrefixedString, IBindings<?>> bs = getBindings().getUnchecked(AssertionUtilities.assertNonnull(e.getKey()));
							return AssertionUtilities.assertNonnull(e.getValue()).asMap().entrySet().stream().sequential() // COMMENT sequential, field binding order matters
									.reduce(false,
											(r2, e2) -> bs.getUnchecked(AssertionUtilities.assertNonnull(e2.getKey()))
													.remove(CastUtilities.castUnchecked( // COMMENT should be of the right type
															AssertionUtilities.assertNonnull(e2.getValue()))) || r2,
											Boolean::logicalOr);
						},
						Boolean::logicalOr);
		if (ret)
			getBindings().cleanUp();
		return ret;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T, R> Optional<? extends Function<T, R>> addTransformer(IBinding.EnumBindingType type, Function<T, R> transformer) {
		Optional<Class<?>>[] types = IBinder.resolveFunctionTypes(transformer);
		if (!types[0].isPresent() || !types[1].isPresent())
			return Optional.of(transformer);
		return CacheUtilities.replace(getTransformers().getUnchecked(type).getUnchecked(types[0].get()), types[1].get(), transformer)
				.map(r -> (Function<T, R>) r);
	}

	@Override
	public <T, R> Optional<? extends Function<T, R>> removeTransformer(IBinding.EnumBindingType type, Class<T> from, Class<R> to) {
		@SuppressWarnings("unchecked") Optional<? extends Function<T, R>> ret =
				CacheUtilities.remove(getTransformers().getUnchecked(type).getUnchecked(from), to)
						.map(r -> (Function<T, R>) r);
		if (ret.isPresent())
			getTransformers().cleanUp();
		return ret;
	}

	@Override
	public boolean unbindAll(Set<IBinding.EnumBindingType> types) {
		boolean ret = types.stream().unordered()
				.reduce(false,
						(r, t) -> Optional.ofNullable(getBindings().getIfPresent(t))
								.filter(bs -> !bs.asMap().isEmpty())
								.filter(bs -> {
									boolean ret2 = bs.asMap().values().stream().unordered()
											.reduce(false,
													(r2, b) -> b.removeAll() || r,
													Boolean::logicalOr);
									bs.invalidateAll();
									return ret2;
								})
								.isPresent()
								|| r,
						Boolean::logicalOr);
		if (ret)
			getBindings().cleanUp();
		return ret;
	}

	@SuppressWarnings("UnstableApiUsage")
	public static <B extends IBinding<?>> Map<IBinding.EnumBindingType, Multimap<INamespacePrefixedString, B>> sortAndTrimBindings(Iterable<B> bindings) {
		Map<IBinding.EnumBindingType, Multimap<INamespacePrefixedString, B>> ret =
				new EnumMap<>(IBinding.EnumBindingType.class);
		bindings.forEach(b ->
				b.getBindingKey()
						.ifPresent(bk ->
								ret.computeIfAbsent(b.getBindingType(), k -> BINDINGS_MULTI_MAP_BUILDER.build()).put(bk, b)));
		return ret;
	}

	protected LoadingCache<IBinding.EnumBindingType, LoadingCache<INamespacePrefixedString, IBindings<?>>> getBindings() { return bindings; }
}
