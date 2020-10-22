package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters;

import com.google.common.collect.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.JAXBAdapterRegistries;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.UIJAXBUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUISide;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.JAXBUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IDuplexFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.ITuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples.ImmutableTuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.core.IRegistryObject;
import jakarta.xml.bind.JAXBElement;

import javax.annotation.Nullable;
import javax.xml.namespace.QName;
import java.awt.Color;
import java.io.Serializable;
import java.util.List;
import java.util.*;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public enum EnumJAXBElementPresetAdapter
		implements ITuple2<ITuple2<? extends QName, ? extends Class<?>>, IRegistryObject<? extends IDuplexFunction<? extends JAXBElement<?>, ?>>> {
	BOOLEAN(ImmutableTuple2.of(UIJAXBUtilities.getQName(UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory()::createBoolean), Boolean.class),
			new IDuplexFunction.Functional<>(JAXBUtilities::getActualValue, UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory()::createBoolean)),
	BYTE(ImmutableTuple2.of(UIJAXBUtilities.getQName(UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory()::createByte), Byte.class),
			new IDuplexFunction.Functional<>(JAXBUtilities::getActualValue, UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory()::createByte)),
	SHORT(ImmutableTuple2.of(UIJAXBUtilities.getQName(UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory()::createShort), Short.class),
			new IDuplexFunction.Functional<>(JAXBUtilities::getActualValue, UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory()::createShort)),
	INT(ImmutableTuple2.of(UIJAXBUtilities.getQName(UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory()::createInt), Integer.class),
			new IDuplexFunction.Functional<>(JAXBUtilities::getActualValue, UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory()::createInt)),
	LONG(ImmutableTuple2.of(UIJAXBUtilities.getQName(UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory()::createLong), Long.class),
			new IDuplexFunction.Functional<>(JAXBUtilities::getActualValue, UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory()::createLong)),
	FLOAT(ImmutableTuple2.of(UIJAXBUtilities.getQName(UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory()::createFloat), Float.class),
			new IDuplexFunction.Functional<>(JAXBUtilities::getActualValue, UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory()::createFloat)),
	DOUBLE(ImmutableTuple2.of(UIJAXBUtilities.getQName(UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory()::createDouble), Double.class),
			new IDuplexFunction.Functional<>(JAXBUtilities::getActualValue, UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory()::createDouble)),
	STRING(ImmutableTuple2.of(UIJAXBUtilities.getQName(UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory()::createString), String.class),
			new IDuplexFunction.Functional<>(JAXBUtilities::getActualValue, UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory()::createString)),
	COLOR(ImmutableTuple2.of(UIJAXBUtilities.<io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.Color>getQName(UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory()::createColor), Color.class),
			new IDuplexFunction.Functional<>(
					left -> JAXBUtilities.getActualValueOptional(left)
							.map(io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.Color::toJava)
							.orElse(null),
					(@Nullable Color right) ->
							UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory()
									.createColor(io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.Color.fromJava(right)))),
	UI_SIDE(ImmutableTuple2.of(UIJAXBUtilities.getQName(UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory()::createSide), EnumUISide.class),
			new IDuplexFunction.Functional<>(
					left -> JAXBUtilities.getActualValueOptional(left)
							.orElseThrow(AssertionError::new)
							.toJava(),
					(@Nullable EnumUISide right) ->
							UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory()
									.createSide(SideType.fromJava(AssertionUtilities.assertNonnull(right)))
			)),
	@SuppressWarnings("UnstableApiUsage") SET(ImmutableTuple2.of(UIJAXBUtilities.getQName(UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory()::createSet), CastUtilities.<Class<Set<?>>>castUnchecked(Set.class)),
			new IDuplexFunction.Functional<>(
					left -> processCollectionType(JAXBUtilities.getActualValueOptional(left).orElse(null))
							.map(leftStream -> leftStream.collect(ImmutableSet.toImmutableSet()))
							.orElse(null),
					(@Nullable Set<?> right) ->
							UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory()
									.createSet(createCollectionType(
											Optional.ofNullable(right)
													.map(Collection::stream)
													.orElse(null)
									).orElse(null))
			)),
	@SuppressWarnings("UnstableApiUsage") LIST(ImmutableTuple2.of(UIJAXBUtilities.getQName(UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory()::createList), CastUtilities.<Class<List<?>>>castUnchecked(List.class)),
			new IDuplexFunction.Functional<>(
					left -> processCollectionType(JAXBUtilities.getActualValueOptional(left).orElse(null))
							.map(leftStream -> leftStream.collect(ImmutableList.toImmutableList()))
							.orElse(null),
					(@Nullable List<?> right) ->
							UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory()
									.createSet(createCollectionType(
											Optional.ofNullable(right)
													.map(Collection::stream)
													.orElse(null)
									).orElse(null))
			)),
	@SuppressWarnings("UnstableApiUsage") RELATIONS_SET(ImmutableTuple2.of(UIJAXBUtilities.getQName(UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory()::createRelationsSet), CastUtilities.<Class<SetMultimap<?, ?>>>castUnchecked(SetMultimap.class)),
			new IDuplexFunction.Functional<>(
					left -> processRelationsType(JAXBUtilities.getActualValueOptional(left).orElse(null))
							.map(leftStream -> leftStream.collect(ImmutableSetMultimap.toImmutableSetMultimap(Map.Entry::getKey, Map.Entry::getValue)))
							.orElse(null),
					(@Nullable SetMultimap<?, ?> right) ->
							UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory()
									.createRelationsSet(createRelationsType(
											Optional.ofNullable(right)
													.map(SetMultimap::entries)
													.map(Collection::stream)
													.orElse(null)
									).orElse(null))
			)),
	@SuppressWarnings("UnstableApiUsage") RELATIONS_LIST(ImmutableTuple2.of(UIJAXBUtilities.getQName(UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory()::createRelationsList), CastUtilities.<Class<ListMultimap<?, ?>>>castUnchecked(ListMultimap.class)),
			new IDuplexFunction.Functional<>(
					left -> processRelationsType(JAXBUtilities.getActualValueOptional(left).orElse(null))
							.map(leftStream -> leftStream.collect(ImmutableListMultimap.toImmutableListMultimap(Map.Entry::getKey, Map.Entry::getValue)))
							.orElse(null),
					(@Nullable ListMultimap<?, ?> right) ->
							UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory()
									.createRelationsSet(createRelationsType(
											Optional.ofNullable(right)
													.map(ListMultimap::entries)
													.map(Collection::stream)
													.orElse(null)
									).orElse(null))
			)),
	@SuppressWarnings("UnstableApiUsage") MAP(ImmutableTuple2.of(UIJAXBUtilities.getQName(UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory()::createMap), CastUtilities.<Class<Map<?, ?>>>castUnchecked(Map.class)),
			new IDuplexFunction.Functional<>(
					left -> processRelationsType(JAXBUtilities.getActualValueOptional(left).orElse(null))
							.map(leftStream -> leftStream.collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue)))
							.orElse(null),
					(@Nullable Map<?, ?> right) ->
							UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory()
									.createRelationsSet(createRelationsType(
											Optional.ofNullable(right)
													.map(Map::entrySet)
													.map(Collection::stream)
													.orElse(null)
									).orElse(null))
			)),
	;

	private final ITuple2<ITuple2<? extends QName, ? extends Class<?>>, IRegistryObject<? extends IDuplexFunction<? extends JAXBElement<?>, ?>>> delegate;

	<L, R, V extends IDuplexFunction<JAXBElement<L>, R> & Serializable> EnumJAXBElementPresetAdapter(ITuple2<? extends QName, ? extends Class<R>> key, V value) {
		IRegistryObject<V> value2 = JAXBAdapterRegistries.Element.getInstance().registerChecked(key, value);
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
					io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.CollectionType left =
							UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory().createCollectionType();
					left.getAny().addAll(intermediate);
					return left;
				});
	}

	@Override
	public IRegistryObject<? extends IDuplexFunction<? extends JAXBElement<?>, ?>> getRight() {
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
					Object key = AssertionUtilities.assertNonnull(leftEntry.getAny().get(0)).getAny();
					Object value = AssertionUtilities.assertNonnull(leftEntry.getAny().get(1)).getAny();
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

					AnyType leftKey =
							UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory().createAnyType();
					AnyType leftValue =
							UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory().createAnyType();

					Tuple2Type leftEntry =
							UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory().createTuple2Type();

					leftKey.setAny(JAXBAdapterRegistries.getToRawAdapter(rightKey).rightToLeft(rightKey));
					leftValue.setAny(JAXBAdapterRegistries.getToRawAdapter(rightValue).rightToLeft(rightValue));

					leftEntry.getAny().set(0, leftKey);
					leftEntry.getAny().set(1, leftValue);

					return leftEntry;
				}))
				.map(intermediateStream -> intermediateStream.collect(ImmutableList.toImmutableList()))
				.map(intermediate -> {
					io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.RelationsType left =
							UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory().createRelationsType();
					left.getEntry().addAll(intermediate);
					return left;
				});
	}

	@Override
	public ITuple2<? extends QName, ? extends Class<?>> getLeft() {
		return getDelegate().getLeft();
	}

	protected ITuple2<? extends ITuple2<? extends QName, ? extends Class<?>>, ? extends IRegistryObject<? extends IDuplexFunction<? extends JAXBElement<?>, ?>>> getDelegate() {
		return delegate;
	}
}
