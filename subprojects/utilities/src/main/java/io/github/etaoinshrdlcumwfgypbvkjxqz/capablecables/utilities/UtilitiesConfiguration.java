package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities;

import com.google.common.base.Suppliers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.throwable.IThrowableHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.function.Supplier;

public final class UtilitiesConfiguration extends CommonConfigurationTemplate<UtilitiesConfiguration.ConfigurationData> {
	private static final Logger BOOTSTRAP_LOGGER = LoggerFactory.getLogger(UtilitiesConstants.getModuleName());
	private static final Supplier<UtilitiesConfiguration> INSTANCE = Suppliers.memoize(UtilitiesConfiguration::new);

	private UtilitiesConfiguration() {}

	public static UtilitiesConfiguration getInstance() { return AssertionUtilities.assertNonnull(INSTANCE.get()); }

	public static Logger getBootstrapLogger() { return BOOTSTRAP_LOGGER; }

	public static final class ConfigurationData
			extends CommonConfigurationTemplate.ConfigurationData {
		public ConfigurationData(Logger logger,
		                         IThrowableHandler<Throwable> throwableHandler,
		                         Supplier<? extends Locale> localeSupplier) {
			super(logger, throwableHandler, localeSupplier);
		}
	}
}
