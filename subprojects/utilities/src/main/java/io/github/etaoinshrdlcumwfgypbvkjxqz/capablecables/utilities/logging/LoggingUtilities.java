package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.logging;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CastUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections.CacheUtilities;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.ext.XLogger;

public enum LoggingUtilities {
	;

	private static final LoadingCache<Logger, XLogger> X_LOGGERS =
			CacheUtilities.newCacheBuilderNormalThreaded().initialCapacity(CapacityUtilities.INITIAL_CAPACITY_SMALL).weakKeys()
					.build(CacheLoader.from(XLogger::new));

	public static XLogger getXLogger(Logger logger) {
		return CastUtilities.castChecked(XLogger.class, logger)
				.orElseGet(() -> getXLoggers().getUnchecked(logger));
	}

	@SuppressWarnings("SameReturnValue")
	private static LoadingCache<Logger, XLogger> getXLoggers() { return X_LOGGERS; }
}
