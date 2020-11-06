package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.CacheUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.ManualLoadingCache;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IThrowingFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.INamespacePrefixedString;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.IBinding;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.NoSuchBindingTransformerException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.core.bindings.IBindings;

import java.util.*;
import java.util.function.Function;

public class DefaultBinder
		implements IBinder {
	@SuppressWarnings("UnstableApiUsage")
	private static final MultimapBuilder.SetMultimapBuilder<Object, Object> BINDINGS_MULTI_MAP_BUILDER = MultimapBuilder
			.linkedHashKeys(CapacityUtilities.getInitialCapacityMedium())
			.linkedHashSetValues(CapacityUtilities.getInitialCapacityTiny()); // COMMENT order is important
	private final LoadingCache<IBinding.EnumBindingType, LoadingCache<INamespacePrefixedString, IBindings<?>>> bindings;
	private final LoadingCache<IBinding.EnumBindingType, LoadingCache<Class<?>, Cache<Class<?>, Function<@Nonnull ?, @Nonnull ?>>>> transformers;

	{
		CacheBuilder<Object, Object> cb = CacheUtilities.newCacheBuilderSingleThreaded()
				.initialCapacity(CapacityUtilities.getInitialCapacitySmall());
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
										cb.build(CacheUtilities.newCacheBuilderSingleThreadedLoader(CapacityUtilities.getInitialCapacitySmall()))))));
	}

	@Override
	public boolean bind(Iterable<? extends IBinding<?>> bindings)
			throws NoSuchBindingTransformerException {
		return sortAndTrimBindings(bindings).entrySet().stream().unordered()
				.map(IThrowingFunction.executeNow(typeEntry -> {
					assert typeEntry != null;
					LoadingCache<INamespacePrefixedString, IBindings<?>> typeBindings = getBindings().getUnchecked(AssertionUtilities.assertNonnull(typeEntry.getKey()));
					return AssertionUtilities.assertNonnull(typeEntry.getValue()).asMap().entrySet().stream() // COMMENT sequential, field binding order matters
							.map(IThrowingFunction.<Map.Entry<INamespacePrefixedString, ? extends Collection<? extends IBinding<?>>>, Boolean,
									NoSuchBindingTransformerException>executeNow(entry -> {
								assert entry != null;
								return typeBindings.getUnchecked(AssertionUtilities.assertNonnull(entry.getKey()))
										.add(CastUtilities.castUnchecked( // COMMENT should be of the right type
												AssertionUtilities.assertNonnull(entry.getValue())));
							}))
							.reduce(false, Boolean::logicalOr);
				}))
				.reduce(false, Boolean::logicalOr);
	}

	@SuppressWarnings("UnstableApiUsage")
	public static <B extends IBinding<?>> Map<IBinding.EnumBindingType, Multimap<INamespacePrefixedString, B>> sortAndTrimBindings(Iterable<B> bindings) {
		Map<IBinding.EnumBindingType, Multimap<INamespacePrefixedString, B>> ret =
				new EnumMap<>(IBinding.EnumBindingType.class);
		bindings.forEach(b ->
				b.getBindingKey()
						.ifPresent(bk ->
								ret.computeIfAbsent(b.getBindingType(), k -> getBindingsMultiMapBuilder().build()).put(bk, b)));
		return ret;
	}

	@SuppressWarnings("UnstableApiUsage")
	protected static MultimapBuilder.SetMultimapBuilder<Object, Object> getBindingsMultiMapBuilder() {
		return BINDINGS_MULTI_MAP_BUILDER;
	}

	@Override
	public boolean unbind(Iterable<? extends IBinding<?>> bindings) {
		boolean ret = sortAndTrimBindings(bindings).entrySet().stream().unordered()
				.map(typeEntry -> {
					LoadingCache<INamespacePrefixedString, IBindings<?>> typeBindings = getBindings().getUnchecked(AssertionUtilities.assertNonnull(typeEntry.getKey()));
					return AssertionUtilities.assertNonnull(typeEntry.getValue()).asMap().entrySet().stream() // COMMENT sequential, field binding order matters
							.map(entry ->
									typeBindings.getUnchecked(AssertionUtilities.assertNonnull(entry.getKey()))
											.remove(CastUtilities.castUnchecked( // COMMENT should be of the right type
													AssertionUtilities.assertNonnull(entry.getValue()))))
							.reduce(false, Boolean::logicalOr);
				})
				.reduce(false, Boolean::logicalOr);
		if (ret)
			getBindings().cleanUp();
		return ret;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T, R> Optional<? extends Function<@Nonnull T, @Nonnull R>> addTransformer(IBinding.EnumBindingType type, Function<@Nonnull T, @Nonnull R> transformer) {
		Optional<Class<?>>[] types = IBinder.resolveFunctionTypes(transformer);
		if (!types[0].isPresent() || !types[1].isPresent())
			return Optional.of(transformer);
		return CacheUtilities.replace(getTransformers().getUnchecked(type).getUnchecked(types[0].get()), types[1].get(), transformer)
				.map(r -> (Function<@Nonnull T, @Nonnull R>) r);
	}

	@Override
	public <T, R> Optional<? extends Function<@Nonnull T, @Nonnull R>> removeTransformer(IBinding.EnumBindingType type, Class<T> from, Class<R> to) {
		@SuppressWarnings("unchecked") Optional<? extends Function<@Nonnull T, @Nonnull R>> ret =
				CacheUtilities.remove(getTransformers().getUnchecked(type).getUnchecked(from), to)
						.map(r -> (Function<@Nonnull T, @Nonnull R>) r);
		if (ret.isPresent())
			getTransformers().cleanUp();
		return ret;
	}

	@Override
	public boolean unbindAll(Set<IBinding.EnumBindingType> types) {
		boolean ret = types.stream().unordered()
				.map(getBindings()::getIfPresent)
				.filter(Objects::nonNull)
				.filter(typeBindings -> !typeBindings.asMap().isEmpty())
				.map(typeBindings -> {
					boolean ret2 = typeBindings.asMap().values().stream().unordered()
							.map(IBindings::removeAll)
							.reduce(false, Boolean::logicalOr);
					typeBindings.invalidateAll();
					return ret2;
				})
				.reduce(false, Boolean::logicalOr);
		if (ret)
			getBindings().cleanUp();
		return ret;
	}

	protected LoadingCache<IBinding.EnumBindingType, LoadingCache<Class<?>, Cache<Class<?>, Function<@Nonnull ?, @Nonnull ?>>>> getTransformers() { return transformers; }

	protected LoadingCache<IBinding.EnumBindingType, LoadingCache<INamespacePrefixedString, IBindings<?>>> getBindings() { return bindings; }
}
