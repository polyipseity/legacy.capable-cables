package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Streams;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.CacheUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.ManualLoadingCache;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.def.IThrowingFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.def.IThrowingToIntFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.primitives.BooleanUtilities.PaddedBool;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.IIdentifier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.IBinder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.IBinding;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.NoSuchBindingTransformerException;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.binding.def.bindings.IBindings;

import java.util.*;
import java.util.function.Function;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.primitives.BooleanUtilities.PaddedBool.*;

public class DefaultBinder
		implements IBinder {
	@SuppressWarnings("UnstableApiUsage")
	private static final MultimapBuilder.SetMultimapBuilder<Object, Object> BINDINGS_MULTI_MAP_BUILDER = MultimapBuilder
			.linkedHashKeys(CapacityUtilities.getInitialCapacityMedium())
			.linkedHashSetValues(CapacityUtilities.getInitialCapacityTiny()); // COMMENT order is important
	private final LoadingCache<IBinding.EnumBindingType, LoadingCache<IIdentifier, IBindings<?>>> bindings;
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
	public boolean bind(Iterator<? extends IBinding<?>> bindings)
			throws NoSuchBindingTransformerException {
		return stripBool(
				sortAndTrimBindings(bindings).entrySet().stream().unordered()
						.flatMapToInt(IThrowingFunction.executeNow(typeEntry -> {
							LoadingCache<IIdentifier, IBindings<?>> typeBindings = getBindings().getUnchecked(AssertionUtilities.assertNonnull(typeEntry.getKey()));
							return AssertionUtilities.assertNonnull(typeEntry.getValue()).asMap().entrySet().stream() // COMMENT sequential, field binding order matters
									.mapToInt(IThrowingToIntFunction.<Map.Entry<IIdentifier, ? extends Collection<? extends IBinding<?>>>,
											NoSuchBindingTransformerException>executeNow(entry -> padBool(
											typeBindings.getUnchecked(AssertionUtilities.assertNonnull(entry.getKey()))
													.add(CastUtilities.castUnchecked( // COMMENT should be of the right type
															AssertionUtilities.assertNonnull(entry.getValue())))
									)));
						}))
						.reduce(fBool(), PaddedBool::orBool)
		);
	}

	@SuppressWarnings("UnstableApiUsage")
	public static <B extends IBinding<?>> @Immutable Map<IBinding.EnumBindingType, Multimap<IIdentifier, B>> sortAndTrimBindings(Iterator<B> bindings) {
		Map<IBinding.EnumBindingType, Multimap<IIdentifier, B>> ret =
				new EnumMap<>(IBinding.EnumBindingType.class);
		Streams.stream(bindings)
				.filter(binding -> binding.getBindingKey().isPresent())
				.forEachOrdered(binding ->
						ret.computeIfAbsent(binding.getBindingType(), k -> getBindingsMultiMapBuilder().build())
								.put(binding.getBindingKey().get(), binding)
				);
		return Maps.immutableEnumMap(ret);
	}

	protected LoadingCache<IBinding.EnumBindingType, LoadingCache<IIdentifier, IBindings<?>>> getBindings() { return bindings; }

	@SuppressWarnings("UnstableApiUsage")
	protected static MultimapBuilder.SetMultimapBuilder<Object, Object> getBindingsMultiMapBuilder() {
		return BINDINGS_MULTI_MAP_BUILDER;
	}

	@Override
	public boolean unbind(Iterator<? extends IBinding<?>> bindings) {
		boolean ret = stripBool(
				sortAndTrimBindings(bindings).entrySet().stream().unordered()
						.flatMapToInt(typeEntry -> {
							LoadingCache<IIdentifier, IBindings<?>> typeBindings = getBindings().getUnchecked(AssertionUtilities.assertNonnull(typeEntry.getKey()));
							return AssertionUtilities.assertNonnull(typeEntry.getValue()).asMap().entrySet().stream() // COMMENT sequential, field binding order matters
									.mapToInt(entry -> padBool(
											typeBindings.getUnchecked(AssertionUtilities.assertNonnull(entry.getKey()))
													.remove(CastUtilities.castUnchecked( // COMMENT should be of the right type
															AssertionUtilities.assertNonnull(entry.getValue())))
									));
						})
						.reduce(fBool(), PaddedBool::orBool)
		);
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
		boolean ret = stripBool(
				types.stream().unordered()
						.map(getBindings()::getIfPresent)
						.filter(Objects::nonNull)
						.filter(typeBindings -> !typeBindings.asMap().isEmpty())
						.mapToInt(typeBindings -> {
							int ret2 = typeBindings.asMap().values().stream().unordered()
									.mapToInt(bindings -> padBool(bindings.removeAll()))
									.reduce(fBool(), PaddedBool::orBool);
							typeBindings.invalidateAll();
							return ret2;
						})
						.reduce(fBool(), PaddedBool::orBool)
		);
		if (ret)
			getBindings().cleanUp();
		return ret;
	}

	protected LoadingCache<IBinding.EnumBindingType, LoadingCache<Class<?>, Cache<Class<?>, Function<@Nonnull ?, @Nonnull ?>>>> getTransformers() { return transformers; }
}
