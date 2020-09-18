package $group__.proxies;

import $group__.utilities.structures.Singleton;
import com.google.common.base.Stopwatch;
import net.minecraftforge.fml.event.lifecycle.*;
import org.apache.logging.log4j.Logger;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.function.Consumer;

import static $group__.ModGlobals.LOGGER;
import static $group__.utilities.LoggerUtilities.EnumMessages.FACTORY_PARAMETERIZED_MESSAGE;
import static $group__.utilities.LoggerUtilities.EnumMessages.PREFIX_MOD_LIFECYCLE_MESSAGE;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

public abstract class Proxy extends Singleton implements IProxy {
	protected Proxy(Logger logger) { super(logger); }

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean onModLifecycle(ModLifecycleEvent event) {
		if (event instanceof FMLFingerprintViolationEvent)
			return processEvent("Fingerprint violation", (FMLFingerprintViolationEvent) event, this::onFingerprintViolation);
		else if (event instanceof FMLCommonSetupEvent)
			return processEvent("Setup", (FMLCommonSetupEvent) event, this::onSetup);
		else if (event instanceof InterModEnqueueEvent)
			return processEvent("IMC enqueuing", (InterModEnqueueEvent) event, this::onInterModEnqueue);
		else if (event instanceof InterModProcessEvent)
			return processEvent("IMC processing", (InterModProcessEvent) event, this::onInterModProcess);
		else if (event instanceof FMLLoadCompleteEvent)
			return processEvent("Loading completion", (FMLLoadCompleteEvent) event, this::onLoadComplete);
		else if (event instanceof FMLModIdMappingEvent)
			return processEvent("Mod ID mapping", (FMLModIdMappingEvent) event, this::onModIdMapping);
		else if (event instanceof GatherDataEvent)
			return processEvent("Data gathering", (GatherDataEvent) event, this::onGatherData);
		return false;
	}

	protected static <E extends ModLifecycleEvent> boolean processEvent(String name, E event, Consumer<? super E> processor) {
		LOGGER.info(() -> PREFIX_MOD_LIFECYCLE_MESSAGE.makeMessage(FACTORY_PARAMETERIZED_MESSAGE.makeMessage("{} started", name)).getFormattedMessage());
		Stopwatch stopwatch = Stopwatch.createStarted();
		processor.accept(event);
		LOGGER.info(() -> PREFIX_MOD_LIFECYCLE_MESSAGE.makeMessage(FACTORY_PARAMETERIZED_MESSAGE.makeMessage("{} ended ({} <- {} ns)", name, stopwatch.stop(), stopwatch.elapsed(NANOSECONDS))).getFormattedMessage());
		return true;
	}
}
