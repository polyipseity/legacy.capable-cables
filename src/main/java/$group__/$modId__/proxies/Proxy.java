package $group__.$modId__.proxies;

import $group__.$modId__.ModOwn;
import $group__.$modId__.common.gui.GuiHandler;
import $group__.$modId__.common.registrable.blocks.BlocksOwn;
import $group__.$modId__.common.registrable.items.ItemsOwn;
import $group__.$modId__.utilities.constructs.classes.Singleton;
import $group__.$modId__.utilities.constructs.interfaces.annotations.ExternalToImmutableMethod;
import $group__.$modId__.utilities.constructs.interfaces.annotations.OverridingStatus;
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
		ExternalToImmutableMethod.AnnotationProcessor.INSTANCE.process(asm);
		ExternalToImmutableMethod.AnnotationProcessor.INSTANCE.process(asm);
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
