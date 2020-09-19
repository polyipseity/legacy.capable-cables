package $group__.utilities;

import $group__.utilities.templates.CommonConfigurationTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

public final class UtilitiesConfiguration extends CommonConfigurationTemplate<UtilitiesConfiguration.ConfigurationData> {
	private static final UtilitiesConfiguration INSTANCE = new UtilitiesConfiguration();
	private static final Logger BOOTSTRAP_LOGGER = LoggerFactory.getLogger(UtilitiesConstants.DISPLAY_NAME);

	private UtilitiesConfiguration() { super(getBootstrapLogger()); }

	public static UtilitiesConfiguration getInstance() { return INSTANCE; }

	private static Logger getBootstrapLogger() { return BOOTSTRAP_LOGGER; }

	public static final class ConfigurationData
			extends CommonConfigurationTemplate.ConfigurationData {
		public ConfigurationData(@Nullable Logger logger,
		                         @Nullable Supplier<? extends Locale> localeSupplier) {
			super(Optional.ofNullable(logger).orElseGet(UtilitiesConfiguration::getBootstrapLogger),
					Optional.<Supplier<? extends Locale>>ofNullable(localeSupplier).orElse(Locale::getDefault));
		}
	}
}
