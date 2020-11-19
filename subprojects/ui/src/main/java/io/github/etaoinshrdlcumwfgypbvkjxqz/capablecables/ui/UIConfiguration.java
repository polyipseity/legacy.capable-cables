package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui;

import com.google.common.base.Suppliers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.cursors.EnumGLFWCursor;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.impl.events.bus.UIEventBusEntryPoint;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.client.MinecraftClientUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.events.impl.EventBusSubject;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.throwable.core.IThrowableHandler;
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
	private static final Supplier<@Nonnull UIConfiguration> INSTANCE = Suppliers.memoize(UIConfiguration::new);

	private UIConfiguration() {}

	public static Logger getBootstrapLogger() { return BOOTSTRAP_LOGGER; }

	public static UIConfiguration getInstance() { return INSTANCE.get(); }

	@Override
	protected void configure0(ConfigurationData data) {
		super.configure0(data);
		// COMMENT cursors
		EnumGLFWCursor.initializeClass();
	}

	@OnlyIn(Dist.CLIENT)
	public enum MinecraftSpecific {
		;

		public static void onConstruction() {
			/* COMMENT
			Mac is sus,
			so let us chop AWT such that it is headless,
			because Mac is immutable.

			See https://github.com/LWJGL/lwjgl3/issues/306#issuecomment-300481571.
			*/
			System.setProperty("java.awt.headless", "true");

			UIEventBusEntryPoint.setEventBus(EventBusSubject.getUIEventBus());
		}

		@SuppressWarnings("deprecation")
		public static void onLoadComplete() {
			DeferredWorkQueue.runLater(MinecraftClientUtilities.getMinecraftNonnull().getFramebuffer()::enableStencil);
		}
	}

	public static final class ConfigurationData
			extends CommonConfigurationTemplate.ConfigurationData {
		public ConfigurationData(Logger logger,
		                         IThrowableHandler<Throwable> throwableHandler,
		                         Supplier<@Nonnull ? extends Locale> localeSupplier) {
			super(logger, throwableHandler, localeSupplier);
		}
	}
}
