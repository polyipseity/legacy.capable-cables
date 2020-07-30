package $group__.utilities;

import $group__.utilities.specific.ThrowableUtilities.Try;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Optional;

public enum CastUtilities {
	;

	public static <T> T castUnchecked(Object obj) { return AssertionUtilities.assertNonnull(castUncheckedNullable(obj)); }

	@SuppressWarnings("unchecked")
	@Nullable
	public static <T> T castUncheckedNullable(@Nullable Object obj) { return (T) obj; }

	public static <T> Optional<T> castChecked(@Nullable Object obj, @Nullable Logger logger) { return Try.call(() -> castUncheckedNullable(obj), logger); }
}
