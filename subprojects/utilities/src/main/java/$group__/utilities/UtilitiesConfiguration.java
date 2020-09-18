package $group__.utilities;

import $group__.utilities.interfaces.IRecordCandidate;
import $group__.utilities.templates.ConfigurationTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Optional;

public final class UtilitiesConfiguration extends ConfigurationTemplate<UtilitiesConfiguration.ConfigurationData> {
	public static final UtilitiesConfiguration INSTANCE = new UtilitiesConfiguration();
	private static final Logger BOOTSTRAP_LOGGER = LogManager.getLogger(UtilitiesConstants.DISPLAY_NAME);

	@Nullable
	private volatile Logger logger;

	private UtilitiesConfiguration() { super(BOOTSTRAP_LOGGER); }

	private static Logger getBootstrapLogger() { return BOOTSTRAP_LOGGER; }

	@Override
	protected void configure0(ConfigurationData data) {
		logger = data.getLogger();
	}

	public Logger getLogger() { return AssertionUtilities.assertNonnull(logger); }

	public static final class ConfigurationData implements IRecordCandidate {
		private final Logger logger;

		public ConfigurationData(@Nullable Logger logger) {
			this.logger = Optional.ofNullable(logger).orElseGet(UtilitiesConfiguration::getBootstrapLogger);
		}

		public Logger getLogger() { return logger; }
	}
}
