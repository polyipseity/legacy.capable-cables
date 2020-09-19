package $group__.utilities.templates;

import $group__.utilities.AssertionUtilities;
import $group__.utilities.DynamicUtilities;
import $group__.utilities.interfaces.IRecordCandidate;
import $group__.utilities.internationalization.ChangingLocaleResourceBundle;
import org.slf4j.Logger;
import org.slf4j.ext.XLogger;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Supplier;

public abstract class CommonConfigurationTemplate<D extends CommonConfigurationTemplate.ConfigurationData>
		extends ConfigurationTemplate<D> {
	@Nullable
	protected volatile XLogger logger;
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

	public XLogger getLogger() { return AssertionUtilities.assertNonnull(logger); }

	@Override
	@OverridingMethodsMustInvokeSuper
	protected void configure0(D data) {
		logger = new XLogger(data.getLogger());
		localeSupplier = data.getLocaleSupplier();
	}

	public static abstract class ConfigurationData implements IRecordCandidate {
		protected final Logger logger;
		protected final Supplier<? extends Locale> localeSupplier;

		public ConfigurationData(Logger logger,
		                         Supplier<? extends Locale> localeSupplier) {
			this.logger = logger;
			this.localeSupplier = localeSupplier;
		}

		public Logger getLogger() { return logger; }

		public Supplier<? extends Locale> getLocaleSupplier() { return localeSupplier; }
	}
}
