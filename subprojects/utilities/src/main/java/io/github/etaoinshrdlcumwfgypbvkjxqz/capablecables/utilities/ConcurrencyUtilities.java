package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import net.minecraftforge.api.distmarker.Dist;

public enum ConcurrencyUtilities {
	;

	public static final int SINGLE_THREAD_THREAD_COUNT = 1;
	private static final int NORMAL_THREAD_THREAD_COUNT = Dist.values().length;

	public static int getSingleThreadThreadCount() {
		return SINGLE_THREAD_THREAD_COUNT;
	}

	public static int getNormalThreadThreadCount() {
		return NORMAL_THREAD_THREAD_COUNT;
	}
}
