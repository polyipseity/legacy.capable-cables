package $group__.utilities;

import javax.annotation.Nullable;
import java.util.Optional;

public enum MathUtilities {
	;

	@Nullable
	public static Double minNullable(@Nullable Double a, @Nullable Double b) {
		if (a == null && b == null)
			return null;
		return Math.min(Optional.ofNullable(a).orElse(b), Optional.ofNullable(b).orElse(a));
	}

	@Nullable
	public static Double maxNullable(@Nullable Double a, @Nullable Double b) {
		if (a == null && b == null)
			return null;
		return Math.max(Optional.ofNullable(a).orElse(b), Optional.ofNullable(b).orElse(a));
	}
}
