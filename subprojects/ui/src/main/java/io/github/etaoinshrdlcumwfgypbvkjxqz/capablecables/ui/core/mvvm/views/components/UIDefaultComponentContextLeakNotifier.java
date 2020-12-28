package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.core.mvvm.views.components;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConstants;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.compile.EnumBuildType;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.impl.ThrowableUtilities;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.BooleanSupplier;

public class UIDefaultComponentContextLeakNotifier
		implements Runnable {
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(UIConfiguration.getInstance());

	private final BooleanSupplier closedSupplier;
	private final Logger logger;
	@Nullable
	private final Throwable throwable;

	public UIDefaultComponentContextLeakNotifier(BooleanSupplier closedSupplier, Logger logger) {
		this.closedSupplier = closedSupplier;
		this.logger = logger;
		this.throwable = UIConstants.getBuildType() == EnumBuildType.DEBUG ? ThrowableUtilities.create() : null;
	}

	@Override
	public void run() {
		if (!getClosedSupplier().getAsBoolean())
			getLogger().atWarn()
					.addMarker(IUIComponentContext.StaticHolder.getClassMarker())
					.setCause(getThrowable().orElse(null))
					.log(() -> getResourceBundle().getString("closed.false"));
	}

	protected BooleanSupplier getClosedSupplier() {
		return closedSupplier;
	}

	protected Logger getLogger() { return logger; }

	protected Optional<? extends Throwable> getThrowable() { return Optional.ofNullable(throwable); }

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }
}
