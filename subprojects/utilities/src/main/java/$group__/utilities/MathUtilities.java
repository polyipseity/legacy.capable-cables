package $group__.utilities;

import javax.annotation.Nullable;

public enum MathUtilities {
	;

	@Nullable
	public static Double minNullable(@Nullable Double a, @Nullable Double b) {
		if (a == null && b == null)
			return null;
		else if (a == null)
			a = b;
		else if (b == null)
			b = a;
		return Math.min(a, b);
	}

	@Nullable
	public static Double maxNullable(@Nullable Double a, @Nullable Double b) {
		if (a == null && b == null)
			return null;
		else if (a == null)
			a = b;
		else if (b == null)
			b = a;
		return Math.max(a, b);
	}
}
