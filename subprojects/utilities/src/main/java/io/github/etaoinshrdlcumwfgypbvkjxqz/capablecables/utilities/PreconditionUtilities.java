package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkElementIndex;

public enum PreconditionUtilities {
	;

	public static void checkArgumentTypes(Class<?>[] types, Object... args) {
		int typesLength = types.length;
		checkElementIndex(typesLength - 1, args.length);
		for (int i = 0; i < typesLength; ++i) {
			@Nullable Object arg = args[i];
			checkArgument(arg == null || types[i].isInstance(arg));
		}
	}

	public static void checkArrayContentType(Class<?> type, Object... array) {
		for (@Nullable Object o : array)
			checkArgument(o == null || type.isInstance(o));
	}
}
