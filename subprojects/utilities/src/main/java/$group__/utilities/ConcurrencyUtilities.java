package $group__.utilities;

import net.minecraftforge.api.distmarker.Dist;

public enum ConcurrencyUtilities {
	;

	public static final int SINGLE_THREAD_THREAD_COUNT = 1;
	public static final int NORMAL_THREAD_THREAD_COUNT = Dist.values().length;
}
