package $group__.utilities.events;

import net.minecraftforge.eventbus.api.Event;

public abstract class EventHook extends Event {
	protected final EnumEventHookStage stage;

	protected EventHook(EnumEventHookStage stage) { this.stage = stage; }

	public EnumEventHookStage getStage() { return stage; }
}
