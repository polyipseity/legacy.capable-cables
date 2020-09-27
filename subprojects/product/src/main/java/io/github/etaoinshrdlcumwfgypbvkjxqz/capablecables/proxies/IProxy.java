package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.proxies;

import net.minecraftforge.fml.event.lifecycle.*;

public interface IProxy<T extends ModLifecycleEvent> {
	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	boolean onModLifecycle(ModLifecycleEvent event);

	default void onFingerprintViolation(FMLFingerprintViolationEvent event) {}

	default void onSetup(FMLCommonSetupEvent event) {}

	void onSetupSided(T event);

	default void onInterModEnqueue(InterModEnqueueEvent event) {}

	default void onInterModProcess(InterModProcessEvent event) {}

	default void onLoadComplete(FMLLoadCompleteEvent event) {}

	default void onGatherData(GatherDataEvent event) {}

	default void onModIdMapping(FMLModIdMappingEvent event) {}
}
