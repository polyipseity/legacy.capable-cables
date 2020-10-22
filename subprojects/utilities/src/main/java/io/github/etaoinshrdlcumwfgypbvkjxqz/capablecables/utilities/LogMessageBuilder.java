package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.functions.ConstantSupplier;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.logging.FormattingUtilities;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

public class LogMessageBuilder {
	private final Set<Supplier<?>> markers = new LinkedHashSet<>(CapacityUtilities.getInitialCapacitySmall());
	private final List<Supplier<?>> messages = new ArrayList<>(CapacityUtilities.getInitialCapacityTiny());
	private final List<Supplier<?>> arguments = new ArrayList<>(CapacityUtilities.getInitialCapacitySmall());
	private final Map<String, Supplier<?>> keyValuePairs = new HashMap<>(CapacityUtilities.getInitialCapacitySmall());

	public LogMessageBuilder addKeyValue(@NonNls CharSequence key, @Nullable Object value) { return addKeyValue(key, ConstantSupplier.of(value)); }

	public LogMessageBuilder addKeyValue(@NonNls CharSequence key, Supplier<?> value) {
		getKeyValuePairs().put(key.toString(), value);
		return this;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Map<String, Supplier<?>> getKeyValuePairs() { return keyValuePairs; }

	public LogMessageBuilder addMarkers(Object... markers) {
		return addMarkers(
				Arrays.stream(markers)
						.map(ConstantSupplier::of)
						.toArray(Supplier[]::new));
	}

	public LogMessageBuilder addMarkers(Supplier<?>... markers) {
		Collections.addAll(getMarkers(), markers);
		return this;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Set<Supplier<?>> getMarkers() { return markers; }

	public LogMessageBuilder addMessages(Object... messages) {
		return addMessages(
				Arrays.stream(messages)
						.map(ConstantSupplier::of)
						.toArray(Supplier[]::new));
	}

	public LogMessageBuilder addMessages(Supplier<?>... messages) {
		Collections.addAll(getMessages(), messages);
		return this;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected List<Supplier<?>> getMessages() { return messages; }

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected List<Supplier<?>> getArguments() { return arguments; }

	public LogMessageBuilder addArguments(Object... arguments) {
		return addArguments(
				Arrays.stream(arguments)
						.map(ConstantSupplier::of)
						.toArray(Supplier[]::new));
	}

	public LogMessageBuilder addArguments(Supplier<?>... arguments) {
		Collections.addAll(getArguments(), arguments);
		return this;
	}

	public String build() {
		StringBuilder ret = new StringBuilder(CapacityUtilities.getInitialCapacityLarge());
		boolean[] hasMarkers = {false};
		getMarkers().forEach(marker -> {
			ret.append('[').append(marker.get()).append(']');
			hasMarkers[0] = true;
		});
		if (hasMarkers[0])
			ret.append(' ');
		getKeyValuePairs().forEach((key, value) ->
				ret.append(key).append('=').append(AssertionUtilities.assertNonnull(value).get()).append(' '));
		ret.append(FormattingUtilities.formatSimpleParameterized(
				getMessages().stream()
						.map(Supplier::get)
						.map(Objects::toString)
						.reduce("", String::concat, String::concat),
				getArguments().stream()
						.map(Supplier::get)
						.toArray()));
		return ret.toString();
	}
}
