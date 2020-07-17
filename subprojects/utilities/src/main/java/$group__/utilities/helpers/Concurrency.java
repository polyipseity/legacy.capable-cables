package $group__.utilities.helpers;

import net.minecraftforge.api.distmarker.Dist;

public enum Concurrency {
	;

	public static final int
			MULTI_THREAD_THREAD_COUNT = Dist.values().length,
			SINGLE_THREAD_THREAD_COUNT = 1;
}
