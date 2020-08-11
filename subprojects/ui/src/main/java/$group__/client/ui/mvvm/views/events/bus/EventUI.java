package $group__.client.ui.mvvm.views.events.bus;

import $group__.utilities.events.EnumEventHookStage;
import $group__.utilities.events.EventHook;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@OnlyIn(Dist.CLIENT)
@Cancelable
@Event.HasResult
public abstract class EventUI extends EventHook {
	protected EventUI(EnumEventHookStage stage) { super(stage); }
}
