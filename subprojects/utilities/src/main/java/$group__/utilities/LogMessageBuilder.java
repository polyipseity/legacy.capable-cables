package $group__.utilities;

import $group__.utilities.functions.ConstantSupplier;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

public class LogMessageBuilder {
	private final Set<Supplier<?>> markers = new LinkedHashSet<>(CapacityUtilities.INITIAL_CAPACITY_SMALL);
	private final List<Supplier<?>> messages = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_TINY);
	private final List<Supplier<?>> arguments = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_SMALL);
	private final Map<String, Supplier<?>> keyValuePairs = new HashMap<>(CapacityUtilities.INITIAL_CAPACITY_SMALL);

	public LogMessageBuilder addKeyValue(@NonNls String key, @Nullable Object value) { return addKeyValue(key, ConstantSupplier.ofNullable(value)); }

	public LogMessageBuilder addKeyValue(@NonNls String key, Supplier<?> value) {
		getKeyValuePairs().put(key, value);
		return this;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Map<String, Supplier<?>> getKeyValuePairs() { return keyValuePairs; }

	public LogMessageBuilder addMarkers(Object... markers) {
		return addMarkers(
				Arrays.stream(markers).sequential()
						.map(ConstantSupplier::ofNullable)
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
				Arrays.stream(messages).sequential()
						.map(ConstantSupplier::ofNullable)
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
				Arrays.stream(arguments).sequential()
						.map(ConstantSupplier::ofNullable)
						.toArray(Supplier[]::new));
	}

	public LogMessageBuilder addArguments(Supplier<?>... arguments) {
		Collections.addAll(getArguments(), arguments);
		return this;
	}

	public String build() {
		StringBuilder ret = new StringBuilder(CapacityUtilities.INITIAL_CAPACITY_LARGE);
		boolean[] hasMarkers = {false};
		getMarkers().forEach(marker -> {
			ret.append('[').append(marker.get()).append(']');
			hasMarkers[0] = true;
		});
		if (hasMarkers[0])
			ret.append(' ');
		getKeyValuePairs().forEach((key, value) ->
				ret.append(key).append('=').append(value.get()).append(' '));
		ret.append(FormattingUtilities.formatSimpleParameterized(
				getMessages().stream().sequential()
						.map(Supplier::get)
						.map(Objects::toString)
						.reduce("", String::concat, String::concat),
				getArguments().stream().sequential()
						.map(Supplier::get)
						.toArray()));
		return ret.toString();
	}
}
