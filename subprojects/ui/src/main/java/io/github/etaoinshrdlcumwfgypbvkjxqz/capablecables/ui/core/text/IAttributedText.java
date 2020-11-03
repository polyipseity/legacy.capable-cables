package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.text;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.IUnion;

import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.List;
import java.util.Map;

public interface IAttributedText {
	@Immutable List<? extends IUnion<? extends CharSequence, ? extends IAttributedText>> getChildrenView();

	@Immutable Map<? extends AttributedCharacterIterator.Attribute, ?> getAttributesView();

	AttributedString compile();
}
