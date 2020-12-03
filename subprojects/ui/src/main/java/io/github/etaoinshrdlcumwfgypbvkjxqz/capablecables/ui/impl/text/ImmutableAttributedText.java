package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.text;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.text.IAttributedText;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.CacheUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.IUnion;

import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public final class ImmutableAttributedText
		implements IAttributedText {
	private final @Immutable List<IUnion<? extends Supplier<? extends CharSequence>, ? extends IAttributedText>> children;
	private final @Immutable Map<AttributedCharacterIterator.Attribute, Object> attributes;
	private final LoadingCache<@Immutable List<? extends IUnion<? extends String, ? extends AttributedString>>, AttributedString> compiler =
			CacheUtilities.newCacheBuilderSingleThreaded().maximumSize(10).build(CacheLoader.from(sources -> {
				assert sources != null;
				AttributedStringBuilder resultBuilder = new AttributedStringBuilder(getChildren().size());
				sources.stream()
						.map(child -> IUnion.mapRight(child, AttributedString::getIterator))
						.forEachOrdered(child ->
								child.accept(
										charSequence ->
												resultBuilder.addCharSequence(charSequence).attachAttributes(getAttributes()),
										text ->
												TextUtilities.forEachRun(text,
														textRun -> {
															Map<AttributedCharacterIterator.Attribute, Object> childAttributes =
																	textRun.getAttributes();
															resultBuilder.addCharSequence(
																	TextUtilities.currentRun(textRun)
															)
																	.attachAttributes(getAttributes()) // COMMENT attach ours first
																	.attachAttributes(childAttributes) // COMMENT child's attributes overwrite ours
															;
														})
								)
						);
				return resultBuilder.build();
			}));

	private ImmutableAttributedText(Iterable<? extends IUnion<? extends Supplier<? extends CharSequence>, ? extends IAttributedText>> children,
	                                Map<? extends AttributedCharacterIterator.Attribute, ?> attributes) {
		this.children = ImmutableList.copyOf(children);
		this.attributes = ImmutableMap.copyOf(attributes);
	}

	public static ImmutableAttributedText of(Iterable<? extends IUnion<? extends Supplier<? extends CharSequence>, ? extends IAttributedText>> children,
	                                         Map<? extends AttributedCharacterIterator.Attribute, ?> attributes) {
		return new ImmutableAttributedText(children, attributes);
	}

	protected Map<AttributedCharacterIterator.Attribute, Object> getAttributes() {
		return attributes;
	}

	@Override
	public @Immutable List<? extends IUnion<? extends Supplier<? extends CharSequence>, ? extends IAttributedText>> getChildrenView() {
		return ImmutableList.copyOf(getChildren());
	}

	protected List<IUnion<? extends Supplier<? extends CharSequence>, ? extends IAttributedText>> getChildren() {
		return children;
	}

	@Override
	public @Immutable Map<? extends AttributedCharacterIterator.Attribute, ?> getAttributesView() {
		return ImmutableMap.copyOf(getAttributes());
	}

	@SuppressWarnings({"UnstableApiUsage", "rawtypes", "RedundantSuppression"})
	@Override
	public AttributedString compile() {
		return getCompiler().getUnchecked(
				getChildren().stream()
						.map(child -> child.mapBoth(Supplier::get, IAttributedText::compile))
						.map(child -> IUnion.mapLeft(child, CharSequence::toString))
						.collect(ImmutableList.toImmutableList())
		);
	}

	protected LoadingCache<List<? extends IUnion<? extends String, ? extends AttributedString>>, ? extends AttributedString> getCompiler() {
		return compiler;
	}
}
