package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import java.util.Arrays;

public enum PreconditionUtilities {
	;

	public static void checkArgumentTypes(Class<?>[] types, Object... args) {
		int typesLength = types.length;
		Preconditions.checkElementIndex(typesLength - 1, args.length);
		LoopUtilities.doNTimes(typesLength, index -> {
			@Nullable Object arg = args[index];
			Preconditions.checkArgument(arg == null || types[index].isInstance(arg));
		});
	}

	public static void checkArrayContentType(Class<?> type, Object... array) {
		Arrays.stream(array).unordered()
				.map(o -> o == null || type.isInstance(o))
				.forEach(Preconditions::checkArgument);
	}
}
