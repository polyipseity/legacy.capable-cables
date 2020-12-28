package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.logging;

import org.slf4j.Logger;
import org.slf4j.event.Level;
import org.slf4j.ext.LoggerWrapper;
import org.slf4j.spi.LoggingEventBuilder;

public class ProperLoggingEventBuilderLogger<T extends Logger>
		extends LoggerWrapper {
	public ProperLoggingEventBuilderLogger(T logger) { super(logger, LoggerWrapper.class.getName()); }

	@Override
	public LoggingEventBuilder makeLoggingEventBuilder(Level level) { return new ProperLoggingEventBuilder(this, level); }
}
