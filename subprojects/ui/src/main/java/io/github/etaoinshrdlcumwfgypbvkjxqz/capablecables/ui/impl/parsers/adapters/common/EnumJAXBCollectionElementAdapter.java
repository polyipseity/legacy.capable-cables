package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.common;

import com.google.common.collect.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.CollectionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.RelationsType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.Tuple2Type;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.adapters.IJAXBAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.adapters.IJAXBElementAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.adapters.registries.IJAXBAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.JAXBUIUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.JAXBUIUtilities.ObjectFactories;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.JAXBFunctionalElementAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.tuples.ITuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples.ImmutableTuple2;
import org.jetbrains.annotations.NonNls;

import javax.xml.namespace.QName;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public enum EnumJAXBCollectionElementAdapter {
	@SuppressWarnings("UnstableApiUsage") SET(ImmutableTuple2.of(JAXBUIUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createSet), CastUtilities.castUnchecked(Set.class)),
			new JAXBFunctionalElementAdapter<CollectionType, Set<?>>(
					(context, left) -> processCollectionType(context, left.getValue()).collect(ImmutableSet.toImmutableSet()),
					(context, right) -> ObjectFactories.getDefaultUIObjectFactory().createSet(createCollectionType(context, right.stream()))
			)),
	@SuppressWarnings("UnstableApiUsage") LIST(ImmutableTuple2.of(JAXBUIUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createList), CastUtilities.castUnchecked(List.class)),
			new JAXBFunctionalElementAdapter<CollectionType, List<?>>(
					(context, left) -> processCollectionType(context, left.getValue()).collect(ImmutableList.toImmutableList()),
					(context, right) -> ObjectFactories.getDefaultUIObjectFactory().createSet(createCollectionType(context, right.stream()))
			)),
	@SuppressWarnings("UnstableApiUsage") RELATIONS_SET(ImmutableTuple2.of(JAXBUIUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createRelationsSet), CastUtilities.castUnchecked(SetMultimap.class)),
			new JAXBFunctionalElementAdapter<RelationsType, SetMultimap<?, ?>>(
					(context, left) -> processRelationsType(context, left.getValue()).collect(ImmutableSetMultimap.toImmutableSetMultimap(Map.Entry::getKey, Map.Entry::getValue)),
					(context, right) -> ObjectFactories.getDefaultUIObjectFactory().createRelationsSet(createRelationsType(context, right.entries().stream()))
			)),
	@SuppressWarnings("UnstableApiUsage") RELATIONS_LIST(ImmutableTuple2.of(JAXBUIUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createRelationsList), CastUtilities.castUnchecked(ListMultimap.class)),
			new JAXBFunctionalElementAdapter<RelationsType, ListMultimap<?, ?>>(
					(context, left) -> processRelationsType(context, left.getValue()).collect(ImmutableListMultimap.toImmutableListMultimap(Map.Entry::getKey, Map.Entry::getValue)),
					(context, right) -> ObjectFactories.getDefaultUIObjectFactory().createRelationsSet(createRelationsType(context, right.entries().stream()))
			)),
	@SuppressWarnings("UnstableApiUsage") MAP(ImmutableTuple2.of(JAXBUIUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createMap), CastUtilities.castUnchecked(Map.class)),
			new JAXBFunctionalElementAdapter<RelationsType, Map<?, ?>>(
					(context, left) -> processRelationsType(context, left.getValue()).collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue)),
					(context, right) -> ObjectFactories.getDefaultUIObjectFactory().createRelationsSet(createRelationsType(context, right.entrySet().stream()))
			)),
	;

	@NonNls
	private static final ImmutableMap<String, Function<@Nonnull EnumJAXBCollectionElementAdapter, @Nullable ?>> OBJECT_VARIABLE_MAP =
			ImmutableMap.<String, Function<@Nonnull EnumJAXBCollectionElementAdapter, @Nullable ?>>builder()
					.put("key", EnumJAXBCollectionElementAdapter::getKey)
					.put("value", EnumJAXBCollectionElementAdapter::getValue)
					.build();
	private final ITuple2<? extends QName, ? extends Class<?>> key;
	private final IJAXBElementAdapter<?, ?> value;

	<L, R, V extends IJAXBElementAdapter<L, R>> EnumJAXBCollectionElementAdapter(ITuple2<? extends QName, ? extends Class<R>> key, V value) {
		this.key = key;
		this.value = value;
	}

	public static Stream<?> processCollectionType(IJAXBAdapterContext context, CollectionType left) {
		return left.getAny().stream()
				.map(leftElement -> IJAXBAdapterRegistry.adaptFromJAXB(context, leftElement));
	}

	@SuppressWarnings("UnstableApiUsage")
	public static CollectionType createCollectionType(IJAXBAdapterContext context, Stream<?> right) {
		CollectionType left = ObjectFactories.getDefaultUIObjectFactory().createCollectionType();
		left.getAny().addAll(right
				.map(rightElement -> IJAXBAdapterRegistry.adaptToJAXB(context, rightElement))
				.collect(ImmutableList.toImmutableList())
		);
		return left;
	}

	public static Stream<Map.Entry<?, ?>> processRelationsType(IJAXBAdapterContext context, RelationsType left) {
		return left.getEntry().stream()
				.map(leftEntry -> {
					Object key = leftEntry.getLeft();
					Object value = leftEntry.getRight();
					return Maps.immutableEntry(IJAXBAdapterRegistry.adaptFromJAXB(context, key),
							IJAXBAdapterRegistry.adaptFromJAXB(context, value));
				});
	}

	@SuppressWarnings("UnstableApiUsage")
	public static RelationsType createRelationsType(IJAXBAdapterContext context, Stream<? extends Map.Entry<?, ?>> right) {
		RelationsType left = ObjectFactories.getDefaultUIObjectFactory().createRelationsType();
		left.getEntry().addAll(right
				.map(rightEntry -> {
					Object rightKey = AssertionUtilities.assertNonnull(rightEntry.getKey());
					Object rightValue = AssertionUtilities.assertNonnull(rightEntry.getValue());
					return Tuple2Type.of(IJAXBAdapterRegistry.adaptToJAXB(context, rightKey),
							IJAXBAdapterRegistry.adaptToJAXB(context, rightValue));
				})
				.collect(ImmutableList.toImmutableList())
		);
		return left;
	}

	public static void registerAll(IJAXBAdapterRegistry registry) {
		Arrays.stream(values()).unordered()
				.forEach(adapter -> adapter.register(registry));
	}

	@SuppressWarnings("deprecation")
	public void register(IJAXBAdapterRegistry registry) {
		registry.getElementRegistry().register(getKey(), getValue()); // COMMENT use deprecated, checked offers no benefits
	}

	public ITuple2<? extends QName, ? extends Class<?>> getKey() {
		return key;
	}

	public IJAXBElementAdapter<?, ?> getValue() {
		return value;
	}

	@Override
	public String toString() {
		return ObjectUtilities.toStringImpl(this, getObjectVariableMap());
	}

	public static ImmutableMap<String, Function<@Nonnull EnumJAXBCollectionElementAdapter, @Nullable ?>> getObjectVariableMap() { return OBJECT_VARIABLE_MAP; }
}
