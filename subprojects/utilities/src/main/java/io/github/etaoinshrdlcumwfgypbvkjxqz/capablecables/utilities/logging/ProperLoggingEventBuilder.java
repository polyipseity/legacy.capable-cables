package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.logging;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.CapacityUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.LogMessageBuilder;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.UtilitiesMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.event.DefaultLoggingEvent;
import org.slf4j.event.Level;
import org.slf4j.event.LoggingEvent;
import org.slf4j.spi.LoggingEventBuilder;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

// TODO remove when not needed
public class ProperLoggingEventBuilder
		implements LoggingEventBuilder {
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UtilitiesConfiguration.getInstance());
	private final Logger logger;
	private final DefaultLoggingEvent loggingEvent;

	public ProperLoggingEventBuilder(Logger logger, Level level) {
		this.logger = logger;
		this.loggingEvent = new DefaultLoggingEvent(level, logger);
	}

	@Override
	public LoggingEventBuilder setCause(Throwable cause) {
		getLoggingEvent().setThrowable(cause);
		return this;
	}

	protected DefaultLoggingEvent getLoggingEvent() { return loggingEvent; }

	@Override
	public LoggingEventBuilder addMarker(Marker marker) {
		getLoggingEvent().addMarker(marker);
		return this;
	}

	@Override
	public LoggingEventBuilder addArgument(Object p) {
		getLoggingEvent().addArgument(p);
		return this;
	}

	@Override
	public LoggingEventBuilder addArgument(Supplier<Object> objectSupplier) { return addArgument(objectSupplier.get()); }

	@Override
	public LoggingEventBuilder addKeyValue(String key, Object value) {
		getLoggingEvent().addKeyValue(key, value);
		return this;
	}

	@Override
	public LoggingEventBuilder addKeyValue(String key, Supplier<Object> value) { return addKeyValue(key, value.get()); }

	@Override
	public void log(String message) {
		getLoggingEvent().setMessage(message);
		log(getLoggingEvent());
	}

	@Override
	public void log(String message, Object arg) {
		addArgument(arg);
		log(message);
	}

	@Override
	public void log(String message, Object arg0, Object arg1) {
		addArgument(arg0);
		addArgument(arg1);
		log(message);
	}

	@Override
	public void log(String message, Object... args) {
		Arrays.stream(args).sequential()
				.forEachOrdered(this::addArgument);
		log(message);
	}

	@Override
	public void log(Supplier<String> messageSupplier) { log(messageSupplier.get()); }

	protected void log(LoggingEvent loggingEvent) {
		// COMMENT marker
		@Nullable Marker marker;
		boolean hasMarker;
		{
			List<Marker> markers = Optional.ofNullable(loggingEvent.getMarkers()).orElseGet(ArrayList::new);
			if (markers.size() > 1)
				throw new IllegalStateException(
						new LogMessageBuilder()
								.addMarkers(UtilitiesMarkers.getInstance()::getMarkerLogging)
								.addKeyValue("this", this).addKeyValue("loggingEvent", loggingEvent).addKeyValue("markers", markers)
								.addMessages(() -> getResourceBundle().getString("markers.size.plural"))
								.build()
				);
			marker = markers.isEmpty() ? null : markers.get(0);
			hasMarker = marker != null;
		}

		// COMMENT key-value pairs
		StringBuilder messageBuilder = new StringBuilder(CapacityUtilities.getInitialCapacityLarge());
		List<Object> argumentsList = Optional.ofNullable(loggingEvent.getArguments()).orElseGet(ArrayList::new);
		Optional.ofNullable(loggingEvent.getKeyValuePairs())
				.ifPresent(pairs -> pairs.forEach(pair -> {
					messageBuilder
							.append(pair.key)
							.append("={} ");
					argumentsList.add(pair.value);
				}));

		// COMMENT message
		messageBuilder.append(loggingEvent.getMessage());
		Optional.ofNullable(loggingEvent.getArguments())
				.ifPresent(argumentsList::addAll);

		// COMMENT throwable
		Optional.ofNullable(loggingEvent.getThrowable())
				.ifPresent(argumentsList::add);

		String message = messageBuilder.toString();
		Object[] arguments = argumentsList.toArray();
		Level level = Optional.ofNullable(loggingEvent.getLevel()).orElse(Level.INFO);
		if (hasMarker) {
			switch (level) {
				case ERROR:
					getLogger().error(marker, message, arguments);
					break;
				case WARN:
					getLogger().warn(marker, message, arguments);
					break;
				case INFO:
					getLogger().info(marker, message, arguments);
					break;
				case DEBUG:
					getLogger().debug(marker, message, arguments);
					break;
				case TRACE:
					getLogger().trace(marker, message, arguments);
					break;
				default:
					throw new AssertionError();
			}
		} else {
			switch (level) {
				case ERROR:
					getLogger().error(message, arguments);
					break;
				case WARN:
					getLogger().warn(message, arguments);
					break;
				case INFO:
					getLogger().info(message, arguments);
					break;
				case DEBUG:
					getLogger().debug(message, arguments);
					break;
				case TRACE:
					getLogger().trace(message, arguments);
					break;
				default:
					throw new AssertionError();
			}
		}
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }

	protected Logger getLogger() { return logger; }
}
