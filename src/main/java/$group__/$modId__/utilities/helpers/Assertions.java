package $group__.$modId__.utilities.helpers;

import javax.annotation.Nullable;

public enum Assertions {
	/* MARK empty */ ;


	/* SECTION static methods */

	@SuppressWarnings("ConstantConditions")
	public static <T> T assumeNonnull(@Nullable T o) { return o; }

	public static <T> T assertNonnull(@Nullable T o) {
		assert o != null;
		return assumeNonnull(o);
	}
}
