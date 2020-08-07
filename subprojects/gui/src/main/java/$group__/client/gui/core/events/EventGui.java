package $group__.client.gui.core.events;

import $group__.utilities.events.EnumEventHookStage;
import $group__.utilities.events.EventHook;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@OnlyIn(Dist.CLIENT)
@Cancelable
@Event.HasResult
public abstract class EventGui extends EventHook {
	protected EventGui(EnumEventHookStage stage) { super(stage); }
}
