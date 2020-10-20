package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.proxies.core;

import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;

import javax.annotation.Nullable;

@FunctionalInterface
public interface IProxy {
	boolean onModLifecycle(@Nullable ModLifecycleEvent event);
}
