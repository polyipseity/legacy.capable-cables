package $group__;

import $group__.utilities.minecraft.internationalization.MinecraftLocaleUtilities;
import $group__.utilities.structures.Singleton;
import $group__.utilities.templates.CommonConfigurationTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

import static $group__.ModConstants.MOD_ID;

public final class ModConfiguration
		extends CommonConfigurationTemplate<ModConfiguration.ConfigurationData> {
	private static final Logger BOOTSTRAP_LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static final ModConfiguration INSTANCE = Singleton.getSingletonInstance(ModConfiguration.class, getBootstrapLogger());

	private ModConfiguration() { super(getBootstrapLogger()); }

	private static Logger getBootstrapLogger() { return BOOTSTRAP_LOGGER; }

	public static ModConfiguration getInstance() { return INSTANCE; }

	public static final class ConfigurationData
			extends CommonConfigurationTemplate.ConfigurationData {
		public ConfigurationData(@Nullable Logger logger,
		                         @Nullable Supplier<? extends Locale> localeSupplier) {
			super(Optional.ofNullable(logger).orElseGet(ModConfiguration::getBootstrapLogger),
					Optional.<Supplier<? extends Locale>>ofNullable(localeSupplier).orElse(MinecraftLocaleUtilities::getCurrentLocale));
		}
	}
}
