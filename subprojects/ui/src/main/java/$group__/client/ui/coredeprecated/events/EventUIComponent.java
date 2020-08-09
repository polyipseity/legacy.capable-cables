package $group__.client.ui.coredeprecated.events;

import $group__.client.ui.mvvm.views.domlike.components.IUIComponentDOMLike;
import $group__.utilities.events.EnumEventHookStage;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class EventUIComponent extends EventUI {
	protected final IUIComponentDOMLike component;

	protected EventUIComponent(EnumEventHookStage stage, IUIComponentDOMLike component) {
		super(stage);
		this.component = component;
	}

	public IUIComponentDOMLike getComponent() { return component; }

	@OnlyIn(Dist.CLIENT)
	public static abstract class Inbound extends EventUIComponent {
		protected Inbound(EnumEventHookStage stage, IUIComponentDOMLike component) { super(stage, component); }
	}

	@OnlyIn(Dist.CLIENT)
	public static abstract class Outbound extends EventUIComponent {
		protected Outbound(EnumEventHookStage stage, IUIComponentDOMLike component) { super(stage, component); }
	}
}
