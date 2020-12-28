package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common;

import com.google.common.base.Suppliers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ModMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.blocks.ModBlocks;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.inventory.ModContainerTypes;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.items.ModItems;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.registrable.tileentities.ModTileEntityTypes;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.proxies.impl.AbstractProxy;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.*;
import org.slf4j.Marker;

import java.util.function.Supplier;

@Immutable
public final class ModCommonProxy
		extends AbstractProxy<Void> {
	private static final Supplier<@Nonnull ModCommonProxy> INSTANCE = Suppliers.memoize(ModCommonProxy::new);

	private ModCommonProxy() {
		super(null);
	}

	public static ModCommonProxy getInstance() {
		return INSTANCE.get();
	}

	@Override
	protected Marker getMarker() {
		return ModMarkers.getInstance().getMarkerLifecycle();
	}

	@Override
	protected void onConstruction() {
		IEventBus modEventBus = AssertionUtilities.assertNonnull(Bus.MOD.bus().get());
		ModBlocks.getRegister().register(modEventBus);
		ModItems.getRegister().register(modEventBus);
		ModTileEntityTypes.getRegister().register(modEventBus);
		ModContainerTypes.getRegister().register(modEventBus);
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
	protected void onModIDMapping(FMLModIdMappingEvent event) {

	}

	@Override
	protected void onGatherData(GatherDataEvent event) {

	}

	@Override
	protected void onSetupSided(Void event) {
		throw new AbstractMethodError();
	}
}
