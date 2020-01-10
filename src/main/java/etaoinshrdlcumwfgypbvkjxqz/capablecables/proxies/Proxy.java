package etaoinshrdlcumwfgypbvkjxqz.capablecables.proxies;

import etaoinshrdlcumwfgypbvkjxqz.capablecables.CapableCables;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.client.gui.utilities.constructs.NumberRelativeDisplay;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.common.gui.GuiHandler;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.blocks.BlocksOwn;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.items.ItemsOwn;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.constructs.MethodOverrideable;
import etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.constructs.Singleton;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

public abstract class Proxy extends Singleton {
	public static final String SUBPACKAGE = "proxies";

	public Proxy() { super(); }

	public void construct(@SuppressWarnings("unused") CapableCables mod, @SuppressWarnings("unused") FMLConstructionEvent e) {
		ASMDataTable asm = e.getASMHarvestedData();
		MethodOverrideable.PROCESSOR.process(asm);
	}

	public void preInitialize(@SuppressWarnings("unused") CapableCables mod, @SuppressWarnings("unused") FMLPreInitializationEvent e) {
		EVENT_BUS.register(BlocksOwn.getInstance());
		EVENT_BUS.register(ItemsOwn.getInstance());
		EVENT_BUS.register(NumberRelativeDisplay.class);
		NetworkRegistry.INSTANCE.registerGuiHandler(mod, GuiHandler.INSTANCE);
	}
}
