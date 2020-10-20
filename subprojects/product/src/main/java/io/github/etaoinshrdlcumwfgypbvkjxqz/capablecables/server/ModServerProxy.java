package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.server;

import com.google.common.base.Suppliers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.ModCommonProxy;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.proxies.impl.AbstractServerProxy;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.*;

import java.util.function.Supplier;

@OnlyIn(Dist.DEDICATED_SERVER)
@Immutable
public final class ModServerProxy
		extends AbstractServerProxy {
	private static final Supplier<ModServerProxy> INSTANCE = Suppliers.memoize(ModServerProxy::new);

	private ModServerProxy() {
		super(ModCommonProxy.getInstance());
	}

	public static ModServerProxy getInstance() { return AssertionUtilities.assertNonnull(INSTANCE.get()); }

	@Override
	protected void onConstruction() {

	}

	@Override
	protected void onFingerprintViolation(FMLFingerprintViolationEvent event) {

	}

	@Override
	protected void onSetup(FMLCommonSetupEvent event) {

	}

	@Override
	protected void onInterModEnqueue(InterModEnqueueEvent event) {

	}

	@Override
	protected void onInterModProcess(InterModProcessEvent event) {

	}

	@Override
	protected void onLoadComplete(FMLLoadCompleteEvent event) {

	}

	@Override
	protected void onModIdMapping(FMLModIdMappingEvent event) {

	}

	@Override
	protected void onGatherData(GatherDataEvent event) {

	}

	@Override
	protected void onSetupSided(FMLDedicatedServerSetupEvent event) {

	}
}
