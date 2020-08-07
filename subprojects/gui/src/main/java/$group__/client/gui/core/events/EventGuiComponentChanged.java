package $group__.client.gui.core.events;

import $group__.client.gui.core.IGuiComponent;
import $group__.utilities.events.EnumEventHookStage;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class EventGuiComponentChanged<T> extends EventGuiComponent.Outbound {
	protected final T previous, next;

	protected EventGuiComponentChanged(EnumEventHookStage stage, IGuiComponent<?, ?, ?> component, T previous, T next) {
		super(stage, component);
		this.previous = previous;
		this.next = next;
	}

	public T getPrevious() { return previous; }

	public T getNext() { return next; }
}
