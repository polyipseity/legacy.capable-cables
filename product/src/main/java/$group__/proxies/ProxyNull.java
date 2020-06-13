package $group__.proxies;

import net.minecraftforge.fml.common.event.*;

import static $group__.utilities.helpers.specific.Throwables.rejectUnsupportedOperation;

public final class ProxyNull extends Proxy {
	/* SECTION methods */

	@Override
	@Deprecated
	public void construct(@SuppressWarnings("unused") ModThis$ mod,
	                      @SuppressWarnings("unused") FMLConstructionEvent event) throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public void preInitialize(@SuppressWarnings("unused") ModThis$ mod,
	                          @SuppressWarnings("unused") FMLPreInitializationEvent event) throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public void initialize(@SuppressWarnings("unused") ModThis$ mod,
	                       @SuppressWarnings("unused") FMLInitializationEvent event) throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public void postInitialize(@SuppressWarnings("unused") ModThis$ mod,
	                           @SuppressWarnings("unused") FMLPostInitializationEvent event) throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public void completeLoading(@SuppressWarnings("unused") ModThis$ mod,
	                            @SuppressWarnings("unused") FMLLoadCompleteEvent event) throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }


	@Override
	@Deprecated
	public void preStartServer(@SuppressWarnings("unused") ModThis$ mod,
	                           @SuppressWarnings("unused") FMLServerAboutToStartEvent event) throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public void startServer(@SuppressWarnings("unused") ModThis$ mod,
	                        @SuppressWarnings("unused") FMLServerStartingEvent event) throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public void postStartServer(@SuppressWarnings("unused") ModThis$ mod,
	                            @SuppressWarnings("unused") FMLServerStartedEvent event) throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public void stopServer(@SuppressWarnings("unused") ModThis$ mod,
	                       @SuppressWarnings("unused") FMLServerStoppingEvent event) throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public void postStopServer(@SuppressWarnings("unused") ModThis$ mod,
	                           @SuppressWarnings("unused") FMLServerStoppedEvent event) throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }


	@Override
	@Deprecated
	public void violateFingerprint(@SuppressWarnings("unused") ModThis$ mod,
	                               @SuppressWarnings("unused") FMLFingerprintViolationEvent event) throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public void processIMCMessages(@SuppressWarnings("unused") ModThis$ mod,
	                               @SuppressWarnings("unused") FMLInterModComms.IMCEvent event) throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }


	@Override
	@Deprecated
	public void processIDMapping(@SuppressWarnings("unused") ModThis$ mod,
	                             @SuppressWarnings("unused") FMLModIdMappingEvent event) throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }

	@Override
	@Deprecated
	public void disable(@SuppressWarnings("unused") ModThis$ mod,
	                    @SuppressWarnings("unused") FMLModDisabledEvent event) throws UnsupportedOperationException { throw rejectUnsupportedOperation(); }
}
