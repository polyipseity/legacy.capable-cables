package $group__.ui;

import $group__.ui.cursors.EnumCursor;
import $group__.ui.events.bus.UIEventBusEntryPoint;
import $group__.ui.parsers.adapters.EnumJAXBElementPresetAdapter;
import $group__.ui.parsers.adapters.EnumJAXBObjectPresetAdapter;
import $group__.utilities.AssertionUtilities;
import $group__.utilities.events.EventBusForge;
import $group__.utilities.interfaces.IRecordCandidate;
import $group__.utilities.templates.ConfigurationTemplate;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Optional;

public final class UIConfiguration extends ConfigurationTemplate<UIConfiguration.ConfigurationData> {
	public static final UIConfiguration INSTANCE = new UIConfiguration();
	private static final Logger BOOTSTRAP_LOGGER = LogManager.getLogger(UIConstants.DISPLAY_NAME);

	@Nullable
	private volatile Logger logger;

	private UIConfiguration() { super(BOOTSTRAP_LOGGER); }

	public Logger getLogger() { return AssertionUtilities.assertNonnull(logger); }

	protected static Logger getBootstrapLogger() { return BOOTSTRAP_LOGGER; }

	@Override
	protected void configure0(ConfigurationData data) {
		this.logger = data.getLogger();
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

		public static void loadComplete() { Minecraft.getInstance().getFramebuffer().enableStencil(); }
	}

	public static final class ConfigurationData implements IRecordCandidate {
		private final Logger logger;

		public ConfigurationData(@Nullable Logger logger) { this.logger = Optional.ofNullable(logger).orElseGet(UIConfiguration::getBootstrapLogger); }

		public Logger getLogger() { return logger; }
	}
}
