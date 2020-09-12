package $group__.ui.events.bus;

import $group__.utilities.events.EnumEventHookStage;
import $group__.utilities.events.EventHook;

public abstract class EventUI extends EventHook {
	protected EventUI(EnumEventHookStage stage) { super(stage); }
}
