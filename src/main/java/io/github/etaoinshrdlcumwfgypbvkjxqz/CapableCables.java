package io.github.etaoinshrdlcumwfgypbvkjxqz;

import io.github.etaoinshrdlcumwfgypbvkjxqz.common.registrables.blocks.BlocksOwn;
import io.github.etaoinshrdlcumwfgypbvkjxqz.common.registrables.items.ItemsOwn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static io.github.etaoinshrdlcumwfgypbvkjxqz.utilities.Constants.*;

@Mod.EventBusSubscriber
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

    public static final Logger LOGGER = LogManager.getLogger(String.format("%s (%s)", NAME, MOD_ID));

    @Mod.EventHandler
    private void preInitialize(FMLPreInitializationEvent e) {
        LOGGER.info("Pre-initialization started");
        MinecraftForge.EVENT_BUS.register(BlocksOwn.getInstance());
        MinecraftForge.EVENT_BUS.register(ItemsOwn.getInstance());
        LOGGER.info("Pre-initialization ended");
    }
}
