package $group__.utilities.templates;

import $group__.utilities.AssertionUtilities;
import $group__.utilities.DynamicUtilities;
import $group__.utilities.interfaces.IRecordCandidate;
import $group__.utilities.internationalization.ChangingLocaleResourceBundle;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Supplier;

public abstract class CommonConfigurationTemplate<D extends CommonConfigurationTemplate.ConfigurationData>
		extends ConfigurationTemplate<D> {
	@Nullable
	protected volatile Supplier<? extends Logger> loggerSupplier;
	@Nullable
	protected volatile Supplier<? extends Locale> localeSupplier;

	protected CommonConfigurationTemplate(Logger logger) { super(logger); }

	public static ResourceBundle createBundle(CommonConfigurationTemplate<?> instance) {
		return new ChangingLocaleResourceBundle.Builder()
				.setBaseName(DynamicUtilities.getCallerClass())
				.setLocaleSupplier(instance.getLocaleSupplier())
				.build();
	}

	public Supplier<? extends Locale> getLocaleSupplier() { return AssertionUtilities.assertNonnull(localeSupplier); }

	public Logger getLogger() { return AssertionUtilities.assertNonnull(AssertionUtilities.assertNonnull(loggerSupplier).get()); }

	@Override
	@OverridingMethodsMustInvokeSuper
	protected void configure0(D data) {
		loggerSupplier = data.getLoggerSupplier();
		localeSupplier = data.getLocaleSupplier();
	}

	public static abstract class ConfigurationData implements IRecordCandidate {
		protected final Supplier<? extends Logger> loggerSupplier;
		protected final Supplier<? extends Locale> localeSupplier;

		public ConfigurationData(Supplier<? extends Logger> loggerSupplier,
		                         Supplier<? extends Locale> localeSupplier) {
			this.loggerSupplier = loggerSupplier;
			this.localeSupplier = localeSupplier;
		}

		public Supplier<? extends Logger> getLoggerSupplier() { return loggerSupplier; }

		public Supplier<? extends Locale> getLocaleSupplier() { return localeSupplier; }
	}
}
