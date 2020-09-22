package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.events;

import net.minecraftforge.eventbus.api.Event;

public abstract class HookEvent extends Event {
	protected final EnumHookStage stage;

	protected HookEvent(EnumHookStage stage) { this.stage = stage; }

	public EnumHookStage getStage() { return stage; }
}
