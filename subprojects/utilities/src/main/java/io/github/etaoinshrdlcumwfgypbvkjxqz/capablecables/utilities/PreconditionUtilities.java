package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import com.google.common.base.Preconditions;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;

import java.util.Arrays;

public enum PreconditionUtilities {
	;

	public static void checkArgumentTypes(Class<?>[] types, Object... args) {
		int typesLength = types.length;
		Preconditions.checkElementIndex(typesLength - 1, args.length);
		LoopUtilities.doNTimes(typesLength, index -> {
			int i = Math.toIntExact(index);
			@Nullable Object arg = args[i];
			Preconditions.checkArgument(arg == null || types[i].isInstance(arg));
		});
	}

	public static void checkArrayContentType(Class<?> type, Object... array) {
		Arrays.stream(array).unordered()
				.map(o -> o == null || type.isInstance(o))
				.forEach(Preconditions::checkArgument);
	}
}
