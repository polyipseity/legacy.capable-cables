package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.proxies;

import com.google.common.base.Stopwatch;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ModConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ModMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.CommonConfigurationTemplate;
import net.minecraftforge.fml.event.lifecycle.*;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

public abstract class AbstractProxy<T extends ModLifecycleEvent> implements IProxy<T> {
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(ModConfiguration.getInstance());

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean onModLifecycle(ModLifecycleEvent event) {
		if (event instanceof FMLFingerprintViolationEvent)
			return processEvent(getResourceBundle().getString("event.fingerprint_violation.name"), (FMLFingerprintViolationEvent) event, this::onFingerprintViolation);
		else if (event instanceof FMLCommonSetupEvent)
			return processEvent(getResourceBundle().getString("event.setup.name"), (FMLCommonSetupEvent) event, this::onSetup);
		else if (event instanceof InterModEnqueueEvent)
			return processEvent(getResourceBundle().getString("event.imc_enqueue.name"), (InterModEnqueueEvent) event, this::onInterModEnqueue);
		else if (event instanceof InterModProcessEvent)
			return processEvent(getResourceBundle().getString("event.imc_process.name"), (InterModProcessEvent) event, this::onInterModProcess);
		else if (event instanceof FMLLoadCompleteEvent)
			return processEvent(getResourceBundle().getString("event.load_complete.name"), (FMLLoadCompleteEvent) event, this::onLoadComplete);
		else if (event instanceof FMLModIdMappingEvent)
			return processEvent(getResourceBundle().getString("event.mod_id_mapping.name"), (FMLModIdMappingEvent) event, this::onModIdMapping);
		else if (event instanceof GatherDataEvent)
			return processEvent(getResourceBundle().getString("event.gather_data.name"), (GatherDataEvent) event, this::onGatherData);
		return false;
	}

	@SuppressWarnings("SameReturnValue")
	protected static <E extends ModLifecycleEvent> boolean processEvent(String name, E event, Consumer<? super E> processor) {
		ModConfiguration.getInstance().getLogger()
				.atInfo()
				.addMarker(ModMarkers.getInstance().getMarkerModLifecycle())
				.addArgument(name)
				.log(() -> getResourceBundle().getString("event.process.start"));
		Stopwatch stopwatch = Stopwatch.createStarted();
		processor.accept(event);
		stopwatch.stop();
		ModConfiguration.getInstance().getLogger()
				.atInfo()
				.addMarker(ModMarkers.getInstance().getMarkerModLifecycle())
				.addArgument(name).addArgument(stopwatch::toString).addArgument(() -> stopwatch.elapsed(NANOSECONDS))
				.log(() -> getResourceBundle().getString("event.process.end"));
		return true;
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }
}
