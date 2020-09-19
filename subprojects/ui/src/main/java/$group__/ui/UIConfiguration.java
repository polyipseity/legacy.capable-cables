package $group__.ui;

import $group__.ui.cursors.EnumCursor;
import $group__.ui.events.bus.UIEventBusEntryPoint;
import $group__.ui.parsers.adapters.EnumJAXBElementPresetAdapter;
import $group__.ui.parsers.adapters.EnumJAXBObjectPresetAdapter;
import $group__.utilities.AssertionUtilities;
import $group__.utilities.events.EventBusForge;
import $group__.utilities.templates.CommonConfigurationTemplate;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

public final class UIConfiguration
		extends CommonConfigurationTemplate<UIConfiguration.ConfigurationData> {
	private static final UIConfiguration INSTANCE = new UIConfiguration();
	private static final Logger BOOTSTRAP_LOGGER = LoggerFactory.getLogger(UIConstants.DISPLAY_NAME);

	private UIConfiguration() { super(getBootstrapLogger()); }

	private static Logger getBootstrapLogger() { return BOOTSTRAP_LOGGER; }

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
		EnumCursor.initializeClass();
	}

	@OnlyIn(Dist.CLIENT)
	public enum MinecraftSpecific {
		;

		public static void loadComplete() { AssertionUtilities.assertNonnull(Minecraft.getInstance()).getFramebuffer().enableStencil(); }
	}

	public static final class ConfigurationData
			extends CommonConfigurationTemplate.ConfigurationData {
		public ConfigurationData(@Nullable Logger logger,
		                         @Nullable Supplier<? extends Locale> localeSupplier) {
			super(Optional.ofNullable(logger).orElseGet(UIConfiguration::getBootstrapLogger),
					Optional.<Supplier<? extends Locale>>ofNullable(localeSupplier).orElse(Locale::getDefault));
		}
	}
}
