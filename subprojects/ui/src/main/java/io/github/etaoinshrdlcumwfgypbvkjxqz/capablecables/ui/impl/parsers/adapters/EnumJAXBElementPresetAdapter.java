package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters;

import com.google.common.collect.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.CollectionType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.RelationsType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.SideType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.Tuple2Type;
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

import javax.xml.namespace.QName;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
	TUPLE_2(ImmutableTuple2.of(UIJAXBUtilities.getQName(UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory()::createTuple2), CastUtilities.<Class<ITuple2<?, ?>>>castUnchecked(ITuple2.class)),
			new IDuplexFunction.Functional<>(
					left -> JAXBUtilities.getActualValueOptional(left)
							.map(left2 -> {
								Object rightLeft = left2.getLeft();
								Object rightRight = left2.getRight();
								return ImmutableTuple2.of(
										AssertionUtilities.assertNonnull(JAXBAdapterRegistries.getFromRawAdapter(rightLeft).leftToRight(rightLeft)),
										AssertionUtilities.assertNonnull(JAXBAdapterRegistries.getFromRawAdapter(rightRight).leftToRight(rightRight))
								);
							})
							.orElse(null),
					(ITuple2<?, ?> right) ->
							UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory().createTuple2(Optional.ofNullable(right)
									.map(right2 -> {
										Object rightLeft = right.getLeft();
										Object rightRight = right.getRight();
										return Tuple2Type.of(
												AssertionUtilities.assertNonnull(JAXBAdapterRegistries.getToRawAdapter(rightLeft).rightToLeft(rightLeft)),
												AssertionUtilities.assertNonnull(JAXBAdapterRegistries.getToRawAdapter(rightRight).rightToLeft(rightRight))
										);
									}).orElse(null))
			)),
	@SuppressWarnings("UnstableApiUsage") SET(ImmutableTuple2.of(UIJAXBUtilities.getQName(UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory()::createSet), CastUtilities.<Class<Set<?>>>castUnchecked(Set.class)),
			new IDuplexFunction.Functional<>(
					left -> processCollectionType(left.getValue()).collect(ImmutableSet.toImmutableSet()),
					(Set<?> right) -> UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory().createSet(createCollectionType(right.stream()))
			)),
	@SuppressWarnings("UnstableApiUsage") LIST(ImmutableTuple2.of(UIJAXBUtilities.getQName(UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory()::createList), CastUtilities.<Class<List<?>>>castUnchecked(List.class)),
			new IDuplexFunction.Functional<>(
					left -> processCollectionType(left.getValue()).collect(ImmutableList.toImmutableList()),
					(List<?> right) -> UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory().createSet(createCollectionType(right.stream()))
			)),
	@SuppressWarnings("UnstableApiUsage") RELATIONS_SET(ImmutableTuple2.of(UIJAXBUtilities.getQName(UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory()::createRelationsSet), CastUtilities.<Class<SetMultimap<?, ?>>>castUnchecked(SetMultimap.class)),
			new IDuplexFunction.Functional<>(
					left -> processRelationsType(left.getValue()).collect(ImmutableSetMultimap.toImmutableSetMultimap(Map.Entry::getKey, Map.Entry::getValue)),
					(SetMultimap<?, ?> right) -> UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory().createRelationsSet(createRelationsType(right.entries().stream()))
			)),
	@SuppressWarnings("UnstableApiUsage") RELATIONS_LIST(ImmutableTuple2.of(UIJAXBUtilities.getQName(UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory()::createRelationsList), CastUtilities.<Class<ListMultimap<?, ?>>>castUnchecked(ListMultimap.class)),
			new IDuplexFunction.Functional<>(
					left -> processRelationsType(left.getValue()).collect(ImmutableListMultimap.toImmutableListMultimap(Map.Entry::getKey, Map.Entry::getValue)),
					(ListMultimap<?, ?> right) -> UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory().createRelationsSet(createRelationsType(right.entries().stream()))
			)),
	@SuppressWarnings("UnstableApiUsage") MAP(ImmutableTuple2.of(UIJAXBUtilities.getQName(UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory()::createMap), CastUtilities.<Class<Map<?, ?>>>castUnchecked(Map.class)),
			new IDuplexFunction.Functional<>(
					left -> processRelationsType(left.getValue()).collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue)),
					(Map<?, ?> right) -> UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory().createRelationsSet(createRelationsType(right.entrySet().stream()))
			)),
	UI_SIDE(ImmutableTuple2.of(UIJAXBUtilities.getQName(UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory()::createSide), EnumUISide.class),
			new IDuplexFunction.Functional<>(
					left -> left.getValue().toJava(),
					(EnumUISide right) -> UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory().createSide(SideType.fromJava(right))
			)),
	;

	private final ITuple2<ITuple2<? extends QName, ? extends Class<?>>, IRegistryObject<? extends IDuplexFunction<? extends JAXBElement<?>, ?>>> delegate;

	<L, R, V extends IDuplexFunction<JAXBElement<L>, R> & Serializable> EnumJAXBElementPresetAdapter(ITuple2<? extends QName, ? extends Class<R>> key, V value) {
		IRegistryObject<V> value2 = JAXBAdapterRegistries.Element.getInstance().registerChecked(key, value);
		this.delegate = ImmutableTuple2.of(key, value2);
	}

	@SuppressWarnings("EmptyMethod")
	public static void initializeClass() {}

	protected static Stream<?> processCollectionType(CollectionType left) {
		return left.getAny().stream()
				.map(leftElement -> JAXBAdapterRegistries.getFromRawAdapter(leftElement).leftToRight(leftElement));
	}

	@SuppressWarnings("UnstableApiUsage")
	protected static CollectionType createCollectionType(Stream<?> right) {
		CollectionType left = UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory().createCollectionType();
		left.getAny().addAll(right
				.map(rightElement -> JAXBAdapterRegistries.getToRawAdapter(rightElement).rightToLeft(rightElement))
				.collect(ImmutableList.toImmutableList())
		);
		return left;
	}

	@Override
	public IRegistryObject<? extends IDuplexFunction<? extends JAXBElement<?>, ?>> getRight() {
		return getDelegate().getRight();
	}

	@Override
	public Object get(int index) throws IndexOutOfBoundsException {
		return getDelegate().get(index);
	}

	protected static Stream<Map.Entry<?, ?>> processRelationsType(RelationsType left) {
		return left.getEntry().stream()
				.map(leftEntry -> {
					Object key = leftEntry.getLeft();
					Object value = leftEntry.getRight();
					return Maps.immutableEntry(
							JAXBAdapterRegistries.getFromRawAdapter(key).leftToRight(key),
							JAXBAdapterRegistries.getFromRawAdapter(value).leftToRight(value)
					);
				});
	}

	@SuppressWarnings("UnstableApiUsage")
	protected static RelationsType createRelationsType(Stream<? extends Map.Entry<?, ?>> right) {
		RelationsType left = UIJAXBUtilities.ObjectFactories.getDefaultUIObjectFactory().createRelationsType();
		left.getEntry().addAll(right
				.map(rightEntry -> {
					Object rightKey = AssertionUtilities.assertNonnull(rightEntry.getKey());
					Object rightValue = AssertionUtilities.assertNonnull(rightEntry.getValue());
					return Tuple2Type.of(
							AssertionUtilities.assertNonnull(JAXBAdapterRegistries.getToRawAdapter(rightKey).rightToLeft(rightKey)),
							AssertionUtilities.assertNonnull(JAXBAdapterRegistries.getToRawAdapter(rightValue).rightToLeft(rightValue))
					);
				})
				.collect(ImmutableList.toImmutableList())
		);
		return left;
	}

	@Override
	public ITuple2<? extends QName, ? extends Class<?>> getLeft() {
		return getDelegate().getLeft();
	}

	protected ITuple2<? extends ITuple2<? extends QName, ? extends Class<?>>, ? extends IRegistryObject<? extends IDuplexFunction<? extends JAXBElement<?>, ?>>> getDelegate() {
		return delegate;
	}
}
