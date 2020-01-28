package $group__.$modId__.utilities.helpers;

import javax.annotation.Nullable;
import java.util.Optional;

public enum Optionals {
	/* MARK empty */;


	/* SECTION static methods */

	@Nullable
	public static <T> T unboxOptional(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<? extends T> o) { return o.orElse(null); }
}
