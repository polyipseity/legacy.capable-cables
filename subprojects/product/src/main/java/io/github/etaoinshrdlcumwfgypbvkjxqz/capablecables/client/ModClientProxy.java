package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.client;

import com.google.common.base.Suppliers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.proxies.AbstractClientProxy;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConstants;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.debug.UIDebugMinecraft;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.AssertionUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.minecraft.internationalization.MinecraftLocaleUtilities;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.templates.ConfigurationTemplate;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.throwable.LoggingThrowableHandler;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.throwable.ThreadLocalThrowableHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.*;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public final class ModClientProxy
		extends AbstractClientProxy {
	private static final Supplier<ModClientProxy> INSTANCE = Suppliers.memoize(ModClientProxy::new);

	private ModClientProxy() {}

	public static ModClientProxy getInstance() { return AssertionUtilities.assertNonnull(INSTANCE.get()); }

	@Override
	public void onFingerprintViolation(FMLFingerprintViolationEvent event) {}

	@Override
	public void onSetup(FMLCommonSetupEvent event) {
		// COMMENT sneak the configuring in effectively-client common setup
		// COMMENT there may be a better solution though...  suggestions welcome
		ConfigurationTemplate.configureSafe(UIConfiguration.getInstance(),
				() -> new UIConfiguration.ConfigurationData(UIConfiguration.getBootstrapLogger(),
						new LoggingThrowableHandler<>(new ThreadLocalThrowableHandler<>(), UIConfiguration.getBootstrapLogger()),
						MinecraftLocaleUtilities::getCurrentLocale));
	}

	@Override
	public void onSetupSided(FMLClientSetupEvent event) {
		if (UIConstants.getBuildType().isDebug())
			UIDebugMinecraft.registerUIFactory();
	}

	@Override
	public void onInterModEnqueue(InterModEnqueueEvent event) {

	}

	@Override
	public void onInterModProcess(InterModProcessEvent event) {

	}

	@Override
	public void onLoadComplete(FMLLoadCompleteEvent event) {
		UIConfiguration.MinecraftSpecific.loadComplete();
	}

	@Override
	public void onGatherData(GatherDataEvent event) {

	}

	@Override
	public void onModIdMapping(FMLModIdMappingEvent event) {

	}
}
