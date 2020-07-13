package $group__.utilities.helpers;

import $group__.utilities.helpers.specific.Optionals;
import $group__.utilities.helpers.specific.Throwables;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Optional;

public enum Casts {
	/* MARK empty */;


	public static <T> T castUncheckedUnboxedNonnull(Object o) { return Assertions.assertNonnull(castUncheckedUnboxed(o)); }

	@Nullable
	public static <T> T castUncheckedUnboxed(@Nullable Object o) { return Optionals.unboxOptional(castUnchecked(o)); }

	@SuppressWarnings("unchecked")
	public static <T> Optional<T> castUnchecked(@Nullable Object o) { return Optional.ofNullable((T) o); }

	@Nullable
	public static <T> T castCheckedUnboxed(@Nullable Object o, Class<T> type, @Nullable Logger logger) { return Optionals.unboxOptional(castChecked(o, type, logger)); }

	public static <T> Optional<T> castChecked(@Nullable Object o, Class<T> type, @Nullable Logger logger) {
		Throwables.clearCaughtThrowableStatic();
		Optional<T> r = Casts.<T>castUnchecked(o).filter(t -> type.isAssignableFrom(t.getClass()));
		if (!r.isPresent() && o != null)
			try { throw Throwables.cast(o, type); } catch (ClassCastException e) {
				Throwables.setCaughtThrowableStatic(e, logger);
			}
		return r;
	}

	public static <T> T castCheckedUnboxedNonnull(Object o, Class<T> type, @Nullable Logger logger) { return castChecked(o, type, logger).orElseThrow(Throwables::rethrowCaughtThrowableStatic); }
}
