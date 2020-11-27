package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.AffineTransform;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.AttributedText;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.Color;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.RelationsType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IJAXBObjectAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.registries.IJAXBAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.text.IAttributedText;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.JAXBUIUtilities.ObjectFactories;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.JAXBFunctionalObjectAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.text.ImmutableAttributedText;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.ITuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.IUnion;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples.ImmutableTuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples.ImmutableUnion;
import jakarta.xml.bind.JAXBElement;
import org.jetbrains.annotations.NonNls;

import java.text.AttributedCharacterIterator;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@SuppressWarnings("unused")
public enum EnumJAXBUIDefaultObjectAdapter {
	COLOR(ImmutableTuple2.of(Color.class, java.awt.Color.class),
			new JAXBFunctionalObjectAdapter<>(
					(context, left) -> new java.awt.Color(left.getRed(), left.getGreen(), left.getBlue(), left.getAlpha()),
					(context, right) -> {
						Color left = ObjectFactories.getDefaultUIObjectFactory().createColor();
						left.setRed((short) right.getRed());
						left.setGreen((short) right.getGreen());
						left.setBlue((short) right.getBlue());
						left.setAlpha((short) right.getAlpha());
						return left;
					})),
	AFFINE_TRANSFORM(ImmutableTuple2.of(AffineTransform.class, java.awt.geom.AffineTransform.class),
			new JAXBFunctionalObjectAdapter<>(
					(context, left) -> {
						return new java.awt.geom.AffineTransform(new double[]{
								left.getScaleX(), left.getShearY(),
								left.getShearX(), left.getScaleY(),
								left.getTranslateX(), left.getTranslateY()
						});
					},
					(context, right) -> {
						AffineTransform left = ObjectFactories.getDefaultUIObjectFactory().createAffineTransform();
						left.setTranslateX(right.getTranslateX());
						left.setTranslateY(right.getTranslateY());
						left.setScaleX(right.getScaleX());
						left.setScaleY(right.getScaleY());
						left.setShearX(right.getShearX());
						left.setShearY(right.getShearY());
						return left;
					}
			)),
	@SuppressWarnings("UnstableApiUsage") ATTRIBUTED_TEXT(ImmutableTuple2.of(AttributedText.class, IAttributedText.class),
			new JAXBFunctionalObjectAdapter<>(
					(context, left) -> {
						JAXBElement<RelationsType> leftAttributes = ObjectFactories.getDefaultUIObjectFactory().createMap(left.getMap());
						List<Object> leftChildren = left.getStringOrAttributedText();

						@SuppressWarnings("unchecked") Map<AttributedCharacterIterator.Attribute, Object> rightAttributes =
								(Map<AttributedCharacterIterator.Attribute, Object>) IJAXBAdapterRegistry.adaptFromJAXB(context, leftAttributes);
						List<IUnion<CharSequence, IAttributedText>> rightChildren = leftChildren.stream()
								.map(leftChild -> IJAXBAdapterRegistry.adaptFromJAXB(context, leftChild))
								.map(rightChild -> ImmutableUnion.of(rightChild, CharSequence.class, IAttributedText.class))
								.collect(ImmutableList.toImmutableList());

						return ImmutableAttributedText.of(rightChildren, rightAttributes);
					},
					(context, right) -> {
						AttributedText left = ObjectFactories.getDefaultUIObjectFactory().createAttributedText();

						Map<? extends AttributedCharacterIterator.Attribute, ?> rightAttributes = right.getAttributesView();
						List<? extends IUnion<? extends CharSequence, ? extends IAttributedText>> rightChildren = right.getChildrenView();

						@SuppressWarnings("unchecked") JAXBElement<RelationsType> leftAttributes =
								(JAXBElement<RelationsType>) IJAXBAdapterRegistry.adaptToJAXB(context, rightAttributes);
						List<Object> leftChildren = left.getStringOrAttributedText();

						left.setMap(leftAttributes.getValue());
						rightChildren.stream()
								.map(IUnion::get)
								.map(rightChild -> IJAXBAdapterRegistry.adaptToJAXB(context, rightChild))
								.forEachOrdered(leftChildren::add);
						return left;
					}
			)),
	;

	@NonNls
	private static final ImmutableMap<String, Function<@Nonnull EnumJAXBUIDefaultObjectAdapter, @Nullable ?>> OBJECT_VARIABLE_MAP =
			ImmutableMap.<String, Function<@Nonnull EnumJAXBUIDefaultObjectAdapter, @Nullable ?>>builder()
					.put("key", EnumJAXBUIDefaultObjectAdapter::getKey)
					.put("value", EnumJAXBUIDefaultObjectAdapter::getValue)
					.build();
	private final ITuple2<? extends Class<?>, ? extends Class<?>> key;
	private final IJAXBObjectAdapter<?, ?> value;

	<L, R, V extends IJAXBObjectAdapter<L, R>> EnumJAXBUIDefaultObjectAdapter(ITuple2<? extends Class<L>, ? extends Class<R>> key, V value) {
		this.key = key;
		this.value = value;
	}

	public static void registerAll(IJAXBAdapterRegistry registry) {
		Arrays.stream(values()).unordered()
				.forEach(adapter -> adapter.register(registry));
	}

	@SuppressWarnings("deprecation")
	public void register(IJAXBAdapterRegistry registry) {
		registry.getObjectRegistry().register(getKey(), getValue()); // COMMENT use deprecated, checked offers no benefits
	}

	public ITuple2<? extends Class<?>, ? extends Class<?>> getKey() {
		return key;
	}

	public IJAXBObjectAdapter<?, ?> getValue() {
		return value;
	}

	@Override
	public String toString() {
		return ObjectUtilities.toStringImpl(this, getObjectVariableMap());
	}

	public static ImmutableMap<String, Function<@Nonnull EnumJAXBUIDefaultObjectAdapter, @Nullable ?>> getObjectVariableMap() { return OBJECT_VARIABLE_MAP; }
}
