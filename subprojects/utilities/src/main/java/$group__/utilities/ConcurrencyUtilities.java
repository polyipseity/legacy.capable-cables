package $group__.utilities;

import net.minecraftforge.api.distmarker.Dist;

public enum ConcurrencyUtilities {
	;

	public static final int
			MULTI_THREAD_THREAD_COUNT = Dist.values().length,
			SINGLE_THREAD_THREAD_COUNT = 1;
}
