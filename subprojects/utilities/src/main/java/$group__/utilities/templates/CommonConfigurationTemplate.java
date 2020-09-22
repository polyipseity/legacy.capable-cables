package $group__.utilities.templates;

import $group__.utilities.AssertionUtilities;
import $group__.utilities.DynamicUtilities;
import $group__.utilities.interfaces.IRecordCandidate;
import $group__.utilities.internationalization.ChangingLocaleResourceBundle;
import $group__.utilities.throwable.IThrowableHandler;
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
	private volatile XLogger logger;
	@Nullable
	private volatile IThrowableHandler<Throwable> throwableHandler;
	@Nullable
	private volatile Supplier<? extends Locale> localeSupplier;

	protected CommonConfigurationTemplate(Logger logger) { super(logger); }

	public static ResourceBundle createBundle(CommonConfigurationTemplate<?> instance) {
		return new ChangingLocaleResourceBundle.Builder()
				.setBaseName(DynamicUtilities.getCallerClass())
				.setLocaleSupplier(instance.getLocaleSupplier())
				.build();
	}

	public Supplier<? extends Locale> getLocaleSupplier() { return AssertionUtilities.assertNonnull(localeSupplier); }

	public XLogger getLogger() { return AssertionUtilities.assertNonnull(logger); }

	public IThrowableHandler<Throwable> getThrowableHandler() { return AssertionUtilities.assertNonnull(throwableHandler); }

	protected void setThrowableHandler(@Nullable IThrowableHandler<Throwable> throwableHandler) { this.throwableHandler = throwableHandler; }

	@Override
	@OverridingMethodsMustInvokeSuper
	protected void configure0(D data) {
		setLogger(new XLogger(data.getLogger())); // COMMENT create 'XLogger' directly to avoid classloading
		setThrowableHandler(data.getThrowableHandler());
		setLocaleSupplier(data.getLocaleSupplier());
	}

	protected void setLogger(@Nullable XLogger logger) { this.logger = logger; }

	protected void setLocaleSupplier(@Nullable Supplier<? extends Locale> localeSupplier) { this.localeSupplier = localeSupplier; }

	public static abstract class ConfigurationData implements IRecordCandidate {
		private final Logger logger;
		private final IThrowableHandler<Throwable> throwableHandler;
		private final Supplier<? extends Locale> localeSupplier;

		protected ConfigurationData(Logger logger,
		                            IThrowableHandler<Throwable> throwableHandler,
		                            Supplier<? extends Locale> localeSupplier) {
			this.logger = logger;
			this.throwableHandler = throwableHandler;
			this.localeSupplier = localeSupplier;
		}

		protected Logger getLogger() { return logger; }

		protected IThrowableHandler<Throwable> getThrowableHandler() { return throwableHandler; }

		protected Supplier<? extends Locale> getLocaleSupplier() { return localeSupplier; }
	}
}
