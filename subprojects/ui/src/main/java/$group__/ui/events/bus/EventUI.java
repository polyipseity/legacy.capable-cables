package $group__.ui.events.bus;

import $group__.utilities.events.EnumEventHookStage;
import $group__.utilities.events.EventHook;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
@Event.HasResult
public abstract class EventUI extends EventHook {
	protected EventUI(EnumEventHookStage stage) { super(stage); }
}
