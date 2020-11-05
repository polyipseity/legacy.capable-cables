package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters;

import com.google.common.collect.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IContextIndependentJAXBAdapterFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IJAXBAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IJAXBElementAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.registries.IJAXBAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.wrappers.IFractionalMetricsRenderingHintWrapper;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.wrappers.ITextAntiAliasRenderingHintWrapper;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.UIJAXBUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.UIJAXBUtilities.ObjectFactories;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.wrappers.EnumFractionalMetricsRenderingHintWrapper;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.wrappers.EnumTextAntiAliasRenderingHintWrapper;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUISide;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.InvokeUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.ITuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples.ImmutableTuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;
import jakarta.xml.bind.JAXBElement;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nullable;
import javax.xml.namespace.QName;
import java.awt.font.TextAttribute;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.text.AttributedCharacterIterator;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public enum EnumJAXBElementPresetAdapter {
	BOOLEAN(ImmutableTuple2.of(UIJAXBUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createBoolean), Boolean.class),
			new DefaultJAXBElementAdapter<>(IContextIndependentJAXBAdapterFunction.of(JAXBElement::getValue),
					IContextIndependentJAXBAdapterFunction.of(ObjectFactories.getDefaultUIObjectFactory()::createBoolean))),
	BYTE(ImmutableTuple2.of(UIJAXBUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createByte), Byte.class),
			new DefaultJAXBElementAdapter<>(IContextIndependentJAXBAdapterFunction.of(JAXBElement::getValue),
					IContextIndependentJAXBAdapterFunction.of(ObjectFactories.getDefaultUIObjectFactory()::createByte))),
	SHORT(ImmutableTuple2.of(UIJAXBUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createShort), Short.class),
			new DefaultJAXBElementAdapter<>(IContextIndependentJAXBAdapterFunction.of(JAXBElement::getValue),
					IContextIndependentJAXBAdapterFunction.of(ObjectFactories.getDefaultUIObjectFactory()::createShort))),
	INT(ImmutableTuple2.of(UIJAXBUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createInt), Integer.class),
			new DefaultJAXBElementAdapter<>(IContextIndependentJAXBAdapterFunction.of(JAXBElement::getValue),
					IContextIndependentJAXBAdapterFunction.of(ObjectFactories.getDefaultUIObjectFactory()::createInt))),
	LONG(ImmutableTuple2.of(UIJAXBUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createLong), Long.class),
			new DefaultJAXBElementAdapter<>(IContextIndependentJAXBAdapterFunction.of(JAXBElement::getValue),
					IContextIndependentJAXBAdapterFunction.of(ObjectFactories.getDefaultUIObjectFactory()::createLong))),
	FLOAT(ImmutableTuple2.of(UIJAXBUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createFloat), Float.class),
			new DefaultJAXBElementAdapter<>(IContextIndependentJAXBAdapterFunction.of(JAXBElement::getValue),
					IContextIndependentJAXBAdapterFunction.of(ObjectFactories.getDefaultUIObjectFactory()::createFloat))),
	DOUBLE(ImmutableTuple2.of(UIJAXBUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createDouble), Double.class),
			new DefaultJAXBElementAdapter<>(IContextIndependentJAXBAdapterFunction.of(JAXBElement::getValue),
					IContextIndependentJAXBAdapterFunction.of(ObjectFactories.getDefaultUIObjectFactory()::createDouble))),
	STRING(ImmutableTuple2.of(UIJAXBUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createString), String.class),
			new DefaultJAXBElementAdapter<>(IContextIndependentJAXBAdapterFunction.of(JAXBElement::getValue),
					IContextIndependentJAXBAdapterFunction.of(ObjectFactories.getDefaultUIObjectFactory()::createString))),
	TUPLE_2(ImmutableTuple2.of(UIJAXBUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createTuple2), CastUtilities.castUnchecked(ITuple2.class)),
			new DefaultJAXBElementAdapter<Tuple2Type, ITuple2<?, ?>>(
					(context, left) -> {
						Tuple2Type left1 = left.getValue();
						Object rightLeft = left1.getLeft();
						Object rightRight = left1.getRight();
						return ImmutableTuple2.of(IJAXBAdapterRegistry.adaptFromJAXB(context.getRegistry(), rightLeft),
								IJAXBAdapterRegistry.adaptFromJAXB(context.getRegistry(), rightRight));
					},
					(context, right) -> {
						Object rightLeft = right.getLeft();
						Object rightRight = right.getRight();
						return ObjectFactories.getDefaultUIObjectFactory().createTuple2(
								Tuple2Type.of(IJAXBAdapterRegistry.adaptToJAXB(context.getRegistry(), rightLeft),
										IJAXBAdapterRegistry.adaptToJAXB(context.getRegistry(), rightRight))
						);
					}
			)),
	@SuppressWarnings("UnstableApiUsage") SET(ImmutableTuple2.of(UIJAXBUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createSet), CastUtilities.castUnchecked(Set.class)),
			new DefaultJAXBElementAdapter<CollectionType, Set<?>>(
					(context, left) -> processCollectionType(context, left.getValue()).collect(ImmutableSet.toImmutableSet()),
					(context, right) -> ObjectFactories.getDefaultUIObjectFactory().createSet(createCollectionType(context, right.stream()))
			)),
	@SuppressWarnings("UnstableApiUsage") LIST(ImmutableTuple2.of(UIJAXBUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createList), CastUtilities.castUnchecked(List.class)),
			new DefaultJAXBElementAdapter<CollectionType, List<?>>(
					(context, left) -> processCollectionType(context, left.getValue()).collect(ImmutableList.toImmutableList()),
					(context, right) -> ObjectFactories.getDefaultUIObjectFactory().createSet(createCollectionType(context, right.stream()))
			)),
	@SuppressWarnings("UnstableApiUsage") RELATIONS_SET(ImmutableTuple2.of(UIJAXBUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createRelationsSet), CastUtilities.castUnchecked(SetMultimap.class)),
			new DefaultJAXBElementAdapter<RelationsType, SetMultimap<?, ?>>(
					(context, left) -> processRelationsType(context, left.getValue()).collect(ImmutableSetMultimap.toImmutableSetMultimap(Map.Entry::getKey, Map.Entry::getValue)),
					(context, right) -> ObjectFactories.getDefaultUIObjectFactory().createRelationsSet(createRelationsType(context, right.entries().stream()))
			)),
	@SuppressWarnings("UnstableApiUsage") RELATIONS_LIST(ImmutableTuple2.of(UIJAXBUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createRelationsList), CastUtilities.castUnchecked(ListMultimap.class)),
			new DefaultJAXBElementAdapter<RelationsType, ListMultimap<?, ?>>(
					(context, left) -> processRelationsType(context, left.getValue()).collect(ImmutableListMultimap.toImmutableListMultimap(Map.Entry::getKey, Map.Entry::getValue)),
					(context, right) -> ObjectFactories.getDefaultUIObjectFactory().createRelationsSet(createRelationsType(context, right.entries().stream()))
			)),
	@SuppressWarnings("UnstableApiUsage") MAP(ImmutableTuple2.of(UIJAXBUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createMap), CastUtilities.castUnchecked(Map.class)),
			new DefaultJAXBElementAdapter<RelationsType, Map<?, ?>>(
					(context, left) -> processRelationsType(context, left.getValue()).collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue)),
					(context, right) -> ObjectFactories.getDefaultUIObjectFactory().createRelationsSet(createRelationsType(context, right.entrySet().stream()))
			)),
	@SuppressWarnings("Convert2Diamond") UI_SIDE(ImmutableTuple2.of(UIJAXBUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createUiSide), EnumUISide.class),
			new DefaultJAXBElementAdapter<UiSideType, EnumUISide>(
					(context, left) -> EnumUISide.valueOf(left.getValue().name()),
					(context, right) -> ObjectFactories.getDefaultUIObjectFactory().createUiSide(UiSideType.valueOf(right.name()))
			)),
	@SuppressWarnings("Convert2Diamond") TEXT_ANTI_ALIAS_RENDERING_HINT(ImmutableTuple2.of(UIJAXBUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createTextAntiAliasRenderingHint), ITextAntiAliasRenderingHintWrapper.class),
			new DefaultJAXBElementAdapter<TextAntiAliasRenderingHintType, ITextAntiAliasRenderingHintWrapper>(
					(context, left) -> EnumTextAntiAliasRenderingHintWrapper.valueOf(left.getValue().name()),
					(context, right) -> ObjectFactories.getDefaultUIObjectFactory().createTextAntiAliasRenderingHint(
							TextAntiAliasRenderingHintType.valueOf(EnumTextAntiAliasRenderingHintWrapper.valueOfData(right.getData()).name())
					)
			)),
	@SuppressWarnings("Convert2Diamond") FRACTIONAL_METRICS_RENDERING_HINT(ImmutableTuple2.of(UIJAXBUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createFractionalMetricsRenderingHint), IFractionalMetricsRenderingHintWrapper.class),
			new DefaultJAXBElementAdapter<FractionalMetricsRenderingHintType, IFractionalMetricsRenderingHintWrapper>(
					(context, left) -> EnumFractionalMetricsRenderingHintWrapper.valueOf(left.getValue().name()),
					(context, right) -> ObjectFactories.getDefaultUIObjectFactory().createFractionalMetricsRenderingHint(
							FractionalMetricsRenderingHintType.valueOf(EnumFractionalMetricsRenderingHintWrapper.valueOfData(right.getData()).name())
					)
			)),
	@SuppressWarnings({"unchecked", "Convert2Diamond"}) TEXT_ATTRIBUTE(ImmutableTuple2.of(UIJAXBUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createTextAttribute), AttributedCharacterIterator.Attribute.class),
			new DefaultJAXBElementAdapter<String, AttributedCharacterIterator.Attribute>(
					new BiFunction<IJAXBAdapterContext, JAXBElement<String>, AttributedCharacterIterator.Attribute>() {
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

						@Override
						public AttributedCharacterIterator.Attribute apply(IJAXBAdapterContext ijaxbAdapterContext, JAXBElement<String> left) {
							@Nullable AttributedCharacterIterator.Attribute result = getStringAttributeMap().get(left.getValue());
							if (result == null)
								throw new IllegalArgumentException();
							return result;
						}
					},
					new BiFunction<IJAXBAdapterContext, AttributedCharacterIterator.Attribute, JAXBElement<String>>() {
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

						@Override
						public JAXBElement<String> apply(IJAXBAdapterContext ijaxbAdapterContext, AttributedCharacterIterator.Attribute right) {
							try {
								return ObjectFactories.getDefaultUIObjectFactory().createTextAttribute((String) getGetNameVirtualMethodHandle().invokeExact(right));
							} catch (Throwable throwable) {
								throw ThrowableUtilities.propagate(throwable);
							}
						}
					}
			)),
	;

	@NonNls
	private static final ImmutableMap<String, Function<EnumJAXBElementPresetAdapter, ?>> OBJECT_VARIABLE_MAP =
			ImmutableMap.<String, Function<EnumJAXBElementPresetAdapter, ?>>builder()
					.put("key", EnumJAXBElementPresetAdapter::getKey)
					.put("value", EnumJAXBElementPresetAdapter::getValue)
					.build();
	private final ITuple2<? extends QName, ? extends Class<?>> key;
	private final IJAXBElementAdapter<?, ?> value;

	<L, R, V extends IJAXBElementAdapter<L, R>> EnumJAXBElementPresetAdapter(ITuple2<? extends QName, ? extends Class<R>> key, V value) {
		this.key = key;
		this.value = value;
	}

	private static Stream<?> processCollectionType(IJAXBAdapterContext context, CollectionType left) {
		return left.getAny().stream()
				.map(leftElement -> IJAXBAdapterRegistry.adaptFromJAXB(context.getRegistry(), leftElement));
	}

	@SuppressWarnings("UnstableApiUsage")
	private static CollectionType createCollectionType(IJAXBAdapterContext context, Stream<?> right) {
		CollectionType left = ObjectFactories.getDefaultUIObjectFactory().createCollectionType();
		left.getAny().addAll(right
				.map(rightElement -> IJAXBAdapterRegistry.adaptToJAXB(context.getRegistry(), rightElement))
				.collect(ImmutableList.toImmutableList())
		);
		return left;
	}

	private static Stream<Map.Entry<?, ?>> processRelationsType(IJAXBAdapterContext context, RelationsType left) {
		return left.getEntry().stream()
				.map(leftEntry -> {
					Object key = leftEntry.getLeft();
					Object value = leftEntry.getRight();
					return Maps.immutableEntry(IJAXBAdapterRegistry.adaptFromJAXB(context.getRegistry(), key),
							IJAXBAdapterRegistry.adaptFromJAXB(context.getRegistry(), value));
				});
	}

	@SuppressWarnings("UnstableApiUsage")
	private static RelationsType createRelationsType(IJAXBAdapterContext context, Stream<? extends Map.Entry<?, ?>> right) {
		RelationsType left = ObjectFactories.getDefaultUIObjectFactory().createRelationsType();
		left.getEntry().addAll(right
				.map(rightEntry -> {
					Object rightKey = AssertionUtilities.assertNonnull(rightEntry.getKey());
					Object rightValue = AssertionUtilities.assertNonnull(rightEntry.getValue());
					return Tuple2Type.of(IJAXBAdapterRegistry.adaptToJAXB(context.getRegistry(), rightKey),
							IJAXBAdapterRegistry.adaptToJAXB(context.getRegistry(), rightValue));
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

	public static ImmutableMap<String, Function<EnumJAXBElementPresetAdapter, ?>> getObjectVariableMap() { return OBJECT_VARIABLE_MAP; }
}
