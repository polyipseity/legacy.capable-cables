package etaoinshrdlcumwfgypbvkjxqz.capablecables;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.items.ItemWrench;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.proxies.Proxy;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.proxies.ProxyClient;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.proxies.ProxyServer;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.Constants.*;
import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.ThrowableHelper.requireRunOnceOnly;

@Mod(modid = MOD_ID,
        name = NAME,
        version = VERSION,
        dependencies = DEPENDENCIES,
        useMetadata = true,
        acceptedMinecraftVersions = ACCEPTED_MINECRAFT_VERSIONS,
        certificateFingerprint = CERTIFICATE_FINGERPRINT,
        updateJSON = UPDATE_JSON)
public enum CapableCables {
    INSTANCE;
    @SuppressWarnings("SameReturnValue")
    @Mod.InstanceFactory
    public static CapableCables getInstance() { return INSTANCE; }

    public static final Logger LOGGER = LogManager.getLogger(String.format("%s|%s", NAME, MOD_ID));
    @SidedProxy(modId = MOD_ID,
            clientSide = GROUP + "." + MOD_ID + "." + Proxy.SUBPACKAGE + "." + ProxyClient.CLASS_SIMPLE_NAME,
            serverSide = GROUP + "." + MOD_ID + "." + Proxy.SUBPACKAGE + "." + ProxyServer.CLASS_SIMPLE_NAME)
    private static Proxy proxy;

    @Mod.EventHandler
    private void preInitialize(FMLPreInitializationEvent e) {
        LOGGER.info("Pre-initialization started");
        proxy.preInitialize(e);
        LOGGER.info("Pre-initialization ended");
    }

    @Config(modid = MOD_ID, name = NAME, category = "General")
    @Config.LangKey(Configuration.LANG_KEY_BASE + ".name")
    public enum Configuration {
        ;
        /* Templates */
        @SuppressWarnings("unused")
        @Config.Comment({"1st line", "2nd line"})
        @Config.Name("Template") // Overridden by @Config.LangKey
        @Config.LangKey("template.template.template_") // Overrides @Config.Name
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
