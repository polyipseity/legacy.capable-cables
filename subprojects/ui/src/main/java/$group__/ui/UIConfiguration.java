package $group__.ui;

import $group__.ui.events.bus.UIEventBusEntryPoint;
import $group__.ui.parsers.adapters.EnumJAXBElementPresetAdapter;
import $group__.ui.parsers.adapters.EnumJAXBObjectPresetAdapter;
import $group__.ui.structures.EnumCursor;
import $group__.utilities.AssertionUtilities;
import $group__.utilities.events.EventBusForge;
import $group__.utilities.interfaces.IRecordCandidate;
import $group__.utilities.templates.ConfigurationTemplate;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Optional;

public final class UIConfiguration extends ConfigurationTemplate<UIConfiguration.ConfigurationData> {
	public static final UIConfiguration INSTANCE = new UIConfiguration();
	private static final Logger BOOTSTRAP_LOGGER = LogManager.getLogger(UIConstants.DISPLAY_NAME);

	@Nullable
	private volatile String modID;
	@Nullable
	private volatile Logger logger;

	private UIConfiguration() { super(BOOTSTRAP_LOGGER); }

	// TODO this is too Minecraft-y
	@Deprecated
	public static void loadComplete() {
		Minecraft.getInstance().getFramebuffer().enableStencil();
		EnumCursor.initializeClass();
	}

	public String getModID() { return AssertionUtilities.assertNonnull(modID); }

	public Logger getLogger() { return AssertionUtilities.assertNonnull(logger); }

	@Override
	protected void configure0(ConfigurationData data) {
		this.modID = data.getModID();
		this.logger = data.getLogger();
		// COMMENT JAXB adapters
		EnumJAXBElementPresetAdapter.initializeClass();
		EnumJAXBObjectPresetAdapter.initializeClass();
		// COMMENT event bus
		UIEventBusEntryPoint.setEventBus(EventBusForge.FORGE_EVENT_BUS);
	}

	public static final class ConfigurationData implements IRecordCandidate {
		private final String modID;
		private final Logger logger;

		public ConfigurationData(String modID, @Nullable Logger logger) {
			this.modID = modID;
			this.logger = Optional.ofNullable(logger).orElseGet(() ->
					LogManager.getLogger(UIConstants.DISPLAY_NAME + '-' + this.modID));
		}

		public String getModID() { return modID; }

		public Logger getLogger() { return logger; }
	}
}
