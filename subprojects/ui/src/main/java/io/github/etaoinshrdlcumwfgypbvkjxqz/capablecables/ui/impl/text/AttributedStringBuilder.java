package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.text;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Ordered;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.MapUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.core.tuples.ITuple2;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.structures.impl.tuples.ImmutableTuple2;

import java.text.AttributedCharacterIterator.Attribute;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressBoxing;
import static io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.SuppressWarningsUtilities.suppressUnboxing;

public class AttributedStringBuilder {
	private final List<String> stringList;
	private final @Ordered Map<ITuple2<Integer, Integer>, Map<Attribute, Object>> attributesList;
	private int length = 0;

	public AttributedStringBuilder(int initialCapacity) {
		this.stringList = new ArrayList<>(initialCapacity);
		this.attributesList = new LinkedHashMap<>(initialCapacity); // COMMENT ensure order
	}

	public AttributedStringBuilder attachAttributes(Map<? extends Attribute, ?> attributes) {
		int to = getLength();
		int from = to - Iterables.getLast(getStringList()).length();
		return attachAttributes(from, to, attributes);
	}

	public int getLength() {
		return length;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected List<String> getStringList() {
		return stringList;
	}

	public AttributedStringBuilder attachAttributes(int from, int to, Map<? extends Attribute, ?> attributes) {
		if (from != to) { // COMMENT need to check, will throw exception if zero length text is added
			// COMMENT it is possible that the range already exists
			getAttributesList().merge(ImmutableTuple2.of(suppressBoxing(from), suppressBoxing(to)),
					ImmutableMap.copyOf(attributes),
					MapUtilities::concatMaps);
		}
		return this;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected @Ordered Map<ITuple2<Integer, Integer>, Map<Attribute, Object>> getAttributesList() {
		return attributesList;
	}

	protected void setLength(int length) {
		this.length = length;
	}

	public AttributedStringBuilder addCharSequence(CharSequence sequence) {
		String string = sequence.toString(); // COMMENT ensure immutable
		getStringList().add(string);
		setLength(getLength() + string.length());
		return this;
	}

	public AttributedString build() {
		AttributedString result = new AttributedString(
				getStringList().stream()
						.reduce("", String::concat)
		);
		getAttributesList().forEach((range, attributes) -> {
			assert !range.getLeft().equals(range.getRight());
			result.addAttributes(attributes, suppressUnboxing(range.getLeft()), suppressUnboxing(range.getRight()));
		});
		return result;
	}
}
