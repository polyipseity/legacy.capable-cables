package $group__.$modId__.proxies;

import $group__.$modId__.ModThis;
import net.minecraftforge.fml.common.event.*;

import static $group__.$modId__.utilities.helpers.Throwables.rejectUnsupportedOperation;

public final class ProxyNull extends Proxy {
	/* SECTION methods */

	@Override
	@Deprecated
	public void construct(@SuppressWarnings("unused") ModThis mod, @SuppressWarnings("unused") FMLConstructionEvent event) { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public void preInitialize(@SuppressWarnings("unused") ModThis mod, @SuppressWarnings("unused") FMLPreInitializationEvent event) { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public void initialize(@SuppressWarnings("unused") ModThis mod, @SuppressWarnings("unused") FMLInitializationEvent event) { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public void postInitialize(@SuppressWarnings("unused") ModThis mod, @SuppressWarnings("unused") FMLPostInitializationEvent event) { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public void completeLoading(@SuppressWarnings("unused") ModThis mod, @SuppressWarnings("unused") FMLLoadCompleteEvent event) { throw rejectUnsupportedOperation(); }


	@Override
	@Deprecated
	public void preStartServer(@SuppressWarnings("unused") ModThis mod, @SuppressWarnings("unused") FMLServerAboutToStartEvent event) { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public void startServer(@SuppressWarnings("unused") ModThis mod, @SuppressWarnings("unused") FMLServerStartingEvent event) { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public void postStartServer(@SuppressWarnings("unused") ModThis mod, @SuppressWarnings("unused") FMLServerStartedEvent event) { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public void stopServer(@SuppressWarnings("unused") ModThis mod, @SuppressWarnings("unused") FMLServerStoppingEvent event) { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public void postStopServer(@SuppressWarnings("unused") ModThis mod, @SuppressWarnings("unused") FMLServerStoppedEvent event) { throw rejectUnsupportedOperation(); }


	@Override
	@Deprecated
	public void violateFingerprint(@SuppressWarnings("unused") ModThis mod, @SuppressWarnings("unused") FMLFingerprintViolationEvent event) { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public void processIMCMessages(@SuppressWarnings("unused") ModThis mod, @SuppressWarnings("unused") FMLInterModComms.IMCEvent event) { throw rejectUnsupportedOperation(); }


	@Override
	@Deprecated
	public void processIDMapping(@SuppressWarnings("unused") ModThis mod, @SuppressWarnings("unused") FMLModIdMappingEvent event) { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public void disable(@SuppressWarnings("unused") ModThis mod, @SuppressWarnings("unused") FMLModDisabledEvent event) { throw rejectUnsupportedOperation(); }
}