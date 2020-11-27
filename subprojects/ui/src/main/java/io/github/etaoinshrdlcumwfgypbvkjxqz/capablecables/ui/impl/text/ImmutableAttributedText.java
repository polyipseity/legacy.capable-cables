package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.text;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.text.IAttributedText;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.IUnion;

import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public final class ImmutableAttributedText
		implements IAttributedText {
	private final @Immutable List<IUnion<? extends CharSequence, ? extends IAttributedText>> children;
	private final @Immutable Map<AttributedCharacterIterator.Attribute, Object> attributes;
	private final Supplier<@Nonnull AttributedString> compiler;

	private ImmutableAttributedText(Iterable<? extends IUnion<? extends CharSequence, ? extends IAttributedText>> children,
	                                Map<? extends AttributedCharacterIterator.Attribute, ?> attributes) {
		this.children = ImmutableList.copyOf(children);
		this.attributes = ImmutableMap.copyOf(attributes);
		this.compiler = Suppliers.memoize(() -> {
			AttributedStringBuilder resultBuilder = new AttributedStringBuilder(getChildren().size());
			getChildren().stream()
					.map(child -> IUnion.mapRight(child, IAttributedText::compile))
					.map(child -> IUnion.mapRight(child, AttributedString::getIterator))
					.forEachOrdered(child ->
							child.accept(
									charSequence -> resultBuilder.addCharSequence(charSequence).attachAttributes(getAttributes()),
									text ->
											TextUtilities.forEachRun(text,
													text1 -> {
														Map<AttributedCharacterIterator.Attribute, Object> childAttributes =
																text1.getAttributes();
														resultBuilder.addCharSequence(
																TextUtilities.currentRun(text1)
														)
																.attachAttributes(getAttributes()) // COMMENT attach ours first
																.attachAttributes(childAttributes) // COMMENT child's attributes overwrite ours
														;
													})
							)
					);
			return resultBuilder.build();
		});
	}

	public List<IUnion<? extends CharSequence, ? extends IAttributedText>> getChildren() {
		return children;
	}

	public Map<AttributedCharacterIterator.Attribute, Object> getAttributes() {
		return attributes;
	}

	public static ImmutableAttributedText of(Iterable<? extends IUnion<? extends CharSequence, ? extends IAttributedText>> children,
	                                         Map<? extends AttributedCharacterIterator.Attribute, ?> attributes) {
		return new ImmutableAttributedText(children, attributes);
	}

	@Override
	public @Immutable List<? extends IUnion<? extends CharSequence, ? extends IAttributedText>> getChildrenView() {
		return ImmutableList.copyOf(getChildren());
	}

	@Override
	public @Immutable Map<? extends AttributedCharacterIterator.Attribute, ?> getAttributesView() {
		return ImmutableMap.copyOf(getAttributes());
	}

	@Override
	public AttributedString compile() {
		return getCompiler().get();
	}

	protected Supplier<@Nonnull ? extends AttributedString> getCompiler() {
		return compiler;
	}
}
