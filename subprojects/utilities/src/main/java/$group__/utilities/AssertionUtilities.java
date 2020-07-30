package $group__.utilities;

import javax.annotation.Nullable;

public enum AssertionUtilities {
	;

	public static <T> T assertNonnull(@Nullable T o) {
		assert o != null;
		return o;
	}
}
