package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.client;

import com.google.common.base.Suppliers;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Immutable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.common.ModCommonProxy;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.proxies.AbstractClientProxy;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.UIConstants;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ui.debug.UIMinecraftDebug;
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
@Immutable
public final class ModClientProxy
		extends AbstractClientProxy {
	private static final Supplier<ModClientProxy> INSTANCE = Suppliers.memoize(ModClientProxy::new);

	private ModClientProxy() {
		super(ModCommonProxy.getInstance());
	}

	public static ModClientProxy getInstance() { return AssertionUtilities.assertNonnull(INSTANCE.get()); }

	@Override
	protected void onConstruction() {

	}

	@Override
	protected void onFingerprintViolation(FMLFingerprintViolationEvent event) {}

	@Override
	protected void onSetup(FMLCommonSetupEvent event) {
		// COMMENT sneak the configuring in effectively-client common setup
		// COMMENT there may be a better solution though...  suggestions welcome
		ConfigurationTemplate.configureSafe(UIConfiguration.getInstance(),
				() -> new UIConfiguration.ConfigurationData(UIConfiguration.getBootstrapLogger(),
						new LoggingThrowableHandler<>(new ThreadLocalThrowableHandler<>(), UIConfiguration.getBootstrapLogger()),
						MinecraftLocaleUtilities::getCurrentLocale));
	}

	@Override
	protected void onInterModEnqueue(InterModEnqueueEvent event) {

	}

	@Override
	protected void onInterModProcess(InterModProcessEvent event) {

	}

	@Override
	protected void onLoadComplete(FMLLoadCompleteEvent event) {
		UIConfiguration.MinecraftSpecific.loadComplete();
	}

	@Override
	protected void onModIdMapping(FMLModIdMappingEvent event) {

	}

	@Override
	protected void onGatherData(GatherDataEvent event) {

	}

	@Override
	protected void onSetupSided(FMLClientSetupEvent event) {
		if (UIConstants.getBuildType().isDebug())
			UIMinecraftDebug.registerUIFactory();
	}
}
