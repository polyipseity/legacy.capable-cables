package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.ui;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IJAXBObjectAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.registries.IJAXBAdapterRegistry;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.wrappers.IFractionalMetricsRenderingHintWrapper;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.wrappers.ITextAntiAliasRenderingHintWrapper;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.text.IAttributedText;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.text.ITextLayout;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.JAXBUIUtilities.ObjectFactories;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters.JAXBFunctionalObjectAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.wrappers.EnumFractionalMetricsRenderingHintWrapper;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.wrappers.EnumTextAntiAliasRenderingHintWrapper;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.text.ImmutableAttributedText;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.text.ImmutableTextLayout;
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
import java.util.Optional;
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
	FONT_RENDER_CONTEXT(ImmutableTuple2.of(FontRenderContext.class, java.awt.font.FontRenderContext.class),
			new JAXBFunctionalObjectAdapter<>(
					(context, left) -> {
						Optional<AffineTransform> leftAffineTransform = left.getAffineTransform();
						JAXBElement<TextAntiAliasRenderingHintType> leftTextAntiAliasRenderingHint = ObjectFactories.getDefaultUIObjectFactory().createTextAntiAliasRenderingHint(
								left.getTextAntiAliasRenderingHint()
						);
						JAXBElement<FractionalMetricsRenderingHintType> leftFractionalMetricsRenderingHint = ObjectFactories.getDefaultUIObjectFactory().createFractionalMetricsRenderingHint(
								left.getFractionalMetricsRenderingHint()
						);

						Optional<java.awt.geom.AffineTransform> rightAffineTransform = leftAffineTransform
								.map(leftAffineTransform1 -> IJAXBAdapterRegistry.adaptFromJAXB(context, leftAffineTransform1))
								.map(java.awt.geom.AffineTransform.class::cast);
						ITextAntiAliasRenderingHintWrapper rightTextAntiAliasRenderingHintWrapper = (ITextAntiAliasRenderingHintWrapper)
								IJAXBAdapterRegistry.adaptFromJAXB(context, leftTextAntiAliasRenderingHint);
						IFractionalMetricsRenderingHintWrapper rightFractionalMetricsRenderingHintWrapper = (IFractionalMetricsRenderingHintWrapper)
								IJAXBAdapterRegistry.adaptFromJAXB(context, leftFractionalMetricsRenderingHint);

						return new java.awt.font.FontRenderContext(
								rightAffineTransform.orElse(null),
								rightTextAntiAliasRenderingHintWrapper.getData(),
								rightFractionalMetricsRenderingHintWrapper.getData()
						);
					},
					(context, right) -> {
						java.awt.geom.AffineTransform rightTransform = right.getTransform();
						ITextAntiAliasRenderingHintWrapper rightAntiAliasingHint = EnumTextAntiAliasRenderingHintWrapper.valueOfData(right.getAntiAliasingHint());
						IFractionalMetricsRenderingHintWrapper rightFractionalMetricsHint = EnumFractionalMetricsRenderingHintWrapper.valueOfData(right.getFractionalMetricsHint());

						AffineTransform leftAffineTransform =
								(AffineTransform) IJAXBAdapterRegistry.adaptToJAXB(context, rightTransform);
						@SuppressWarnings("unchecked")
						JAXBElement<TextAntiAliasRenderingHintType> leftTextAntiAliasRenderingHint =
								(JAXBElement<TextAntiAliasRenderingHintType>) IJAXBAdapterRegistry.adaptToJAXB(context, rightAntiAliasingHint);
						@SuppressWarnings("unchecked")
						JAXBElement<FractionalMetricsRenderingHintType> leftFractionalMetricsRenderingHint =
								(JAXBElement<FractionalMetricsRenderingHintType>) IJAXBAdapterRegistry.adaptToJAXB(context, rightFractionalMetricsHint);

						FontRenderContext left = ObjectFactories.getDefaultUIObjectFactory().createFontRenderContext();
						left.setAffineTransform(leftAffineTransform);
						left.setTextAntiAliasRenderingHint(leftTextAntiAliasRenderingHint.getValue());
						left.setFractionalMetricsRenderingHint(leftFractionalMetricsRenderingHint.getValue());
						return left;
					}
			)),
	TEXT_LAYOUT(ImmutableTuple2.of(TextLayout.class, ITextLayout.class),
			new JAXBFunctionalObjectAdapter<>(
					(context, left) -> {
						AttributedText leftAttributedText = left.getAttributedText();
						FontRenderContext leftFontRenderContext = left.getFontRenderContext();

						IAttributedText rightAttributedText = (IAttributedText) IJAXBAdapterRegistry.adaptFromJAXB(context, leftAttributedText);
						java.awt.font.FontRenderContext rightFontRenderContext = (java.awt.font.FontRenderContext) IJAXBAdapterRegistry.adaptFromJAXB(context, leftFontRenderContext);

						return ImmutableTextLayout.of(rightAttributedText, rightFontRenderContext);
					},
					(context, right) -> {
						IAttributedText rightAttributedText = right.getText();
						java.awt.font.FontRenderContext rightFontRenderContext = right.getFontRenderContext();

						AttributedText leftAttributedText = (AttributedText) IJAXBAdapterRegistry.adaptToJAXB(context, rightAttributedText);
						FontRenderContext leftFontRenderContext = (FontRenderContext) IJAXBAdapterRegistry.adaptToJAXB(context, rightFontRenderContext);

						TextLayout left = ObjectFactories.getDefaultUIObjectFactory().createTextLayout();
						left.setAttributedText(leftAttributedText);
						left.setFontRenderContext(leftFontRenderContext);
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

	@Override
	public String toString() {
		return ObjectUtilities.toStringImpl(this, getObjectVariableMap());
	}

	public static ImmutableMap<String, Function<@Nonnull EnumJAXBUIDefaultObjectAdapter, @Nullable ?>> getObjectVariableMap() { return OBJECT_VARIABLE_MAP; }

	public ITuple2<? extends Class<?>, ? extends Class<?>> getKey() {
		return key;
	}

	public IJAXBObjectAdapter<?, ?> getValue() {
		return value;
	}
}
