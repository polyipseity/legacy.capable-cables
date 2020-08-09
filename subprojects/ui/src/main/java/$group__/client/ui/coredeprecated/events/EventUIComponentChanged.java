package $group__.client.ui.coredeprecated.events;

import $group__.client.ui.mvvm.views.components.IUIComponent;
import $group__.utilities.events.EnumEventHookStage;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class EventUIComponentChanged<T> extends EventUIComponent.Outbound {
	protected final T previous, next;

	protected EventUIComponentChanged(EnumEventHookStage stage, IUIComponent component, T previous, T next) {
		super(stage, component);
		this.previous = previous;
		this.next = next;
	}

	public T getPrevious() { return previous; }

	public T getNext() { return next; }
}
