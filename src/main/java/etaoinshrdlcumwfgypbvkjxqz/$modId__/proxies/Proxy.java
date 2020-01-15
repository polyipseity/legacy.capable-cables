package etaoinshrdlcumwfgypbvkjxqz.$modId__.proxies;

import etaoinshrdlcumwfgypbvkjxqz.$modId__.ModOwn;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.common.gui.GuiHandler;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.common.registrable.blocks.BlocksOwn;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.common.registrable.items.ItemsOwn;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.OverridingStatus;
import etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs.Singleton;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.OverridingMethodsMustInvokeSuper;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

public abstract class Proxy extends Singleton {
	/* SECTION methods */

	@OverridingMethodsMustInvokeSuper
	public void construct(@SuppressWarnings("unused") ModOwn mod, @SuppressWarnings("unused") FMLConstructionEvent e) {
		ASMDataTable asm = e.getASMHarvestedData();
		OverridingStatus.AnnotationProcessor.INSTANCE.process(asm);
	}

	@OverridingMethodsMustInvokeSuper
	public void preInitialize(@SuppressWarnings("unused") ModOwn mod, @SuppressWarnings("unused") FMLPreInitializationEvent e) {
		EVENT_BUS.register(BlocksOwn.INSTANCE);
		EVENT_BUS.register(ItemsOwn.INSTANCE);
		NetworkRegistry.INSTANCE.registerGuiHandler(mod, GuiHandler.INSTANCE);
	}


	/* SECTION static variables */

	public static final String SUBPACKAGE = "proxies";
}
