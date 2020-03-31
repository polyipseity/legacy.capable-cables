package $group__.$modId__.utilities.helpers;

import $group__.$modId__.utilities.helpers.specific.Throwables;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Optional;

import static $group__.$modId__.utilities.helpers.Assertions.assertNonnull;
import static $group__.$modId__.utilities.helpers.specific.Optionals.unboxOptional;
import static $group__.$modId__.utilities.helpers.specific.Throwables.*;

public enum Casts {
	/* MARK empty */;


	/* SECTION static methods */

	public static <T> T castUncheckedUnboxedNonnull(Object o) { return assertNonnull(castUncheckedUnboxed(o)); }

	@Nullable
	public static <T> T castUncheckedUnboxed(@Nullable Object o) { return unboxOptional(castUnchecked(o)); }

	@SuppressWarnings("unchecked")
	public static <T> Optional<T> castUnchecked(@Nullable Object o) { return Optional.ofNullable((T) o); }

	@Nullable
	public static <T> T castCheckedUnboxed(@Nullable Object o, Class<T> type, @Nullable Logger logger) { return unboxOptional(castChecked(o, type, logger)); }

	public static <T> Optional<T> castChecked(@Nullable Object o, Class<T> type, @Nullable Logger logger) {
		clearCaughtThrowableStatic();
		Optional<T> r = Casts.<T>castUnchecked(o).filter(t -> type.isAssignableFrom(t.getClass()));
		if (!r.isPresent() && o != null)
			try { throw cast(o, type); } catch (ClassCastException e) { setCaughtThrowableStatic(e, logger); }
		return r;
	}

	public static <T> T castCheckedUnboxedNonnull(Object o, Class<T> type, @Nullable Logger logger) { return castChecked(o, type, logger).orElseThrow(Throwables::rethrowCaughtThrowableStatic); }
}
