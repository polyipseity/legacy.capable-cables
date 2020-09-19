package $group__.utilities;

import $group__.utilities.functions.ConstantSupplier;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

public class LogMessageBuilder {
	@SuppressWarnings("StringBufferField")
	private final StringBuilder messageBuilder = new StringBuilder(CapacityUtilities.INITIAL_CAPACITY_LARGE);
	private final List<Supplier<?>> arguments = new ArrayList<>(CapacityUtilities.INITIAL_CAPACITY_SMALL);
	private final Map<String, Supplier<?>> keyValuePairs = new HashMap<>(CapacityUtilities.INITIAL_CAPACITY_SMALL);

	public LogMessageBuilder addKeyValue(String key, @Nullable Object value) { return addKeyValue(key, ConstantSupplier.ofNullable(value)); }

	public LogMessageBuilder addKeyValue(String key, Supplier<?> value) {
		getKeyValuePairs().put(key, value);
		return this;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected Map<String, Supplier<?>> getKeyValuePairs() { return keyValuePairs; }

	public LogMessageBuilder appendMessages(String... messages) {
		for (String message : messages)
			getMessageBuilder().append(message);
		return this;
	}

	protected StringBuilder getMessageBuilder() { return messageBuilder; }

	public LogMessageBuilder appendArguments(Object... arguments) {
		return appendArguments(
				Arrays.stream(arguments).sequential()
						.map(ConstantSupplier::ofNullable)
						.toArray(Supplier[]::new));
	}

	public LogMessageBuilder appendArguments(Supplier<?>... arguments) {
		Collections.addAll(getArguments(), arguments);
		return this;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected List<Supplier<?>> getArguments() { return arguments; }

	public String build() {
		StringBuilder ret = new StringBuilder(CapacityUtilities.INITIAL_CAPACITY_LARGE);
		getKeyValuePairs().forEach((key, value) ->
				ret.append(key).append('=').append(value.get()).append(' '));
		ret.append(FormattingUtilities.formatSimpleParameterized(
				getMessageBuilder().toString(),
				getArguments().stream().sequential()
						.map(Supplier::get)
						.toArray()));
		return ret.toString();
	}
}
