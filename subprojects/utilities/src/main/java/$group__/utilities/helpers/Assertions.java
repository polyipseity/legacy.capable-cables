package $group__.utilities.helpers;

import javax.annotation.Nullable;

public enum Assertions {
	;

	public static <T> T assertNonnull(@Nullable T o) {
		assert o != null;
		return assumeNonnull(o);
	}

	@SuppressWarnings("ConstantConditions")
	public static <T> T assumeNonnull(@Nullable T o) { return o; }
}
