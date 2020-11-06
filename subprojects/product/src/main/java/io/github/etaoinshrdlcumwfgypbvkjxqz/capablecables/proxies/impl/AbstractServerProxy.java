package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.proxies.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ModConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.proxies.core.IProxy;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.ResourceBundle;

@OnlyIn(Dist.DEDICATED_SERVER)
public abstract class AbstractServerProxy
		extends AbstractProxy<FMLDedicatedServerSetupEvent> {
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(ModConfiguration.getInstance());

	public AbstractServerProxy(@Nullable IProxy parent) {
		super(parent);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean onModLifecycle(@Nullable ModLifecycleEvent event) {
		if (super.onModLifecycle(event))
			return true;
		else if (event instanceof FMLDedicatedServerSetupEvent)
			return processEvent(getResourceBundle().getString("event.server_setup.name"), (FMLDedicatedServerSetupEvent) event, this::onSetupSided);
		return false;
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }
}
