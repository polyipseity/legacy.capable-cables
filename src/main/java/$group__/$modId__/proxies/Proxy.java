package $group__.$modId__.proxies;

import $group__.$modId__.ModThis;
import $group__.$modId__.common.gui.GuiHandler;
import $group__.$modId__.utilities.constructs.classes.Singleton;
import $group__.$modId__.utilities.constructs.classes.concrete.AnnotationProcessingEvent;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.OverridingMethodsMustInvokeSuper;

import static $group__.$modId__.utilities.variables.Constants.MOD_ID;
import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

public abstract class Proxy extends Singleton {
	/* SECTION methods */

	@OverridingMethodsMustInvokeSuper
	public void construct(@SuppressWarnings("unused") ModThis mod, @SuppressWarnings("unused") FMLConstructionEvent e) {
		EVENT_BUS.post(new AnnotationProcessingEvent(MOD_ID, e.getASMHarvestedData()));
	}

	@OverridingMethodsMustInvokeSuper
	public void preInitialize(@SuppressWarnings("unused") ModThis mod, @SuppressWarnings("unused") FMLPreInitializationEvent e) {
		NetworkRegistry.INSTANCE.registerGuiHandler(mod, GuiHandler.INSTANCE);
	}


	/* SECTION static variables */

	public static final String SUBPACKAGE = "proxies";
}
