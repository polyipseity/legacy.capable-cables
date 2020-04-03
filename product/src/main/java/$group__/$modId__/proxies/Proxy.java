package $group__.$modId__.proxies;

import $group__.$modId__.ModThis;
import $group__.$modId__.annotations.runtime.processors.ExternalCloneMethodProcessor;
import $group__.$modId__.annotations.runtime.processors.ExternalToImmutableMethodProcessor;
import $group__.$modId__.common.events.AnnotationProcessingEvent;
import $group__.$modId__.common.gui.GuiHandler;
import $group__.$modId__.utilities.Singleton;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.OverridingMethodsMustInvokeSuper;

import static $group__.$modId__.Globals.LOGGER;
import static $group__.$modId__.utilities.Constants.MOD_ID;
import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

@SuppressWarnings("EmptyMethod")
public abstract class Proxy extends Singleton {
	/* SECTION static variables */

	public static final String SUBPACKAGE = "proxies";

	protected Proxy() { super(LOGGER); }


	/* SECTION methods */

	@OverridingMethodsMustInvokeSuper
	public void construct(@SuppressWarnings("unused") ModThis mod,
	                      @SuppressWarnings("unused") FMLConstructionEvent event) {
		EVENT_BUS.register(ExternalToImmutableMethodProcessor.class);
		EVENT_BUS.register(ExternalCloneMethodProcessor.class);
		EVENT_BUS.post(new AnnotationProcessingEvent(MOD_ID, event.getASMHarvestedData(), LOGGER));
	}

	@OverridingMethodsMustInvokeSuper
	public void preInitialize(@SuppressWarnings("unused") ModThis mod,
	                          @SuppressWarnings("unused") FMLPreInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(mod, GuiHandler.INSTANCE);
	}

	@OverridingMethodsMustInvokeSuper
	public void initialize(@SuppressWarnings("unused") ModThis mod,
	                       @SuppressWarnings("unused") FMLInitializationEvent event) {}

	@OverridingMethodsMustInvokeSuper
	public void postInitialize(@SuppressWarnings("unused") ModThis mod,
	                           @SuppressWarnings("unused") FMLPostInitializationEvent event) {}

	@OverridingMethodsMustInvokeSuper
	public void completeLoading(@SuppressWarnings("unused") ModThis mod,
	                            @SuppressWarnings("unused") FMLLoadCompleteEvent event) {}

	@OverridingMethodsMustInvokeSuper
	public void preStartServer(@SuppressWarnings("unused") ModThis mod,
	                           @SuppressWarnings("unused") FMLServerAboutToStartEvent event) {}

	@OverridingMethodsMustInvokeSuper
	public void startServer(@SuppressWarnings("unused") ModThis mod,
	                        @SuppressWarnings("unused") FMLServerStartingEvent event) {}

	@OverridingMethodsMustInvokeSuper
	public void postStartServer(@SuppressWarnings("unused") ModThis mod,
	                            @SuppressWarnings("unused") FMLServerStartedEvent event) {}

	@OverridingMethodsMustInvokeSuper
	public void stopServer(@SuppressWarnings("unused") ModThis mod,
	                       @SuppressWarnings("unused") FMLServerStoppingEvent event) {}

	@OverridingMethodsMustInvokeSuper
	public void postStopServer(@SuppressWarnings("unused") ModThis mod,
	                           @SuppressWarnings("unused") FMLServerStoppedEvent event) {}

	@OverridingMethodsMustInvokeSuper
	public void violateFingerprint(@SuppressWarnings("unused") ModThis mod,
	                               @SuppressWarnings("unused") FMLFingerprintViolationEvent event) {}

	@OverridingMethodsMustInvokeSuper
	public void processIMCMessages(@SuppressWarnings("unused") ModThis mod,
	                               @SuppressWarnings("unused") FMLInterModComms.IMCEvent event) {}

	@OverridingMethodsMustInvokeSuper
	public void processIDMapping(@SuppressWarnings("unused") ModThis mod,
	                             @SuppressWarnings("unused") FMLModIdMappingEvent event) {}

	@OverridingMethodsMustInvokeSuper
	public void disable(@SuppressWarnings("unused") ModThis mod,
	                    @SuppressWarnings("unused") FMLModDisabledEvent event) {}
}
