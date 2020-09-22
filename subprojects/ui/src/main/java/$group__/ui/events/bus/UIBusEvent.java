package $group__.ui.events.bus;

import $group__.utilities.events.EnumHookStage;
import $group__.utilities.events.HookEvent;

public abstract class UIBusEvent extends HookEvent {
	protected UIBusEvent(EnumHookStage stage) { super(stage); }
}
