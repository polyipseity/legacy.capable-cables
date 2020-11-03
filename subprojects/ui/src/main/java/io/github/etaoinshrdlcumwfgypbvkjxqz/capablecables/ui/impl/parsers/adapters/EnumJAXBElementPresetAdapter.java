package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters;

import com.google.common.collect.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IFractionalMetricsRenderingHintWrapper;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.ITextAntiAliasRenderingHintWrapper;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.JAXBAdapterRegistries;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.UIJAXBUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.UIJAXBUtilities.ObjectFactories;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUISide;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.JAXBUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.InvokeUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IDuplexFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.ITuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples.ImmutableTuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.core.IRegistryObject;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;
import jakarta.xml.bind.JAXBElement;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.namespace.QName;
import java.awt.font.TextAttribute;
import java.io.Serializable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.text.AttributedCharacterIterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public enum EnumJAXBElementPresetAdapter
		implements ITuple2<ITuple2<? extends QName, ? extends Class<?>>, IRegistryObject<? extends IDuplexFunction<? extends JAXBElement<?>, ?>>> {
	BOOLEAN(ImmutableTuple2.of(UIJAXBUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createBoolean), Boolean.class),
			new IDuplexFunction.Functional<>(JAXBUtilities::getActualValue, ObjectFactories.getDefaultUIObjectFactory()::createBoolean)),
	BYTE(ImmutableTuple2.of(UIJAXBUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createByte), Byte.class),
			new IDuplexFunction.Functional<>(JAXBUtilities::getActualValue, ObjectFactories.getDefaultUIObjectFactory()::createByte)),
	SHORT(ImmutableTuple2.of(UIJAXBUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createShort), Short.class),
			new IDuplexFunction.Functional<>(JAXBUtilities::getActualValue, ObjectFactories.getDefaultUIObjectFactory()::createShort)),
	INT(ImmutableTuple2.of(UIJAXBUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createInt), Integer.class),
			new IDuplexFunction.Functional<>(JAXBUtilities::getActualValue, ObjectFactories.getDefaultUIObjectFactory()::createInt)),
	LONG(ImmutableTuple2.of(UIJAXBUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createLong), Long.class),
			new IDuplexFunction.Functional<>(JAXBUtilities::getActualValue, ObjectFactories.getDefaultUIObjectFactory()::createLong)),
	FLOAT(ImmutableTuple2.of(UIJAXBUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createFloat), Float.class),
			new IDuplexFunction.Functional<>(JAXBUtilities::getActualValue, ObjectFactories.getDefaultUIObjectFactory()::createFloat)),
	DOUBLE(ImmutableTuple2.of(UIJAXBUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createDouble), Double.class),
			new IDuplexFunction.Functional<>(JAXBUtilities::getActualValue, ObjectFactories.getDefaultUIObjectFactory()::createDouble)),
	STRING(ImmutableTuple2.of(UIJAXBUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createString), String.class),
			new IDuplexFunction.Functional<>(JAXBUtilities::getActualValue, ObjectFactories.getDefaultUIObjectFactory()::createString)),
	TUPLE_2(ImmutableTuple2.of(UIJAXBUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createTuple2), CastUtilities.<Class<ITuple2<?, ?>>>castUnchecked(ITuple2.class)),
			new IDuplexFunction.Functional<>(
					(JAXBElement<Tuple2Type> left) -> JAXBUtilities.getActualValueOptional(left)
							.map(left2 -> {
								Object rightLeft = left2.getLeft();
								Object rightRight = left2.getRight();
								return ImmutableTuple2.of(JAXBAdapterRegistries.adaptFromRaw(rightLeft), JAXBAdapterRegistries.adaptFromRaw(rightRight));
							})
							.orElse(null),
					right ->
							ObjectFactories.getDefaultUIObjectFactory().createTuple2(Optional.ofNullable(right)
									.map(right2 -> {
										Object rightLeft = right.getLeft();
										Object rightRight = right.getRight();
										return Tuple2Type.of(JAXBAdapterRegistries.adaptToRaw(rightLeft), JAXBAdapterRegistries.adaptToRaw(rightRight));
									}).orElse(null))
			)),
	@SuppressWarnings("UnstableApiUsage") SET(ImmutableTuple2.of(UIJAXBUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createSet), CastUtilities.<Class<Set<?>>>castUnchecked(Set.class)),
			new IDuplexFunction.Functional<>(
					(JAXBElement<CollectionType> left) -> processCollectionType(left.getValue()).collect(ImmutableSet.toImmutableSet()),
					right -> ObjectFactories.getDefaultUIObjectFactory().createSet(createCollectionType(right.stream()))
			)),
	@SuppressWarnings("UnstableApiUsage") LIST(ImmutableTuple2.of(UIJAXBUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createList), CastUtilities.<Class<List<?>>>castUnchecked(List.class)),
			new IDuplexFunction.Functional<>(
					(JAXBElement<CollectionType> left) -> processCollectionType(left.getValue()).collect(ImmutableList.toImmutableList()),
					right -> ObjectFactories.getDefaultUIObjectFactory().createSet(createCollectionType(right.stream()))
			)),
	@SuppressWarnings("UnstableApiUsage") RELATIONS_SET(ImmutableTuple2.of(UIJAXBUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createRelationsSet), CastUtilities.<Class<SetMultimap<?, ?>>>castUnchecked(SetMultimap.class)),
			new IDuplexFunction.Functional<>(
					(JAXBElement<RelationsType> left) -> processRelationsType(left.getValue()).collect(ImmutableSetMultimap.toImmutableSetMultimap(Map.Entry::getKey, Map.Entry::getValue)),
					right -> ObjectFactories.getDefaultUIObjectFactory().createRelationsSet(createRelationsType(right.entries().stream()))
			)),
	@SuppressWarnings("UnstableApiUsage") RELATIONS_LIST(ImmutableTuple2.of(UIJAXBUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createRelationsList), CastUtilities.<Class<ListMultimap<?, ?>>>castUnchecked(ListMultimap.class)),
			new IDuplexFunction.Functional<>(
					(JAXBElement<RelationsType> left) -> processRelationsType(left.getValue()).collect(ImmutableListMultimap.toImmutableListMultimap(Map.Entry::getKey, Map.Entry::getValue)),
					right -> ObjectFactories.getDefaultUIObjectFactory().createRelationsSet(createRelationsType(right.entries().stream()))
			)),
	@SuppressWarnings("UnstableApiUsage") MAP(ImmutableTuple2.of(UIJAXBUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createMap), CastUtilities.<Class<Map<?, ?>>>castUnchecked(Map.class)),
			new IDuplexFunction.Functional<>(
					(JAXBElement<RelationsType> left) -> processRelationsType(left.getValue()).collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue)),
					right -> ObjectFactories.getDefaultUIObjectFactory().createRelationsSet(createRelationsType(right.entrySet().stream()))
			)),
	@SuppressWarnings("RedundantLambdaParameterType") UI_SIDE(ImmutableTuple2.of(UIJAXBUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createUiSide), EnumUISide.class),
			new IDuplexFunction.Functional<>(
					(JAXBElement<UiSideType> left) -> EnumUISide.valueOf(left.getValue().name()),
					right -> ObjectFactories.getDefaultUIObjectFactory().createUiSide(UiSideType.valueOf(right.name()))
			)),
	TEXT_ANTI_ALIAS_RENDERING_HINT(ImmutableTuple2.of(UIJAXBUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createTextAntiAliasRenderingHint), ITextAntiAliasRenderingHintWrapper.class),
			new IDuplexFunction.Functional<>(
					(JAXBElement<TextAntiAliasRenderingHintType> left) -> EnumTextAntiAliasRenderingHintWrapper.valueOf(left.getValue().name()),
					right -> ObjectFactories.getDefaultUIObjectFactory().createTextAntiAliasRenderingHint(
							TextAntiAliasRenderingHintType.valueOf(EnumTextAntiAliasRenderingHintWrapper.valueOfData(right.getData()).name())
					)
			)),
	FRACTIONAL_METRICS_RENDERING_HINT(ImmutableTuple2.of(UIJAXBUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createFractionalMetricsRenderingHint), IFractionalMetricsRenderingHintWrapper.class),
			new IDuplexFunction.Functional<>(
					(JAXBElement<FractionalMetricsRenderingHintType> left) -> EnumFractionalMetricsRenderingHintWrapper.valueOf(left.getValue().name()),
					right -> ObjectFactories.getDefaultUIObjectFactory().createFractionalMetricsRenderingHint(
							FractionalMetricsRenderingHintType.valueOf(EnumFractionalMetricsRenderingHintWrapper.valueOfData(right.getData()).name())
					)
			)),
	@SuppressWarnings("unchecked") TEXT_ATTRIBUTE(ImmutableTuple2.of(UIJAXBUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createTextAttribute), AttributedCharacterIterator.Attribute.class),
			new IDuplexFunction.Functional<>(
					new Function<JAXBElement<String>, AttributedCharacterIterator.Attribute>() {
						private final @Immutable Map<String, AttributedCharacterIterator.Attribute> stringAttributeMap;

						{
							try {
								stringAttributeMap = ImmutableMap.<String, AttributedCharacterIterator.Attribute>builder()
										.putAll(
												(Map<String, AttributedCharacterIterator.Attribute>)
														InvokeUtilities.getImplLookup()
																.findStaticGetter(AttributedCharacterIterator.Attribute.class, "instanceMap", Map.class)
																.invokeExact()
										)
										.putAll(
												(Map<String, TextAttribute>)
														InvokeUtilities.getImplLookup()
																.findStaticGetter(TextAttribute.class, "instanceMap", Map.class)
																.invokeExact()
										)
										.build();
							} catch (Throwable throwable) {
								throw ThrowableUtilities.propagate(throwable);
							}
						}

						@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
						protected @Immutable Map<String, AttributedCharacterIterator.Attribute> getStringAttributeMap() {
							return stringAttributeMap;
						}

						@Nonnull
						@Override
						public AttributedCharacterIterator.Attribute apply(JAXBElement<String> left) {
							@Nullable AttributedCharacterIterator.Attribute result = getStringAttributeMap().get(left.getValue());
							if (result == null)
								throw new IllegalArgumentException();
							return result;
						}
					},
					new Function<AttributedCharacterIterator.Attribute, JAXBElement<String>>() {
						private final MethodHandle getNameVirtualMethodHandle;

						{
							try {
								getNameVirtualMethodHandle = InvokeUtilities.getImplLookup().findVirtual(AttributedCharacterIterator.Attribute.class, "getName", MethodType.methodType(String.class));
							} catch (NoSuchMethodException | IllegalAccessException e) {
								throw ThrowableUtilities.propagate(e);
							}
						}

						protected MethodHandle getGetNameVirtualMethodHandle() {
							return getNameVirtualMethodHandle;
						}

						@Nonnull
						@Override
						public JAXBElement<String> apply(AttributedCharacterIterator.Attribute right) {
							try {
								return ObjectFactories.getDefaultUIObjectFactory().createTextAttribute((String) getGetNameVirtualMethodHandle().invokeExact(right));
							} catch (Throwable throwable) {
								throw ThrowableUtilities.propagate(throwable);
							}
						}
					}
			)),
	;

	private final ITuple2<ITuple2<? extends QName, ? extends Class<?>>, IRegistryObject<? extends IDuplexFunction<? extends JAXBElement<?>, ?>>> delegate;

	@NonNls
	private static final ImmutableMap<String, Function<EnumJAXBElementPresetAdapter, ?>> OBJECT_VARIABLE_MAP =
			ImmutableMap.<String, Function<EnumJAXBElementPresetAdapter, ?>>builder()
					.put("delegate", EnumJAXBElementPresetAdapter::getDelegate)
					.build();

	@SuppressWarnings("EmptyMethod")
	public static void initializeClass() {}

	<L, R, V extends IDuplexFunction<JAXBElement<L>, R> & Serializable> EnumJAXBElementPresetAdapter(ITuple2<? extends QName, ? extends Class<R>> key, V value) {
		IRegistryObject<V> value2 = JAXBAdapterRegistries.Elements.getInstance().registerChecked(key, value);
		this.delegate = ImmutableTuple2.of(key, value2);
	}

	protected static Stream<?> processCollectionType(CollectionType left) {
		return left.getAny().stream()
				.map(JAXBAdapterRegistries::adaptFromRaw);
	}

	@Override
	public IRegistryObject<? extends IDuplexFunction<? extends JAXBElement<?>, ?>> getRight() {
		return getDelegate().getRight();
	}

	@SuppressWarnings("UnstableApiUsage")
	protected static CollectionType createCollectionType(Stream<?> right) {
		CollectionType left = ObjectFactories.getDefaultUIObjectFactory().createCollectionType();
		left.getAny().addAll(right
				.map(JAXBAdapterRegistries::adaptToRaw)
				.collect(ImmutableList.toImmutableList())
		);
		return left;
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
					return Maps.immutableEntry(JAXBAdapterRegistries.adaptFromRaw(key), JAXBAdapterRegistries.adaptFromRaw(value));
				});
	}

	@SuppressWarnings("UnstableApiUsage")
	protected static RelationsType createRelationsType(Stream<? extends Map.Entry<?, ?>> right) {
		RelationsType left = ObjectFactories.getDefaultUIObjectFactory().createRelationsType();
		left.getEntry().addAll(right
				.map(rightEntry -> {
					Object rightKey = AssertionUtilities.assertNonnull(rightEntry.getKey());
					Object rightValue = AssertionUtilities.assertNonnull(rightEntry.getValue());
					return Tuple2Type.of(JAXBAdapterRegistries.adaptToRaw(rightKey), JAXBAdapterRegistries.adaptToRaw(rightValue));
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

	@SuppressWarnings("unchecked")
	@Override
	public ITuple2<IRegistryObject<? extends IDuplexFunction<? extends JAXBElement<?>, ?>>, ITuple2<? extends QName, ? extends Class<?>>> swap() {
		return (ITuple2<IRegistryObject<? extends IDuplexFunction<? extends JAXBElement<?>, ?>>, ITuple2<? extends QName, ? extends Class<?>>>) getDelegate().swap(); // COMMENT should not matter
	}

	@Override
	public String toString() {
		return ObjectUtilities.toStringImpl(this, getObjectVariableMap());
	}

	public static ImmutableMap<String, Function<EnumJAXBElementPresetAdapter, ?>> getObjectVariableMap() { return OBJECT_VARIABLE_MAP; }
}
