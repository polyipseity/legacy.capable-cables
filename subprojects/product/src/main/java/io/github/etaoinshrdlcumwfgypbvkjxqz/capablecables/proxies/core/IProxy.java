package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.proxies.core;

import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;

@FunctionalInterface
public interface IProxy {
	boolean onModLifecycle(@Nullable ModLifecycleEvent event);
}
