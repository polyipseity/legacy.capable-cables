package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.proxies.impl;

import com.google.common.base.Stopwatch;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ModConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ModMarkers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.proxies.core.IProxy;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;
import net.minecraftforge.fml.event.lifecycle.*;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

public abstract class AbstractProxy<S>
		implements IProxy {
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(ModConfiguration.getInstance());
	@Nullable
	private final IProxy parent; // COMMENT we own the parent

	protected AbstractProxy(@Nullable IProxy parent) {
		this.parent = parent;
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean onModLifecycle(@Nullable ModLifecycleEvent event) {
		boolean ret = getParent().map(superProxy -> superProxy.onModLifecycle(event)).orElse(false);
		if (event == null)
			ret |= processEvent(getResourceBundle().getString("event.construction.name"), null, evt -> onConstruction());
		else if (event instanceof FMLFingerprintViolationEvent)
			ret |= processEvent(getResourceBundle().getString("event.fingerprint_violation.name"), (FMLFingerprintViolationEvent) event, this::onFingerprintViolation);
		else if (event instanceof FMLCommonSetupEvent)
			ret |= processEvent(getResourceBundle().getString("event.setup.name"), (FMLCommonSetupEvent) event, this::onSetup);
		else if (event instanceof InterModEnqueueEvent)
			ret |= processEvent(getResourceBundle().getString("event.imc_enqueue.name"), (InterModEnqueueEvent) event, this::onInterModEnqueue);
		else if (event instanceof InterModProcessEvent)
			ret |= processEvent(getResourceBundle().getString("event.imc_process.name"), (InterModProcessEvent) event, this::onInterModProcess);
		else if (event instanceof FMLLoadCompleteEvent)
			ret |= processEvent(getResourceBundle().getString("event.load_complete.name"), (FMLLoadCompleteEvent) event, this::onLoadComplete);
		else if (event instanceof FMLModIdMappingEvent)
			ret |= processEvent(getResourceBundle().getString("event.mod_id_mapping.name"), (FMLModIdMappingEvent) event, this::onModIDMapping);
		else if (event instanceof GatherDataEvent)
			ret |= processEvent(getResourceBundle().getString("event.gather_data.name"), (GatherDataEvent) event, this::onGatherData);
		return ret;
	}

	protected Optional<? extends IProxy> getParent() {
		return Optional.ofNullable(parent);
	}

	@SuppressWarnings("SameReturnValue")
	protected static <E extends ModLifecycleEvent> boolean processEvent(CharSequence name, @Nullable E event, Consumer<? super E> processor) {
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

	protected abstract void onConstruction();

	protected abstract void onFingerprintViolation(FMLFingerprintViolationEvent event);

	protected abstract void onSetup(FMLCommonSetupEvent event);

	protected abstract void onInterModEnqueue(InterModEnqueueEvent event);

	protected abstract void onInterModProcess(InterModProcessEvent event);

	protected abstract void onLoadComplete(FMLLoadCompleteEvent event);

	protected abstract void onModIDMapping(FMLModIdMappingEvent event);

	protected abstract void onGatherData(GatherDataEvent event);

	protected abstract void onSetupSided(S event);
}
