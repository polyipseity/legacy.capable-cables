package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui;

import com.google.common.base.Suppliers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.cursors.EnumGLFWCursor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.adapters.EnumJAXBElementPresetAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.parsers.adapters.EnumJAXBObjectPresetAdapter;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.MinecraftClientUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.throwable.IThrowableHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DeferredWorkQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.function.Supplier;

public final class UIConfiguration
		extends CommonConfigurationTemplate<UIConfiguration.ConfigurationData> {
	private static final Logger BOOTSTRAP_LOGGER = LoggerFactory.getLogger(UIConstants.getModuleName());
	private static final Supplier<UIConfiguration> INSTANCE = Suppliers.memoize(UIConfiguration::new);

	private UIConfiguration() {}

	public static Logger getBootstrapLogger() { return BOOTSTRAP_LOGGER; }

	public static UIConfiguration getInstance() { return AssertionUtilities.assertNonnull(INSTANCE.get()); }

	@Override
	protected void configure0(ConfigurationData data) {
		super.configure0(data);
		// COMMENT JAXB adapters
		EnumJAXBElementPresetAdapter.initializeClass();
		EnumJAXBObjectPresetAdapter.initializeClass();
		// COMMENT cursors
		EnumGLFWCursor.initializeClass();
	}

	@OnlyIn(Dist.CLIENT)
	public enum MinecraftSpecific {
		;

		@SuppressWarnings("deprecation")
		public static void loadComplete() {
			DeferredWorkQueue.runLater(MinecraftClientUtilities.getMinecraftNonnull().getFramebuffer()::enableStencil);
		}
	}

	public static final class ConfigurationData
			extends CommonConfigurationTemplate.ConfigurationData {
		public ConfigurationData(Logger logger,
		                         IThrowableHandler<Throwable> throwableHandler,
		                         Supplier<? extends Locale> localeSupplier) {
			super(logger, throwableHandler, localeSupplier);
		}
	}
}
