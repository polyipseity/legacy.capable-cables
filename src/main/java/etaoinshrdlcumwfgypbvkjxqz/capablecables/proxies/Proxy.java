package etaoinshrdlcumwfgypbvkjxqz.capablecables.proxies;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrables.blocks.BlocksOwn;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrables.items.ItemsOwn;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.Singleton;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import javax.annotation.concurrent.Immutable;

@Immutable
public abstract class Proxy extends Singleton {
    public static final String SUBPACKAGE = "proxies";
    public Proxy() { super(); }

    public void preInitialize(@SuppressWarnings("unused") FMLPreInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(BlocksOwn.getInstance());
        MinecraftForge.EVENT_BUS.register(ItemsOwn.getInstance());
    }
}
