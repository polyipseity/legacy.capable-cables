package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.logging.LoggingUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.def.IThrowableHandler;
import org.slf4j.Logger;
import org.slf4j.ext.XLogger;

public class LoggingThrowableHandler<T extends Throwable>
		extends DelegatingThrowableHandler<T> {
	private final XLogger logger;
	private final XLogger.Level level;

	public LoggingThrowableHandler(IThrowableHandler<T> delegated, Logger logger) { this(delegated, logger, XLogger.Level.ERROR); }

	public LoggingThrowableHandler(IThrowableHandler<T> delegated, Logger logger, XLogger.Level level) {
		super(delegated);
		this.logger = LoggingUtilities.getXLogger(logger);
		this.level = level;
	}

	@Override
	public void catch_(T throwable) {
		getLogger().catching(level, throwable);
		super.catch_(throwable);
	}

	protected XLogger getLogger() { return logger; }
}
