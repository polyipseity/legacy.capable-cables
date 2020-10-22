package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters;

import com.google.common.collect.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.components.CollectionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.components.RelationsType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.JAXBAdapterRegistries;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.UIJAXBUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.JAXBUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IDuplexFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.ITuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples.ImmutableTuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.RegistryObject;
import jakarta.xml.bind.JAXBElement;

import javax.annotation.Nullable;
import javax.xml.namespace.QName;
import java.awt.*;
import java.io.Serializable;
import java.util.List;
import java.util.*;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public enum EnumJAXBElementPresetAdapter
		implements ITuple2<ITuple2<? extends QName, ? extends Class<?>>, RegistryObject<? extends IDuplexFunction<? extends JAXBElement<?>, ?>>> {
	BOOLEAN(ImmutableTuple2.of(UIJAXBUtilities.getQName(UIJAXBUtilities.ObjectFactories.getDefaultComponentObjectFactory()::createBoolean), Boolean.class),
			new IDuplexFunction.Functional<>(JAXBUtilities::getActualValue, UIJAXBUtilities.ObjectFactories.getDefaultComponentObjectFactory()::createBoolean)),
	BYTE(ImmutableTuple2.of(UIJAXBUtilities.getQName(UIJAXBUtilities.ObjectFactories.getDefaultComponentObjectFactory()::createByte), Byte.class),
			new IDuplexFunction.Functional<>(JAXBUtilities::getActualValue, UIJAXBUtilities.ObjectFactories.getDefaultComponentObjectFactory()::createByte)),
	SHORT(ImmutableTuple2.of(UIJAXBUtilities.getQName(UIJAXBUtilities.ObjectFactories.getDefaultComponentObjectFactory()::createShort), Short.class),
			new IDuplexFunction.Functional<>(JAXBUtilities::getActualValue, UIJAXBUtilities.ObjectFactories.getDefaultComponentObjectFactory()::createShort)),
	INT(ImmutableTuple2.of(UIJAXBUtilities.getQName(UIJAXBUtilities.ObjectFactories.getDefaultComponentObjectFactory()::createInt), Integer.class),
			new IDuplexFunction.Functional<>(JAXBUtilities::getActualValue, UIJAXBUtilities.ObjectFactories.getDefaultComponentObjectFactory()::createInt)),
	LONG(ImmutableTuple2.of(UIJAXBUtilities.getQName(UIJAXBUtilities.ObjectFactories.getDefaultComponentObjectFactory()::createLong), Long.class),
			new IDuplexFunction.Functional<>(JAXBUtilities::getActualValue, UIJAXBUtilities.ObjectFactories.getDefaultComponentObjectFactory()::createLong)),
	FLOAT(ImmutableTuple2.of(UIJAXBUtilities.getQName(UIJAXBUtilities.ObjectFactories.getDefaultComponentObjectFactory()::createFloat), Float.class),
			new IDuplexFunction.Functional<>(JAXBUtilities::getActualValue, UIJAXBUtilities.ObjectFactories.getDefaultComponentObjectFactory()::createFloat)),
	DOUBLE(ImmutableTuple2.of(UIJAXBUtilities.getQName(UIJAXBUtilities.ObjectFactories.getDefaultComponentObjectFactory()::createDouble), Double.class),
			new IDuplexFunction.Functional<>(JAXBUtilities::getActualValue, UIJAXBUtilities.ObjectFactories.getDefaultComponentObjectFactory()::createDouble)),
	STRING(ImmutableTuple2.of(UIJAXBUtilities.getQName(UIJAXBUtilities.ObjectFactories.getDefaultComponentObjectFactory()::createString), String.class),
			new IDuplexFunction.Functional<>(JAXBUtilities::getActualValue, UIJAXBUtilities.ObjectFactories.getDefaultComponentObjectFactory()::createString)),
	COLOR(ImmutableTuple2.of(UIJAXBUtilities.<io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.components.Color>getQName(UIJAXBUtilities.ObjectFactories.getDefaultComponentObjectFactory()::createColor), Color.class),
			new IDuplexFunction.Functional<>(
					left -> JAXBUtilities.getActualValueOptional(left)
							.map(io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.components.Color::toJava)
							.orElse(null),
					(@Nullable Color right) ->
							UIJAXBUtilities.ObjectFactories.getDefaultComponentObjectFactory()
									.createColor(io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.components.Color.fromJava(right)))),
	@SuppressWarnings("UnstableApiUsage") SET(ImmutableTuple2.of(UIJAXBUtilities.getQName(UIJAXBUtilities.ObjectFactories.getDefaultComponentObjectFactory()::createSet), CastUtilities.<Class<Set<?>>>castUnchecked(Set.class)),
			new IDuplexFunction.Functional<>(
					left -> processCollectionType(JAXBUtilities.getActualValueOptional(left).orElse(null))
							.map(leftStream -> leftStream.collect(ImmutableSet.toImmutableSet()))
							.orElse(null),
					(@Nullable Set<?> right) ->
							UIJAXBUtilities.ObjectFactories.getDefaultComponentObjectFactory()
									.createSet(createCollectionType(
											Optional.ofNullable(right)
													.map(Collection::stream)
													.orElse(null)
									).orElse(null))
			)),
	@SuppressWarnings("UnstableApiUsage") LIST(ImmutableTuple2.of(UIJAXBUtilities.getQName(UIJAXBUtilities.ObjectFactories.getDefaultComponentObjectFactory()::createList), CastUtilities.<Class<List<?>>>castUnchecked(List.class)),
			new IDuplexFunction.Functional<>(
					left -> processCollectionType(JAXBUtilities.getActualValueOptional(left).orElse(null))
							.map(leftStream -> leftStream.collect(ImmutableList.toImmutableList()))
							.orElse(null),
					(@Nullable List<?> right) ->
							UIJAXBUtilities.ObjectFactories.getDefaultComponentObjectFactory()
									.createSet(createCollectionType(
											Optional.ofNullable(right)
													.map(Collection::stream)
													.orElse(null)
									).orElse(null))
			)),
	@SuppressWarnings("UnstableApiUsage") RELATIONS_SET(ImmutableTuple2.of(UIJAXBUtilities.getQName(UIJAXBUtilities.ObjectFactories.getDefaultComponentObjectFactory()::createRelationsSet), CastUtilities.<Class<SetMultimap<?, ?>>>castUnchecked(SetMultimap.class)),
			new IDuplexFunction.Functional<>(
					left -> processRelationsType(JAXBUtilities.getActualValueOptional(left).orElse(null))
							.map(leftStream -> leftStream.collect(ImmutableSetMultimap.toImmutableSetMultimap(Map.Entry::getKey, Map.Entry::getValue)))
							.orElse(null),
					(@Nullable SetMultimap<?, ?> right) ->
							UIJAXBUtilities.ObjectFactories.getDefaultComponentObjectFactory()
									.createRelationsSet(createRelationsType(
											Optional.ofNullable(right)
													.map(SetMultimap::entries)
													.map(Collection::stream)
													.orElse(null)
									).orElse(null))
			)),
	@SuppressWarnings("UnstableApiUsage") RELATIONS_LIST(ImmutableTuple2.of(UIJAXBUtilities.getQName(UIJAXBUtilities.ObjectFactories.getDefaultComponentObjectFactory()::createRelationsSet), CastUtilities.<Class<ListMultimap<?, ?>>>castUnchecked(ListMultimap.class)),
			new IDuplexFunction.Functional<>(
					left -> processRelationsType(JAXBUtilities.getActualValueOptional(left).orElse(null))
							.map(leftStream -> leftStream.collect(ImmutableListMultimap.toImmutableListMultimap(Map.Entry::getKey, Map.Entry::getValue)))
							.orElse(null),
					(@Nullable ListMultimap<?, ?> right) ->
							UIJAXBUtilities.ObjectFactories.getDefaultComponentObjectFactory()
									.createRelationsSet(createRelationsType(
											Optional.ofNullable(right)
													.map(ListMultimap::entries)
													.map(Collection::stream)
													.orElse(null)
									).orElse(null))
			)),
	@SuppressWarnings("UnstableApiUsage") MAP(ImmutableTuple2.of(UIJAXBUtilities.getQName(UIJAXBUtilities.ObjectFactories.getDefaultComponentObjectFactory()::createMap), CastUtilities.<Class<Map<?, ?>>>castUnchecked(Map.class)),
			new IDuplexFunction.Functional<>(
					left -> processRelationsType(JAXBUtilities.getActualValueOptional(left).orElse(null))
							.map(leftStream -> leftStream.collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue)))
							.orElse(null),
					(@Nullable Map<?, ?> right) ->
							UIJAXBUtilities.ObjectFactories.getDefaultComponentObjectFactory()
									.createRelationsSet(createRelationsType(
											Optional.ofNullable(right)
													.map(Map::entrySet)
													.map(Collection::stream)
													.orElse(null)
									).orElse(null))
			)),
	;

	private final ITuple2<ITuple2<? extends QName, ? extends Class<?>>, RegistryObject<? extends IDuplexFunction<? extends JAXBElement<?>, ?>>> delegate;

	<L, R, V extends IDuplexFunction<JAXBElement<L>, R> & Serializable> EnumJAXBElementPresetAdapter(ITuple2<? extends QName, ? extends Class<R>> key, V value) {
		RegistryObject<V> value2 = JAXBAdapterRegistries.Element.getInstance().registerChecked(ITuple2.upcast(key), value);
		this.delegate = ImmutableTuple2.of(key, value2);
	}

	@SuppressWarnings("EmptyMethod")
	public static void initializeClass() {}

	protected static Optional<Stream<?>> processCollectionType(@Nullable CollectionType left) {
		return Optional.ofNullable(left)
				.map(CollectionType::getAny)
				.map(Collection::stream)
				.map(leftStream -> leftStream.map(leftElement -> JAXBAdapterRegistries.getFromRawAdapter(leftElement).leftToRight(leftElement)));
	}

	@SuppressWarnings("UnstableApiUsage")
	protected static Optional<CollectionType> createCollectionType(@Nullable Stream<?> right) {
		return Optional.ofNullable(right)
				.map(rightStream -> rightStream.map(rightElement -> JAXBAdapterRegistries.getToRawAdapter(rightElement).rightToLeft(rightElement)))
				.map(intermediateStream -> intermediateStream.collect(ImmutableList.toImmutableList()))
				.map(intermediate -> {
					io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.components.CollectionType left =
							UIJAXBUtilities.ObjectFactories.getDefaultComponentObjectFactory().createCollectionType();
					left.getAny().addAll(intermediate);
					return left;
				});
	}

	@Override
	public RegistryObject<? extends IDuplexFunction<? extends JAXBElement<?>, ?>> getRight() {
		return getDelegate().getRight();
	}

	@Override
	public Object get(int index) throws IndexOutOfBoundsException {
		return getDelegate().get(index);
	}

	protected static Optional<Stream<Map.Entry<?, ?>>> processRelationsType(@Nullable RelationsType left) {
		return Optional.ofNullable(left)
				.map(RelationsType::getEntry)
				.map(Collection::stream)
				.map(leftStream -> leftStream.map(leftEntry -> {
					Object key = leftEntry.getKey().getAny();
					Object value = leftEntry.getValue().getAny();
					return Maps.immutableEntry(
							JAXBAdapterRegistries.getFromRawAdapter(key).leftToRight(key),
							JAXBAdapterRegistries.getFromRawAdapter(value).leftToRight(value)
					);
				}));
	}

	@SuppressWarnings("UnstableApiUsage")
	protected static Optional<RelationsType> createRelationsType(@Nullable Stream<? extends Map.Entry<?, ?>> right) {
		return Optional.ofNullable(right)
				.map(rightStream -> rightStream.map(rightEntry -> {
					Object rightKey = AssertionUtilities.assertNonnull(rightEntry.getKey());
					Object rightValue = AssertionUtilities.assertNonnull(rightEntry.getValue());

					RelationsType.Entry.Key leftKey =
							UIJAXBUtilities.ObjectFactories.getDefaultComponentObjectFactory().createRelationsTypeEntryKey();
					RelationsType.Entry.Value leftValue =
							UIJAXBUtilities.ObjectFactories.getDefaultComponentObjectFactory().createRelationsTypeEntryValue();

					RelationsType.Entry leftEntry =
							UIJAXBUtilities.ObjectFactories.getDefaultComponentObjectFactory().createRelationsTypeEntry();

					leftKey.setAny(JAXBAdapterRegistries.getToRawAdapter(rightKey).rightToLeft(rightKey));
					leftValue.setAny(JAXBAdapterRegistries.getToRawAdapter(rightValue).rightToLeft(rightValue));

					leftEntry.setKey(leftKey);
					leftEntry.setValue(leftValue);

					return leftEntry;
				}))
				.map(intermediateStream -> intermediateStream.collect(ImmutableList.toImmutableList()))
				.map(intermediate -> {
					io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.components.RelationsType left =
							UIJAXBUtilities.ObjectFactories.getDefaultComponentObjectFactory().createRelationsType();
					left.getEntry().addAll(intermediate);
					return left;
				});
	}

	@Override
	public ITuple2<? extends QName, ? extends Class<?>> getLeft() {
		return getDelegate().getLeft();
	}

	protected ITuple2<? extends ITuple2<? extends QName, ? extends Class<?>>, ? extends RegistryObject<? extends IDuplexFunction<? extends JAXBElement<?>, ?>>> getDelegate() {
		return delegate;
	}
}
