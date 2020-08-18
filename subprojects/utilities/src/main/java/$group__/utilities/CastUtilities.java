package $group__.utilities;

import javax.annotation.Nullable;
import java.util.Optional;

public enum CastUtilities {
	;

	public static <T> T castUnchecked(Object obj) { return AssertionUtilities.assertNonnull(castUncheckedNullable(obj)); }

	@SuppressWarnings("unchecked")
	@Nullable
	public static <T> T castUncheckedNullable(@Nullable Object obj) { return (T) obj; }

	public static <T> Optional<T> castChecked(Class<T> clazz, @Nullable Object obj) {
		if (clazz.isInstance(obj))
			return Optional.of(clazz.cast(obj));
		return Optional.empty();
	}
}