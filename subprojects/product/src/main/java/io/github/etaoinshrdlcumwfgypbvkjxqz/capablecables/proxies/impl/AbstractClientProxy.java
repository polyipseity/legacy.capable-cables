package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.proxies.impl;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.ModConfiguration;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.proxies.core.IProxy;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.systems.templates.CommonConfigurationTemplate;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.ResourceBundle;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractClientProxy
		extends AbstractProxy<FMLClientSetupEvent> {
	private static final ResourceBundle RESOURCE_BUNDLE = CommonConfigurationTemplate.createBundle(ModConfiguration.getInstance());

	public AbstractClientProxy(@Nullable IProxy parent) {
		super(parent);
	}

	@Override
	@OverridingMethodsMustInvokeSuper
	public boolean onModLifecycle(@Nullable ModLifecycleEvent event) {
		if (super.onModLifecycle(event))
			return true;
		else if (event instanceof FMLClientSetupEvent)
			return processEvent(getResourceBundle().getString("event.client_setup.name"), (FMLClientSetupEvent) event, this::onSetupSided);
		return false;
	}

	protected static ResourceBundle getResourceBundle() { return RESOURCE_BUNDLE; }
}
