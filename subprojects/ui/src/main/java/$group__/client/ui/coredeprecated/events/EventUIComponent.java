package $group__.client.ui.coredeprecated.events;

import $group__.client.ui.mvvm.views.components.IUIComponent;
import $group__.utilities.events.EnumEventHookStage;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class EventUIComponent extends EventUI {
	protected final IUIComponent component;

	protected EventUIComponent(EnumEventHookStage stage, IUIComponent component) {
		super(stage);
		this.component = component;
	}

	public IUIComponent getComponent() { return component; }

	@OnlyIn(Dist.CLIENT)
	public static abstract class Inbound extends EventUIComponent {
		protected Inbound(EnumEventHookStage stage, IUIComponent component) { super(stage, component); }
	}

	@OnlyIn(Dist.CLIENT)
	public static abstract class Outbound extends EventUIComponent {
		protected Outbound(EnumEventHookStage stage, IUIComponent component) { super(stage, component); }
	}
}
