package $group__.$modId__.utilities.constructs.interfaces.basic;

import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;

import javax.annotation.Nullable;
import javax.annotation.meta.When;
import java.util.Locale;

import static $group__.$modId__.utilities.helpers.Miscellaneous.Casts.castUnchecked;
import static $group__.$modId__.utilities.variables.Constants.GROUP;
import static java.util.Objects.requireNonNull;

@SuppressWarnings("SpellCheckingInspection")
public interface IImmutablizable<T> {
	/* SECTION methods */

	@OverridingStatus(group = GROUP, when = When.ALWAYS)
	T toImmutable();

	@OverridingStatus(group = GROUP, when = When.MAYBE)
	default boolean isImmutable() { return getClass().getSimpleName().toLowerCase(Locale.ROOT).contains("immutable"); }


	/* SECTION static methods */

	@Nullable
	static <T> T tryToImmutable(@Nullable T o) { return o instanceof IImmutablizable<?> ? castUnchecked(o, (IImmutablizable<T>) null).toImmutable() : o; }

	static <T> T tryToImmutableNonnull(T o) { return requireNonNull(tryToImmutable(o)); }
}
