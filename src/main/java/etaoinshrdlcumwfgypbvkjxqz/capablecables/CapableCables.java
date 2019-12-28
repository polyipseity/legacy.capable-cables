package etaoinshrdlcumwfgypbvkjxqz.capablecables;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.proxies.Proxy;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.proxies.ProxyClient;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.proxies.ProxyServer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.Constants.*;

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
}
