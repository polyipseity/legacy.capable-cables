package $group__.$modId__;

import $group__.$modId__.common.registrable.items.ItemWrench;
import $group__.$modId__.proxies.Proxy;
import $group__.$modId__.proxies.ProxyClient;
import $group__.$modId__.proxies.ProxyServer;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static $group__.$modId__.utilities.helpers.Throwables.requireRunOnceOnly;
import static $group__.$modId__.utilities.variables.Constants.*;

@Mod(modid = MOD_ID,
		name = NAME,
		version = VERSION,
		dependencies = DEPENDENCIES,
		useMetadata = true,
		acceptedMinecraftVersions = ACCEPTED_MINECRAFT_VERSIONS,
		certificateFingerprint = CERTIFICATE_FINGERPRINT,
		updateJSON = UPDATE_JSON)
public enum ModOwn {
	/* SECTION enums */
	INSTANCE;


	/* SECTION methods */

	@Mod.EventHandler
	private void construct(FMLConstructionEvent e) {
		LOGGER.info("Construction started");
		proxy.construct(this, e);
		LOGGER.info("Construction ended");
	}

	@Mod.EventHandler
	private void preInitialize(FMLPreInitializationEvent e) {
		LOGGER.info("Pre-initialization started");
		proxy.preInitialize(this, e);
		LOGGER.info("Pre-initialization ended");
	}


	/* SECTION static variables */

	public static final Logger LOGGER = LogManager.getLogger(NAME + "|" + MOD_ID);

	@SidedProxy(modId = MOD_ID,
			clientSide = GROUP + "." + MOD_ID + "." + Proxy.SUBPACKAGE + "." + ProxyClient.CLASS_SIMPLE_NAME,
			serverSide = GROUP + "." + MOD_ID + "." + Proxy.SUBPACKAGE + "." + ProxyServer.CLASS_SIMPLE_NAME)
	private static Proxy proxy;


	/* SECTION static methods */

	@SuppressWarnings("SameReturnValue")
	@Mod.InstanceFactory
	public static ModOwn getInstance() { return INSTANCE; }


	/* SECTION static classes */

	@Config(modid = MOD_ID, name = NAME, category = "General")
	@Config.LangKey(Configuration.LANG_KEY_BASE + ".name")
	public enum Configuration {
		;
		@SuppressWarnings("unused")
		@Config.Comment({"1st line", "2nd line"})
		@Config.Name("Template") // COMMENT overridden by @Config.LangKey
		@Config.LangKey("template.template.template_") // COMMENT overrides @Config.Name
		@Config.RequiresMcRestart
		@Config.RequiresWorldRestart
		@Config.Ignore
		private static final String TEMPLATE_ = "default";
		@SuppressWarnings("unused")
		@Config.RangeInt(min = -1337, max = 1337)
		@Config.Ignore
		private static final int INT_TEMPLATE_ = 69;
		@SuppressWarnings("unused")
		@Config.RangeDouble(min = -13.37D, max = 13.37D)
		@Config.Ignore
		private static final double DOUBLE_TEMPLATE_ = 6.9D;

		@Config.Ignore
		public static final String LANG_KEY_BASE = "config." + MOD_ID;

		@Config.LangKey(Behavior.LANG_KEY_BASE + ".name")
		public static final Behavior behavior = new Behavior();

		public static final class Behavior {
			public static final String LANG_KEY_BASE = Configuration.LANG_KEY_BASE + ".behavior";

			private Behavior() { requireRunOnceOnly(); }

			@Config.LangKey(Items.LANG_KEY_BASE + ".name")
			public final Items items = new Items();

			public static final class Items {
				public static final String LANG_KEY_BASE = Behavior.LANG_KEY_BASE + ".items";

				private Items() { requireRunOnceOnly(); }

				@Config.LangKey(ItemWrench.Configuration.LANG_KEY_BASE + ".name")
				public final ItemWrench.Configuration wrench = new ItemWrench.Configuration();
			}
		}
	}
}
