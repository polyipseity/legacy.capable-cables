package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui;

import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.UiAxisType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.UiRotationType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.UiSideType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.adapters.IJAXBAdapterContext;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.adapters.IJAXBElementAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.def.parsers.adapters.registries.IJAXBAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.JAXBUIUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.JAXBUIUtilities.ObjectFactories;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.JAXBEnumElementAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.JAXBFunctionalElementAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUIAxis;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUIRotation;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.utilities.EnumUISide;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.dynamic.InvokeUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.def.tuples.ITuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples.ImmutableTuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;
import jakarta.xml.bind.JAXBElement;
import org.jetbrains.annotations.NonNls;

import javax.xml.namespace.QName;
import java.awt.font.TextAttribute;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.text.AttributedCharacterIterator;
import java.util.Arrays;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

@SuppressWarnings("unused")
public enum EnumJAXBUIDefaultElementAdapter {
	UI_SIDE(ImmutableTuple2.of(JAXBUIUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createUiSide), EnumUISide.class),
			new JAXBEnumElementAdapter<>(UiSideType.class, EnumUISide.class, ObjectFactories.getDefaultUIObjectFactory()::createUiSide)),
	UI_ROTATION(ImmutableTuple2.of(JAXBUIUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createUiRotation), EnumUIRotation.class),
			new JAXBEnumElementAdapter<>(UiRotationType.class, EnumUIRotation.class, ObjectFactories.getDefaultUIObjectFactory()::createUiRotation)),
	UI_AXIS(ImmutableTuple2.of(JAXBUIUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createUiAxis), EnumUIAxis.class),
			new JAXBEnumElementAdapter<>(UiAxisType.class, EnumUIAxis.class, ObjectFactories.getDefaultUIObjectFactory()::createUiAxis)),
	@SuppressWarnings({"unchecked", "Convert2Diamond", "AnonymousInnerClass"}) TEXT_ATTRIBUTE(ImmutableTuple2.of(JAXBUIUtilities.getQName(ObjectFactories.getDefaultUIObjectFactory()::createTextAttribute), AttributedCharacterIterator.Attribute.class),
			new JAXBFunctionalElementAdapter<String, AttributedCharacterIterator.Attribute>(
					new BiFunction<@Nonnull IJAXBAdapterContext, @Nonnull JAXBElement<String>, AttributedCharacterIterator.@Nonnull Attribute>() {
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
						@Nonnull
						public AttributedCharacterIterator.Attribute apply(@Nonnull IJAXBAdapterContext context, @Nonnull JAXBElement<String> left) {
							@Nullable AttributedCharacterIterator.Attribute result = getStringAttributeMap().get(left.getValue());
							if (result == null)
								throw new IllegalArgumentException();
							return result;
						}
					},
					new BiFunction<@Nonnull IJAXBAdapterContext, AttributedCharacterIterator.@Nonnull Attribute, @Nonnull JAXBElement<String>>() {
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
						@Nonnull
						@SuppressWarnings("cast")
						public JAXBElement<String> apply(@Nonnull IJAXBAdapterContext context, @Nonnull AttributedCharacterIterator.Attribute right) {
							try {
								return ObjectFactories.getDefaultUIObjectFactory().createTextAttribute((String) getGetNameVirtualMethodHandle().invokeExact((AttributedCharacterIterator.Attribute) right));
							} catch (Throwable throwable) {
								throw ThrowableUtilities.propagate(throwable);
							}
						}
					}
			)),
	;

	@NonNls
	private static final ImmutableMap<String, Function<@Nonnull EnumJAXBUIDefaultElementAdapter, @Nullable ?>> OBJECT_VARIABLE_MAP =
			ImmutableMap.<String, Function<@Nonnull EnumJAXBUIDefaultElementAdapter, @Nullable ?>>builder()
					.put("key", EnumJAXBUIDefaultElementAdapter::getKey)
					.put("value", EnumJAXBUIDefaultElementAdapter::getValue)
					.build();
	private final ITuple2<? extends QName, ? extends Class<?>> key;
	private final IJAXBElementAdapter<?, ?> value;

	<L, R, V extends IJAXBElementAdapter<L, R>> EnumJAXBUIDefaultElementAdapter(ITuple2<? extends QName, ? extends Class<R>> key, V value) {
		this.key = key;
		this.value = value;
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

	public static ImmutableMap<String, Function<@Nonnull EnumJAXBUIDefaultElementAdapter, @Nullable ?>> getObjectVariableMap() { return OBJECT_VARIABLE_MAP; }
}
