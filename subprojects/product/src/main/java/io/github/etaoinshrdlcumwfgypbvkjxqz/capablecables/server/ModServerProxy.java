package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.server;

import com.google.common.base.Suppliers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.proxies.AbstractServerProxy;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.*;

import java.util.function.Supplier;

@OnlyIn(Dist.DEDICATED_SERVER)
public final class ModServerProxy
		extends AbstractServerProxy {
	private static final Supplier<ModServerProxy> INSTANCE = Suppliers.memoize(ModServerProxy::new);

	private ModServerProxy() {}

	public static ModServerProxy getInstance() { return AssertionUtilities.assertNonnull(INSTANCE.get()); }

	@Override
	public void onFingerprintViolation(FMLFingerprintViolationEvent event) {

	}

	@Override
	public void onSetup(FMLCommonSetupEvent event) {

	}

	@Override
	public void onSetupSided(FMLDedicatedServerSetupEvent event) {

	}

	@Override
	public void onInterModEnqueue(InterModEnqueueEvent event) {

	}

	@Override
	public void onInterModProcess(InterModProcessEvent event) {

	}

	@Override
	public void onLoadComplete(FMLLoadCompleteEvent event) {

	}

	@Override
	public void onGatherData(GatherDataEvent event) {

	}

	@Override
	public void onModIdMapping(FMLModIdMappingEvent event) {

	}
}
