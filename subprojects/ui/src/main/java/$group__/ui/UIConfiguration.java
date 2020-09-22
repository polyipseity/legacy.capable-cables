package $group__.ui;

import $group__.ui.cursors.EnumGLFWCursor;
import $group__.ui.events.bus.UIEventBusEntryPoint;
import $group__.ui.parsers.adapters.EnumJAXBElementPresetAdapter;
import $group__.ui.parsers.adapters.EnumJAXBObjectPresetAdapter;
import $group__.utilities.events.EventBusForge;
import $group__.utilities.minecraft.client.ClientUtilities;
import $group__.utilities.structures.Singleton;
import $group__.utilities.templates.CommonConfigurationTemplate;
import $group__.utilities.throwable.IThrowableHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.function.Supplier;

public final class UIConfiguration
		extends CommonConfigurationTemplate<UIConfiguration.ConfigurationData> {
	private static final Logger BOOTSTRAP_LOGGER = LoggerFactory.getLogger(UIConstants.MODULE_NAME);
	private static final UIConfiguration INSTANCE = Singleton.getSingletonInstance(UIConfiguration.class);

	private UIConfiguration() { super(getBootstrapLogger()); }

	public static Logger getBootstrapLogger() { return BOOTSTRAP_LOGGER; }

	public static UIConfiguration getInstance() { return INSTANCE; }

	@Override
	protected void configure0(ConfigurationData data) {
		super.configure0(data);
		// COMMENT JAXB adapters
		EnumJAXBElementPresetAdapter.initializeClass();
		EnumJAXBObjectPresetAdapter.initializeClass();
		// COMMENT event bus
		UIEventBusEntryPoint.setEventBus(EventBusForge.FORGE_EVENT_BUS);
		// COMMENT cursors
		EnumGLFWCursor.initializeClass();
	}

	@OnlyIn(Dist.CLIENT)
	public enum MinecraftSpecific {
		;

		public static void loadComplete() { ClientUtilities.getMinecraftNonnull().getFramebuffer().enableStencil(); }
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
