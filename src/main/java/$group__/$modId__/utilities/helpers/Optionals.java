package $group__.$modId__.utilities.helpers;

import javax.annotation.Nullable;
import java.util.Optional;

import static $group__.$modId__.utilities.helpers.Assertions.assertNonnull;

public enum Optionals {
	/* MARK empty */;


	/* SECTION static methods */

	@Nullable
	public static <T> T unboxOptional(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<? extends T> o) { return o.orElse(null); }

	public static <T> T unboxOptionalNonnull(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<? extends T> o) { return assertNonnull(unboxOptional(o)); }
}
