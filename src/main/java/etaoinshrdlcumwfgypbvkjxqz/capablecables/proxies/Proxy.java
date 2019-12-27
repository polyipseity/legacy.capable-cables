package etaoinshrdlcumwfgypbvkjxqz.capablecables.proxies;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrables.blocks.BlocksOwn;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrables.items.ItemsOwn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public abstract class Proxy {
    public static final String SUBPACKAGE = "proxies";

    public void preInitialize(@SuppressWarnings("unused") FMLPreInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(BlocksOwn.getInstance());
        MinecraftForge.EVENT_BUS.register(ItemsOwn.getInstance());
    }
}
