package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.adapters;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.jaxb.subprojects.ui.ui.*;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.IFractionalMetricsRenderingHintWrapper;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.ITextAntiAliasRenderingHintWrapper;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.parsers.adapters.JAXBAdapterRegistries;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.text.IAttributedText;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.text.ITextLayout;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.parsers.UIJAXBUtilities.ObjectFactories;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.text.ImmutableAttributedText;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.text.ImmutableTextLayout;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ObjectUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.IDuplexFunction;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.ITuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.IUnion;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples.ImmutableTuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples.ImmutableUnion;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.registration.core.IRegistryObject;
import jakarta.xml.bind.JAXBElement;
import org.jetbrains.annotations.NonNls;

import java.io.Serializable;
import java.text.AttributedCharacterIterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@SuppressWarnings("unused")
public enum EnumJAXBObjectPresetAdapter
		implements ITuple2<ITuple2<? extends Class<?>, ? extends Class<?>>, IRegistryObject<? extends IDuplexFunction<?, ?>>> {
	COLOR(ImmutableTuple2.of(Color.class, java.awt.Color.class),
			new IDuplexFunction.Functional<>(
					left -> new java.awt.Color(left.getRed(), left.getGreen(), left.getBlue(), left.getAlpha()),
					right -> {
						Color left = ObjectFactories.getDefaultUIObjectFactory().createColor();
						left.setRed((short) right.getRed());
						left.setGreen((short) right.getGreen());
						left.setBlue((short) right.getBlue());
						left.setAlpha((short) right.getAlpha());
						return left;
					})),
	AFFINE_TRANSFORM(ImmutableTuple2.of(AffineTransform.class, java.awt.geom.AffineTransform.class),
			new IDuplexFunction.Functional<>(
					left -> {
						return new java.awt.geom.AffineTransform(new double[]{
								left.getScaleX(), left.getShearY(),
								left.getShearX(), left.getScaleY(),
								left.getTranslateX(), left.getTranslateY()
						});
					},
					right -> {
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
			new IDuplexFunction.Functional<>(
					left -> {
						JAXBElement<RelationsType> leftAttributes = ObjectFactories.getDefaultUIObjectFactory().createMap(left.getMap());
						List<Object> leftChildren = left.getStringOrAttributedText();

						@SuppressWarnings("unchecked") Map<AttributedCharacterIterator.Attribute, Object> rightAttributes =
								(Map<AttributedCharacterIterator.Attribute, Object>) JAXBAdapterRegistries.adaptFromRaw(leftAttributes);
						List<IUnion<CharSequence, IAttributedText>> rightChildren = leftChildren.stream()
								.map(JAXBAdapterRegistries::adaptFromRaw)
								.map(rightChild -> ImmutableUnion.choice(rightChild, CharSequence.class, IAttributedText.class))
								.collect(ImmutableList.toImmutableList());

						return ImmutableAttributedText.of(rightChildren, rightAttributes);
					},
					right -> {
						AttributedText left = ObjectFactories.getDefaultUIObjectFactory().createAttributedText();

						Map<? extends AttributedCharacterIterator.Attribute, ?> rightAttributes = right.getAttributesView();
						List<? extends IUnion<? extends CharSequence, ? extends IAttributedText>> rightChildren = right.getChildrenView();

						@SuppressWarnings("unchecked") JAXBElement<RelationsType> leftAttributes =
								(JAXBElement<RelationsType>) JAXBAdapterRegistries.adaptToRaw(rightAttributes);
						List<Object> leftChildren = left.getStringOrAttributedText();

						left.setMap(leftAttributes.getValue());
						rightChildren.stream()
								.map(IUnion::get)
								.map(JAXBAdapterRegistries::adaptToRaw)
								.forEachOrdered(leftChildren::add);
						return left;
					}
			)),
	FONT_RENDER_CONTEXT(ImmutableTuple2.of(FontRenderContext.class, java.awt.font.FontRenderContext.class),
			new IDuplexFunction.Functional<>(
					left -> {
						Optional<AffineTransform> leftAffineTransform = left.getAffineTransform();
						JAXBElement<TextAntiAliasRenderingHintType> leftTextAntiAliasRenderingHint = ObjectFactories.getDefaultUIObjectFactory().createTextAntiAliasRenderingHint(
								left.getTextAntiAliasRenderingHint()
						);
						JAXBElement<FractionalMetricsRenderingHintType> leftFractionalMetricsRenderingHint = ObjectFactories.getDefaultUIObjectFactory().createFractionalMetricsRenderingHint(
								left.getFractionalMetricsRenderingHint()
						);

						Optional<java.awt.geom.AffineTransform> rightAffineTransform = leftAffineTransform
								.map(JAXBAdapterRegistries::adaptFromRaw)
								.map(java.awt.geom.AffineTransform.class::cast);
						ITextAntiAliasRenderingHintWrapper rightTextAntiAliasRenderingHintWrapper = (ITextAntiAliasRenderingHintWrapper)
								JAXBAdapterRegistries.adaptFromRaw(leftTextAntiAliasRenderingHint);
						IFractionalMetricsRenderingHintWrapper rightFractionalMetricsRenderingHintWrapper = (IFractionalMetricsRenderingHintWrapper)
								JAXBAdapterRegistries.adaptFromRaw(leftFractionalMetricsRenderingHint);

						return new java.awt.font.FontRenderContext(
								rightAffineTransform.orElse(null),
								rightTextAntiAliasRenderingHintWrapper.getData(),
								rightFractionalMetricsRenderingHintWrapper.getData()
						);
					},
					right -> {
						java.awt.geom.AffineTransform rightTransform = right.getTransform();
						ITextAntiAliasRenderingHintWrapper rightAntiAliasingHint = EnumTextAntiAliasRenderingHintWrapper.valueOfData(right.getAntiAliasingHint());
						IFractionalMetricsRenderingHintWrapper rightFractionalMetricsHint = EnumFractionalMetricsRenderingHintWrapper.valueOfData(right.getFractionalMetricsHint());

						AffineTransform leftAffineTransform =
								(AffineTransform) JAXBAdapterRegistries.adaptToRaw(rightTransform);
						@SuppressWarnings("unchecked")
						JAXBElement<TextAntiAliasRenderingHintType> leftTextAntiAliasRenderingHint =
								(JAXBElement<TextAntiAliasRenderingHintType>) JAXBAdapterRegistries.adaptToRaw(rightAntiAliasingHint);
						@SuppressWarnings("unchecked")
						JAXBElement<FractionalMetricsRenderingHintType> leftFractionalMetricsRenderingHint =
								(JAXBElement<FractionalMetricsRenderingHintType>) JAXBAdapterRegistries.adaptToRaw(rightFractionalMetricsHint);

						FontRenderContext left = ObjectFactories.getDefaultUIObjectFactory().createFontRenderContext();
						left.setAffineTransform(leftAffineTransform);
						left.setTextAntiAliasRenderingHint(leftTextAntiAliasRenderingHint.getValue());
						left.setFractionalMetricsRenderingHint(leftFractionalMetricsRenderingHint.getValue());
						return left;
					}
			)),
	TEXT_LAYOUT(ImmutableTuple2.of(TextLayout.class, ITextLayout.class),
			new IDuplexFunction.Functional<>(
					left -> {
						AttributedText leftAttributedText = left.getAttributedText();
						FontRenderContext leftFontRenderContext = left.getFontRenderContext();

						IAttributedText rightAttributedText = (IAttributedText) JAXBAdapterRegistries.adaptFromRaw(leftAttributedText);
						java.awt.font.FontRenderContext rightFontRenderContext = (java.awt.font.FontRenderContext) JAXBAdapterRegistries.adaptFromRaw(leftFontRenderContext);

						return ImmutableTextLayout.of(rightAttributedText, rightFontRenderContext);
					},
					right -> {
						IAttributedText rightAttributedText = right.getText();
						java.awt.font.FontRenderContext rightFontRenderContext = right.getFontRenderContext();

						AttributedText leftAttributedText = (AttributedText) JAXBAdapterRegistries.adaptToRaw(rightAttributedText);
						FontRenderContext leftFontRenderContext = (FontRenderContext) JAXBAdapterRegistries.adaptToRaw(rightFontRenderContext);

						TextLayout left = ObjectFactories.getDefaultUIObjectFactory().createTextLayout();
						left.setAttributedText(leftAttributedText);
						left.setFontRenderContext(leftFontRenderContext);
						return left;
					}
			)),
	;

	private final ITuple2<ITuple2<? extends Class<?>, ? extends Class<?>>, IRegistryObject<? extends IDuplexFunction<?, ?>>> delegate;

	@NonNls
	private static final ImmutableMap<String, Function<EnumJAXBObjectPresetAdapter, ?>> OBJECT_VARIABLE_MAP =
			ImmutableMap.<String, Function<EnumJAXBObjectPresetAdapter, ?>>builder()
					.put("delegate", EnumJAXBObjectPresetAdapter::getDelegate)
					.build();

	@SuppressWarnings("EmptyMethod")
	public static void initializeClass() {}

	@Override
	public ITuple2<? extends Class<?>, ? extends Class<?>> getLeft() {
		return getDelegate().getLeft();
	}

	@Override
	public IRegistryObject<? extends IDuplexFunction<?, ?>> getRight() {
		return getDelegate().getRight();
	}

	protected ITuple2<? extends ITuple2<? extends Class<?>, ? extends Class<?>>, ? extends IRegistryObject<? extends IDuplexFunction<?, ?>>> getDelegate() {
		return delegate;
	}

	<L, R, V extends IDuplexFunction<L, R> & Serializable> EnumJAXBObjectPresetAdapter(ITuple2<? extends Class<L>, ? extends Class<R>> key, V value) {
		IRegistryObject<V> value2 = JAXBAdapterRegistries.Objects.getInstance().registerChecked(key, value);
		this.delegate = ImmutableTuple2.of(key, value2);
	}

	@Override
	public Object get(int index) throws IndexOutOfBoundsException {
		return getDelegate().get(index);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ITuple2<IRegistryObject<? extends IDuplexFunction<?, ?>>, ITuple2<? extends Class<?>, ? extends Class<?>>> swap() {
		return (ITuple2<IRegistryObject<? extends IDuplexFunction<?, ?>>, ITuple2<? extends Class<?>, ? extends Class<?>>>) getDelegate().swap(); // COMMENT should not matter
	}

	@Override
	public String toString() {
		return ObjectUtilities.toStringImpl(this, getObjectVariableMap());
	}

	public static ImmutableMap<String, Function<EnumJAXBObjectPresetAdapter, ?>> getObjectVariableMap() { return OBJECT_VARIABLE_MAP; }
}
