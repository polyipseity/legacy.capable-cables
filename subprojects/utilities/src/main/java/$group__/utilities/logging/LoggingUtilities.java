package $group__.utilities.logging;

import $group__.utilities.CapacityUtilities;
import $group__.utilities.CastUtilities;
import $group__.utilities.collections.CacheUtilities;
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

	private static LoadingCache<Logger, XLogger> getXLoggers() { return X_LOGGERS; }
}
