package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.proxies;

import net.minecraftforge.fml.event.lifecycle.*;

public interface IProxy<T extends ModLifecycleEvent> {
	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	boolean onModLifecycle(ModLifecycleEvent event);

	void onFingerprintViolation(FMLFingerprintViolationEvent event);

	void onSetup(FMLCommonSetupEvent event);

	void onSetupSided(T event);

	void onInterModEnqueue(InterModEnqueueEvent event);

	void onInterModProcess(InterModProcessEvent event);

	void onLoadComplete(FMLLoadCompleteEvent event);

	void onGatherData(GatherDataEvent event);

	void onModIdMapping(FMLModIdMappingEvent event);
}
